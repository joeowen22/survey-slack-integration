package digital.and.bootcamp.slackintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SlackIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlackIntegrationApplication.class, args);
	}

}
