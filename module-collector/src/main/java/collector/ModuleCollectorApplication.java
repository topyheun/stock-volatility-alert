package collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ModuleCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleCollectorApplication.class, args);
    }

}
