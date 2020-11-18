package com.oracle.kafka.demo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.oracle.kafka.demo.config.KafkaCommonProperties;

@SpringBootApplication
@EnableConfigurationProperties(KafkaCommonProperties.class)
//@EntityScan("com.oracle.kafka.demo.config.Notes")
public class KafkaStreamsApplication implements ApplicationRunner {

	public static void main(String[] args) {
		 SpringApplication.run(KafkaStreamsApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// sendMessage("Hi Welcome to Spring For Apache Kafka");
	}
}


//@ConfigurationPropertiesScan("com.oracle.kafka.demo.config.KafkaCommonProperties")