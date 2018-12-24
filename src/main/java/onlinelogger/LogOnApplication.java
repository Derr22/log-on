package onlinelogger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@SpringBootApplication
@ComponentScan("controllers")
public class LogOnApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogOnApplication.class, args);
	}
}
