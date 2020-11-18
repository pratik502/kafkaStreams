package com.oracle.kafka.demo.producer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

	@Value("${kafkaprop.brokersUrl}")
	private String brokersUrl;

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



	@Bean
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		setDefaults(configProps);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}
	
	@Bean
	public ProducerFactory<String, byte[]> producerFactoryForFile() {
		Map<String, Object> configProps = new HashMap<>();
		setDefaults(configProps);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}
	
	public void setDefaults(Map<String, Object> configProps) {
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokersUrl);
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

	@Bean(name = "TextProducer")
	public KafkaTemplate<String, String> kafkaTextTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
	
	@Bean(name = "ByteArrayProducer")
	public KafkaTemplate<String, byte[]> kafkaByteArrayTemplate() {
		return new KafkaTemplate<>(producerFactoryForFile());
	}
}