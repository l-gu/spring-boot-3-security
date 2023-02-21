package org.demo.myapp.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/doc", produces = MediaType.TEXT_PLAIN_VALUE)
public class DocRestController {

	private static final Logger logger = LoggerFactory.getLogger(DocRestController.class);
	
	
    public DocRestController() {
		super();
		logger.info("controller created");
	}

	@GetMapping("/page1")
    String httpGetPage1() {
		logger.debug("page1");
		return "Doc : page 1";
    }

	@GetMapping("/page2")
    String httpGetPage2() {
		logger.debug("page2");
		return "Doc : page 2";
    }

}
