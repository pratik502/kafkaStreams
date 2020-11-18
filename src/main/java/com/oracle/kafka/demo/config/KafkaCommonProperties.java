package com.oracle.kafka.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafkaprop")
public class KafkaCommonProperties {

	private String streamThreads;
	private String replicationFactor;
	private String brokersUrl;
	private String securityProtocolConfig;
	private String sslTruststoreLocationConfig;
	private String sslTruststorePasswordConfig;
	private String sslKeystoreLocationConfig;
	private String sslKeystorePasswordConfig;
	private String sslEndpointIdentificationAlgorithmConfig;
	private String sslTrusttoreTypeConfig;
	private String sslKeyPasswordConfig;
	private String sslEnabledProtocolsConfig;
	private String inFilesPath;
	private String outFilesPath;
	private String logFilesPath;
}
