package phan.recipesite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "phan.recipesite")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
