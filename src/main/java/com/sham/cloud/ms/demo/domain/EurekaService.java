package com.sham.cloud.ms.demo.domain;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

@Service
public class EurekaService {

	@Value("${app.greetingService.serviceId}")
	private String greetingServiceId;

	@Autowired
	private DiscoveryClient discoveryClient;

	public URI serviceInstancesByApplicationNameURI() {
		final List<ServiceInstance> instances = this.discoveryClient.getInstances(this.greetingServiceId);
		return instances.stream().findFirst().map(instance -> instance.getUri()).orElse(null);
	}
}
