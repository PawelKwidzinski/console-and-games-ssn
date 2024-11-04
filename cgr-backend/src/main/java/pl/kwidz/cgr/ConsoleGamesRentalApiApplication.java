package pl.kwidz.cgr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ConsoleGamesRentalApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsoleGamesRentalApiApplication.class, args);
	}

}
