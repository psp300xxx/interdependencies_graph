package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootMain {

    private boolean isTesting = false;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMain.class, args);
    }


}
