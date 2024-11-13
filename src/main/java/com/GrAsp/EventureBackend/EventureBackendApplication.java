package com.GrAsp.EventureBackend;

import com.GrAsp.EventureBackend.security.config.JwtConfigProperties;
import com.GrAsp.EventureBackend.security.config.RsaKeyConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtConfigProperties.class, RsaKeyConfigProperties.class})
public class EventureBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventureBackendApplication.class, args);
	}

}
