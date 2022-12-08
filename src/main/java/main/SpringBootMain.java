package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.apache.log4j.BasicConfigurator.configure;

@SpringBootApplication
public class SpringBootMain {

    private boolean isTesting = false;

    static {
        configure();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMain.class, args);
    }


}
