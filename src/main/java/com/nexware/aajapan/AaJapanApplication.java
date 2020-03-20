package com.nexware.aajapan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.nexware.aajapan.property.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class })
public class AaJapanApplication{

	public static void main(String[] args) {
		SpringApplication.run(AaJapanApplication.class, args);
	}
}
