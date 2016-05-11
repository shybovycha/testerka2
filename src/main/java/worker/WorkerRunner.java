package worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Created by shybovycha on 10/05/16.
 */
@SpringBootApplication
@Configuration
@EnableAutoConfiguration(exclude = WebMvcAutoConfiguration.class)
public class WorkerRunner {
    public static void main(String[] args) {
        SpringApplication.run(CheckerWorker.class, args);
    }
}
