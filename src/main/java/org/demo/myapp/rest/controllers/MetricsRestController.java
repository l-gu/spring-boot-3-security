package org.demo.myapp.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/metrics", produces = MediaType.TEXT_PLAIN_VALUE)
public class MetricsRestController {

	private static final Logger logger = LoggerFactory.getLogger(MetricsRestController.class);
	
	
    public MetricsRestController() {
		super();
		logger.info("controller created");
	}

	@GetMapping("/aaa")
    String httpGetPage1() {
		logger.debug("/metrics/aaa");
		return "metrics : foo";
    }

	@GetMapping("/bbb")
    String httpGetPage2() {
		logger.debug("/metrics/bbb");
		return "metrics : bar";
    }

}
