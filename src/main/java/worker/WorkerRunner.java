package worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created by shybovycha on 10/05/16.
 */
@SpringBootApplication(exclude = { EmbeddedServletContainerAutoConfiguration.class, WebMvcAutoConfiguration.class })
public class WorkerRunner {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplicationBuilder()
            .sources(CheckerWorker.class)
            .web(false)
            .build();

        app.run(args);
    }
}
