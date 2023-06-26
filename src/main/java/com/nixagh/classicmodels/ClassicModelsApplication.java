package com.nixagh.classicmodels;

import com.nixagh.classicmodels._common.auth.AuthenticationService;
import com.nixagh.classicmodels._common.auth.RegisterRequest;
import com.nixagh.classicmodels.entity.user.LoginType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.nixagh.classicmodels.entity.user.Role.ADMIN;
import static com.nixagh.classicmodels.entity.user.Role.MANAGER;

@SpringBootApplication
public class ClassicModelsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassicModelsApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner commandLineRunner(
//			AuthenticationService service
//	) {
//		return args -> {
//			var admin = RegisterRequest.builder()
//					.firstName("Admin")
//					.lastName("Admin")
//					.email("admin@mail.com")
//					.password("password")
//					.role(ADMIN)
//					.type(LoginType.NORMAL)
//					.build();
//			System.out.println("Admin token: " + service.register(admin).getAccessToken());
//
//			var manager = RegisterRequest.builder()
//					.firstName("Admin")
//					.lastName("Admin")
//					.email("manager@mail.com")
//					.password("password")
//					.role(MANAGER)
//					.type(LoginType.NORMAL)
//					.build();
//			System.out.println("Manager token: " + service.register(manager).getAccessToken());
//		};
//	}

}
