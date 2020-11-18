package com.oracle.kafka.demo.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.FailOnInvalidTimestamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

@Configuration
public class KafkaStreamConfig {

	@Value("${kafkaprop.streamThreads}")
	private int threads;

	@Value("${kafkaprop.replicationFactor}")
	private int replicationFactor;

	@Value("${kafkaprop.brokersUrl}")
	private String brokersUrl;

	@Value("${kafkaprop.autoOffsetResetConfig}")
	private String autoOffsetResetConfig;
	
	
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

	@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
	public StreamsConfig kStreamsConfigs() {
		Map<String, Object> config = new HashMap<>();
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "default");
		setDefaults(config);
		return new StreamsConfig(config);
	}

	public void setDefaults(Map<String, Object> config) {
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokersUrl);
		config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		config.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, FailOnInvalidTimestamp.class);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetResetConfig);
		
		config.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
		config.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,trustStoreLocation);
		config.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, trustStorePassword);
		config.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG,keyStoreLocation);
		config.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, keyStorePassword);
		config.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, keyPassword);
		config.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, endpointAlgorithm);
		config.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, trustStoreType);
		config.put(SslConfigs.SSL_ENABLED_PROTOCOLS_CONFIG, sslProtocols);
	}

	@Bean("app1StreamBuilder")
	public StreamsBuilderFactoryBean app1StreamBuilderFactoryBean() {
		Map<String, Object> config = new HashMap<>();
		setDefaults(config);
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "app1");
		config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
		config.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 300);
		config.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, threads);
		config.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, replicationFactor);
		return new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(config));
	}

	/*@Bean("app2StreamBuilder")
	public StreamsBuilderFactoryBean app2StreamBuilderFactoryBean() {
		Map<String, Object> config = new HashMap<>();
		setDefaults(config);
		config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "app2");
		config.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 30000);
		config.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, threads);
		config.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, replicationFactor);
		return new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(config));
	}*/
}