package com.oracle.kafka.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class KafkaStream {
	

	@Value("${kafkaprop.log-files-path}")
	private String logpath;
	

	@Bean("app1StreamTopology")
	public KStream<String, String> startProcessing(@Qualifier("app1StreamBuilder") StreamsBuilder builder) {

		
		KStream<String, String> source = builder.stream("PRATIK.SERVER.REQ");
		source.foreach((String key, String value) -> {
			System.out.println("Event key"+key + ", Event value " + value);
			System.out.println("LogPath: "+logpath);
			try {
				Files.write(Paths.get(logpath), ("\n"+value).getBytes(), StandardOpenOption.APPEND);
				System.out.println("Log wirtten..");
			} catch (IOException e) {
				System.err.println("Error while writing file");
				e.printStackTrace();
			}
		});
	
		return source;
	}
}


//.\bin\windows\kafka-topics.bat --zookeeper localhost:2181 --describe --topic ROUTER
