package de.htw.ai.momotarian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SongsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SongsApplication.class, args);
	}
}
