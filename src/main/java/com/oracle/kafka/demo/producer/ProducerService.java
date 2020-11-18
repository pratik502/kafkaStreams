package com.oracle.kafka.demo.producer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

	@Value("${kafkaprop.in-files-path}")
	private String inpath;

	@Autowired
	@Qualifier("TextProducer")
	private KafkaTemplate<String, String> kafkaTextProducerTemplate;

	@Autowired
	@Qualifier("ByteArrayProducer")
	private KafkaTemplate<String, byte[]> kafkaFileProducerTemplate;

	private static Lock lock = new ReentrantLock();
	private static Condition condition = lock.newCondition();
	private Thread checkSize=null;
	private Thread runnable=null;

	public void writeFromFile(String src, String dest) {

		// Start directory size check thread
		startDirectorySizeThread();

		// Creating a File object for directory
		startProcessingFiles(src, dest);

	}

	public void sendFileMessage(String key, byte[] value) {
		System.out.println("Sending File..");
		kafkaFileProducerTemplate.send("TEST", key, value);
		System.out.println("File Sent..");
	}

	public void writeLog(String key, String value) {
		kafkaTextProducerTemplate.send("PRATIK.SERVER.REQ", key, value);
	}

	public void startDirectorySizeThread() {
		

		if(checkSize == null) {
			checkSize = new Thread(() -> {
				boolean locked = false;
				while (true) {
					try {
						locked = lock.tryLock(1, TimeUnit.SECONDS);
						if (locked) {
							// System.out.println("2 checking size.....");
							long size = Files.walk(Paths.get(inpath)).count();
							if (size > 1) {
								System.out.println("Size GOT===>" + size);
								condition.signal();

							}
						}

					} catch (InterruptedException | IOException e) {
						System.out.println("Lock exception  " + e);
					} finally {
						if (locked)
							lock.unlock();
					}

				}
			});
			
			System.out.println("Thread checkSize has not started yet...now starting");
			checkSize.start();
		}
		else {
			System.out.println("Thread  checkSize has already started");
		}
		
	}

	public void startProcessingFiles(String src, String dest) {
		

		if(runnable == null) {
		  runnable = new Thread(() -> {
			boolean locked = false;
			File directoryPath = new File(inpath);
			String filePath = null;

			while (true)
				try {
					locked = lock.tryLock();
					if (locked) {
						System.out.println("1 Waiting....");
						condition.await();
						System.out.println("1 Now started running=====>");
						// List of all files and directories
						String contents[] = directoryPath.list();
						System.out.println("List of files and directories in the specified directory:");
						for (int i = 0; i < contents.length; i++) {
							filePath = inpath + "\\" + contents[i];
							System.out.println(filePath);
							try {
								byte[] fileByteArray = Files.readAllBytes(Paths.get(filePath));
								sendFileMessage(String.join(":", src, dest, contents[i]), fileByteArray);
								writeLog("Success", "File put on Topic successfully " + contents[i] + " At time "+ LocalDateTime.now());
								Files.deleteIfExists(Paths.get(filePath));
							} catch (IOException e) {
								System.err.println("Error in reading file");
								e.printStackTrace();
								writeLog("Error", "Error in writing file at producer" + contents[i] + " At time "	+ LocalDateTime.now());
							}
						}
					}

				} catch (InterruptedException e) {
					System.out.println("Lock exception  " + e);
				} finally {
					if (locked)
						lock.unlock();
				}

		});

		System.out.println("Thread runnable has not started yet...now starting");
		runnable.start();
		}
		else {
			System.out.println("Thread runnable has already started");
		}
		

	}

	
}
