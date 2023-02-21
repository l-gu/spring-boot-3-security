package org.demo.myapp.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Application launched by SpringBoot starter
 * 
 * @author xx
 *
 */
@Component
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
    /**
     * Application entry point called by SpringBoot starter
     * 
     * @param args
     */
    public void run(String[] args) {
    	logger.info("Starting application...");
    	logger.debug("Starting application : args.length = " + args.length);

    }
}
