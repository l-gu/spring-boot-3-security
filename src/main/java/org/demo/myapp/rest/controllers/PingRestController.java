package org.demo.myapp.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(value = "/api/ping", produces = MediaType.TEXT_PLAIN_VALUE)
public class PingRestController {

	private static final Logger logger = LoggerFactory.getLogger(PingRestController.class);
	
	
    public PingRestController() {
		super();
		logger.info("controller created");
	}

	@GetMapping("")
    String httpRestPing() {
		logger.debug("ping");
		return "pong (ok)";
    }

    @GetMapping("/{id}")
    String httpRestPing(@PathVariable long id) {
		logger.debug("ping/{}", id);
		return "pong : " + id;
    }

}
