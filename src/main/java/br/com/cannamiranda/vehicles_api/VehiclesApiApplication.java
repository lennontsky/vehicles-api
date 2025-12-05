package br.com.cannamiranda.vehicles_api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Vehicles API",
        version = "0.1",
        description = "API para teste técnico da Tinnova")
)
public class VehiclesApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(VehiclesApiApplication.class, args);
	}
}
