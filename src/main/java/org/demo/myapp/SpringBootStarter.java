package org.demo.myapp;

import org.demo.myapp.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * Spring Boot entry point <br>
 * 
 * In order to use "Convention Over Configuration" this class is supposed to be located <br> 
 * at the top level of the packages hierarchy 
 * 
 * @author Laurent Guerin
 *
 */
@SpringBootApplication
// If this class is not at the top level of the packages hierarchy use @ComponentScan
// @ComponentScan(basePackages = "org.demo") 
public class SpringBootStarter {

    /**
     * Main entry point for Spring Boot starting
     * 
     * @param args
     */
    public static void main(String[] args) {
    	ConfigurableApplicationContext appContext =  SpringApplication.run(SpringBootStarter.class, args);
    	
    	// get the application bean (the "root component")
    	Application application = appContext.getBean(Application.class);
    	
    	// launch application with its own entry point method
        application.run(args);
    }

}
