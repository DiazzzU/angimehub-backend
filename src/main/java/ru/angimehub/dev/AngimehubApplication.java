package ru.angimehub.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
@EntityScan("ru.angimehub.dev.*")
@EnableJpaRepositories("ru.angimehub.dev.*")
@ComponentScan(basePackages = "ru.angimehub.dev.*")
public class AngimehubApplication {

	public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
		SpringApplication.run(AngimehubApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}
		};
	}

}
