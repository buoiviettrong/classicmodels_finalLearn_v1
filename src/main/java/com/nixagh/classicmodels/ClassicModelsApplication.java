package com.nixagh.classicmodels;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
