package sube.interviews.mareoenvios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication(scanBasePackages = "sube.interviews.mareoenvios")
@EnableCaching
public class MareoEnviosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MareoEnviosApplication.class, args);
    }

}
