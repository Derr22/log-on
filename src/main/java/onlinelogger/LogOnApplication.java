package onlinelogger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("controllers")
public class LogOnApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogOnApplication.class, args);
	}
}
