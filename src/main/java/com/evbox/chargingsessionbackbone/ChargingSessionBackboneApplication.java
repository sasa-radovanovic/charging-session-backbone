package com.evbox.chargingsessionbackbone;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@Slf4j
public class ChargingSessionBackboneApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ChargingSessionBackboneApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("------------------------------");
		log.info("Charging session backbone started");
		log.info("------------------------------");
	}
}
