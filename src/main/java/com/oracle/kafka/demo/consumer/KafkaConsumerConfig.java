package com.oracle.kafka.demo.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

	@Value("${kafkaprop.brokersUrl}")
	private String brokersUrl;

	@Value("${kafkaprop.autoOffsetResetConfig}")
	private String autoOffsetResetConfig;

	@Value("${kafkaprop.replicationFactor}")
	private int replicationFactor;

	@Value("${kafkaprop.securityProtocolConfig}")
	private String securityProtocol;

	@Value("${kafkaprop.sslTruststoreLocationConfig}")
	private String trustStoreLocation;

	@Value("${kafkaprop.sslTruststorePasswordConfig}")
	private String trustStorePassword;

	@Value("${kafkaprop.sslKeystoreLocationConfig}")
	private String keyStoreLocation;

	@Value("${kafkaprop.sslKeystorePasswordConfig}")
	private String keyStorePassword;

	@Value("${kafkaprop.sslKeyPasswordConfig}")
	private String keyPassword;

	@Value("${kafkaprop.sslEndpointIdentificationAlgorithmConfig}")
	private String endpointAlgorithm;

	@Value("${kafkaprop.sslTrusttoreTypeConfig}")
	private String trustStoreType;

	@Value("${kafkaprop.sslEnabledProtocolsConfig}")
	private String sslProtocols;

	

	public void setDefault(Map<String, Object> configProps) {

		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokersUrl);
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetResetConfig);
		configProps.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, replicationFactor);
		configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
		configProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, trustStoreLocation);
		configProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, trustStorePassword);
		configProps.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, keyStoreLocation);
		configProps.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, keyStorePassword);
		configProps.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, keyPassword);
		configProps.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, endpointAlgorithm);
		configProps.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, trustStoreType);
		configProps.put(SslConfigs.SSL_ENABLED_PROTOCOLS_CONFIG, sslProtocols);
	}

	
	@Bean
	public ConsumerFactory<String, String> consumerFactoryForText() {
		Map<String, Object> configProps = new HashMap<>();
		setDefault(configProps);
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
		return new DefaultKafkaConsumerFactory<>(configProps);
	}
	
	@Bean
	public ConsumerFactory<String, byte[]> consumerFactoryForFiles() {
		Map<String, Object> configProps = new HashMap<>();
		setDefault(configProps);
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "group2");
		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean("StringDataConsumer")
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactoryForText());
		return factory;
	}
	
	@Bean("ByteArrayDataConsumer")
	public ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaListenerContainerFactoryFiles() {
		ConcurrentKafkaListenerContainerFactory<String, byte[]> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactoryForFiles());
		return factory;
	}

}