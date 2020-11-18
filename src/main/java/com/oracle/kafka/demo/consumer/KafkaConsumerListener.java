package com.oracle.kafka.demo.consumer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.oracle.kafka.demo.config.TransactionRecord;

@Component
@EnableKafka
public class KafkaConsumerListener {

	@Value("${kafkaprop.out-files-path}")
	private String outpath;

	@Autowired
	@Qualifier("TextProducer")
	private KafkaTemplate<String, String> kafkaTextProducerTemplate;
	
	@Autowired
	private TransactionRecordRepository transactionRecordRepository;

	@KafkaListener(topics = "TEST", groupId = "group2", containerFactory = "ByteArrayDataConsumer")
	public void listenForByte(ConsumerRecord<String, byte[]> record) {
		System.out.println("Received file Messasge in with key: " + record.key());
		System.out.println("Received file Messasge in with value: " + record.value());
		String[] keyArray = record.key().split(":");
		String src = keyArray[0], dest = keyArray[1];
		
		if (src.equalsIgnoreCase("file") && dest.equalsIgnoreCase("file")) {
			String filename=LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME).toString().replace(":", "_").replace(".","_")+".txt";
			//File file = new File(outpath + "\\" + keyArray[2]);
			File file = new File(outpath + "\\" + filename);
			try {
				file.createNewFile();
				Files.write(Paths.get(file.getAbsolutePath()), record.value());
				writeLog("Success", "File " + record.key() + " written successfully at time " + LocalDateTime.now());
			} catch (IOException e) {
				System.err.println("Error while writing file "+e);
				writeLog("Error","File " + record.key() + " has encountered error while writing at time " + LocalDateTime.now());
				e.printStackTrace();
			}
		}
		
		if (src.equalsIgnoreCase("file") && dest.equalsIgnoreCase("database")) {
			LocalDate today=LocalDate.now();
			TransactionRecord note=new TransactionRecord(today.getDayOfMonth()+"",today.getMonth()+"", today.getYear()+"",new String(record.value()));
			try {
				transactionRecordRepository.save(note);
				writeLog("Success", "Files record " + record.key() + " written successfully on db at time " + LocalDateTime.now());
			}
			catch(Exception e) {
				System.err.println("Error while db updation "+e);
				writeLog("Error","File " + record.key() + " has encountered error while writing in db at time " + LocalDateTime.now());
			}
		}

	}

	public void writeLog(String key, String value) {
		kafkaTextProducerTemplate.send("PRATIK.SERVER.REQ", key, value);
	}

	public static void main(String[] args) {
		String filename=LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME).toString().replace(":", "_").replace(".","_");
		System.out.println(filename);
	}
	/*
	 * @KafkaListener(topics = "PRATIK.SERVER.REQ", groupId = "group1",
	 * containerFactory = "StringDataConsumer") public void
	 * listen(ConsumerRecord<String,String> cr) {
	 * System.out.println("Received Messasge in with key: " + cr.key());
	 * System.out.println("Received Messasge in with value: " + cr.value()); }
	 */
}
