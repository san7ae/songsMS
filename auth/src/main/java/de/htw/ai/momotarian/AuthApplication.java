package de.htw.ai.momotarian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import java.io.IOException;

@SpringBootApplication
@EnableEurekaClient
public class AuthApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(AuthApplication.class,args);
	}

}
