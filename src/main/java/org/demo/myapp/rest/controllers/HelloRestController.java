package org.demo.myapp.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;



@RestController
@RequestMapping(value = "/api/hello", produces = MediaType.TEXT_PLAIN_VALUE)
public class HelloRestController {

	private static final Logger logger = LoggerFactory.getLogger(HelloRestController.class);
	
	
    public HelloRestController() {
		super();
		logger.info("controller created");
	}

    @RolesAllowed({Role.STANDARD, Role.MANAGER})
	@GetMapping("")
    String httpGet() {
		logger.debug("hello");
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if ( securityContext != null ) {
			logger.debug("hello : SecurityContext is NOT NULL");
			Authentication authentication = securityContext.getAuthentication();
			if ( authentication != null ) {
				logger.debug("hello : Authentication is NOT NULL");
				logger.debug("hello : isAuthenticated() = " + authentication.isAuthenticated() );
			}
			else {
				logger.debug("hello : Authentication is NULL");
			}
		}
		else {
			logger.debug("hello : SecurityContext is NULL");
		}
		return "Hello";
    }

    @GetMapping("/{name}")
    String httpRestPing(@PathVariable String name) {
		logger.debug("hello/{}", name);
		return "Hello " + name;
    }

    @RolesAllowed(Role.ADMIN)
    @GetMapping("/admin")
    String httpGetAdmin() {
		logger.debug("hello/admin");
		return "Hello : admin ";
    }

    @RolesAllowed(Role.MANAGER)
    @GetMapping("/manager")
    String httpGetManager() {
		logger.debug("hello/manager");
		return "Hello : manager ";
    }

}
