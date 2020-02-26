package com.sham.cloud.ms.demo;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.sham.cloud.ms.demo.domain.EurekaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private EurekaService eurekaService;

	@HystrixCommand(threadPoolKey = "sham2ThreadPool")
	public ResponseEntity<String> helloRemoteServiceCall(final String firstName, final String lastName) {
		final String url = this.eurekaService.serviceInstancesByApplicationNameURI().toString();
		final URI uri = UriComponentsBuilder.fromUriString(url.concat("/users/solution/{firstName}/{lastName}"))
				.buildAndExpand(firstName, lastName).toUri();
		log.info("### 2 SHAZAM = User Process");
		return this.restTemplate.getForEntity(uri, String.class);
	}

	@GetMapping("/{firstName}/{lastName}")
	public ResponseEntity<String> hello(@PathVariable final String firstName, @PathVariable final String lastName) {
		log.info("### 1 SHAZAM = User Process");
		return helloRemoteServiceCall(firstName, lastName);
	}

	@GetMapping("/solution/{firstName}/{lastName}")
	public String helloFinal(@PathVariable final String firstName, @PathVariable final String lastName) {
		log.info("###  3 SHAZAM = User Process");
		return "Hello! " + firstName + ", " + lastName;
	}

	@GetMapping("/uri")
	public String uri() {
		return this.eurekaService.serviceInstancesByApplicationNameURI().toString();
	}

}
