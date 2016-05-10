package worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by shybovycha on 10/05/16.
 */
@SpringBootApplication
@EnableScheduling
public class WorkerRunner {
    public static void main(String[] args) {
        SpringApplication.run(CheckerWorker.class);
    }
}
