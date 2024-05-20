package main.java.com.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
public class RentifyApplication {
    public static void main(String[] args) {
        SpringApplication.run(RentifyApplication.class, args);
    }
}
