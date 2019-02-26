package de.tuchemnitz.tomkr.msar;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "de.tuchemnitz.tomkr.msar")
//@EnableMongoRepositories(basePackages = "de.tuchemnitz.tomkr.metaapp.service.mongo")
public class Config {

	@Value("${elasticsearch.host}")
	public String host;
	
	@Value("${elasticsearch.port}")
	public int port;
	
	@Value("${elasticsearch.clustername}")
	public String clusterName;
	
	
	@Bean
	public Client client() throws UnknownHostException {
		Settings settings = Settings.builder().put("cluster.name", clusterName).build();
		TransportClient client = new PreBuiltTransportClient(settings);
		client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
		return client;
	}
}
