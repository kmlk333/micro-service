package com.ITrator.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

@EnableDiscoveryClient
@SpringBootApplication
@RestController
@RequestMapping("/root")
public class ClientApplication {

	
	@Autowired
	private EurekaClient client;
	
	@Autowired
	private RestTemplateBuilder restTemplateBuilder;
	
	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}

	@RequestMapping("/service")
	public String callService() {
		System.out.println("Service call");
		RestTemplate restTemplate = restTemplateBuilder.build();
		InstanceInfo instanceInfo = client.getNextServerFromEureka("service", false);
		String baseUrl = instanceInfo.getHomePageUrl();
		System.out.println("url is " + baseUrl);
		ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.GET, null, String.class);
		return response.getBody();
	}
	
	@RequestMapping("/bikeService")
	public String callBikeService() {
		System.out.println("callBikeService call");
		/*
		 * RestTemplate restTemplate = restTemplateBuilder.build(); InstanceInfo
		 * instanceInfo = client.getNextServerFromEureka("bike-service", false);
		 * //Application service = client.getApplication("bike-service"); String baseUrl
		 * = instanceInfo.getHomePageUrl(); ResponseEntity<String> response =
		 * restTemplate.exchange(baseUrl, HttpMethod.GET, null, String.class); return
		 * response.getBody();
		 */
		RestTemplate restTemplate = restTemplateBuilder.build();
		Application application = client.getApplication("bike-service");
        InstanceInfo instanceInfo = application.getInstances().get(0);
        String url = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort() + "/" + "bike";
        System.out.println("URL" + url);
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("RESPONSE " + response);
        return response;
	}
}
