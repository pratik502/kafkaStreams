package com.oracle.kafka.demo.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send/Data")
public class ProducerController {

	@Autowired
	private ProducerService pservice;

	@PostMapping("/source/{source}/dest/{dest}")
	public void getAndSendDataFromFile(@PathVariable("source") String source, @PathVariable("dest") String dest) {
			pservice.writeFromFile(source,dest);
		
	}

}
