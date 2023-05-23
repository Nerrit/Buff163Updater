package it.nerr.buff163updater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = {"it.nerr.buff163updater", "it.nerr.database"})
public class Buff163UpdaterApplication {

    public static void main(String[] args) {
        SpringApplication.run(Buff163UpdaterApplication.class, args);
    }

}
