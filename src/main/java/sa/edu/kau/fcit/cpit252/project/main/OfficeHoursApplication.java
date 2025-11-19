package sa.edu.kau.fcit.cpit252.project.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "sa.edu.kau.fcit.cpit252.project")
public class OfficeHoursApplication {

    public static void main(String[] args) {
        SpringApplication.run(OfficeHoursApplication.class, args);
    }
}
