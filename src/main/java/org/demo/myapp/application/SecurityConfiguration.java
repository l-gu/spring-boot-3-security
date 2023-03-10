package org.demo.myapp.application;

import org.demo.myapp.security.AuthenticatedUserFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//EnableGlobalMethodSecurity is deprecated => use EnableMethodSecurity instead
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true) // see https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html 
public class SecurityConfiguration {
	
	@Autowired
	private AuthenticatedUserFilter authenticatedUserFilter ;
	
	/**
	 * Configure SecurityFilterChain
	 * @param httpSecurity
	 * @return
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		
		//--- CSRF configuration : Starting from Spring Security 4.x, the CSRF protection is enabled by default.
		// httpSecurity.csrf().disable();
		
		//--- CORS 
		httpSecurity.cors().disable();
		
		//--- "remember me" Spring authentication
		// NB : The Spring security cookie based remember me authentication comes with some security challenges and this approach is not recommended for your production system. 
		// see https://www.javadevjournal.com/spring-security/spring-security-remember-me 
		httpSecurity.rememberMe().disable();
		
		//--- Define if URLs are always allowed or only for authenticated user.
		httpSecurity.authorizeHttpRequests()
			.requestMatchers("/metrics/**", "/doc/**" ).permitAll() // Specify that URLs are allowed by anyone
			.requestMatchers("/anonymous/**" ).anonymous() // Specify that URLs are allowed by anonymous users
			.requestMatchers("/admin/**" ).hasRole("ADMIN") // Specify that URLs are allowed by anonymous users
			.requestMatchers(HttpMethod.DELETE).hasAnyRole("USER", "ADMIN")  // Specifies a user requires a role
			.anyRequest().authenticated() ; // Specify that URLs are allowed by any authenticated user
		
		//--- Session management 
		// STATELESS : Spring Security will never create an HttpSession and it will never use it to obtain the SecurityContext
		httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		//--- Define behavior if user is not authenticated :
		// httpBasic(...) : configures HTTP Basic authentication : user/password asked by browser dialog box
		// httpSecurity.httpBasic(withDefaults()); // withDefaults() : returns a Customizer that does not alter the input argument

		// Specifies to support FORM based authentication : user/password asked by HTML FORM 
		// httpSecurity.formLogin(withDefaults()); // withDefaults() : returns a Customizer that does not alter the input argument
		
		// if nothing defined => return http status 403 Forbidden

		//--- Add "AuthenticatedUserFilter" before "UsernamePasswordAuthenticationFilter" (one of the known Filter classes)
		httpSecurity.addFilterBefore(authenticatedUserFilter, UsernamePasswordAuthenticationFilter.class);
		
		return httpSecurity.build();
	}
	
	// Spring : https://github.com/spring-projects/spring-boot/issues/23421
	//    "using the SecurityFilterChain bean will be the recommended approach to configure HttpSecurity going forward."
//  @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        //return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
//    }
	

}
