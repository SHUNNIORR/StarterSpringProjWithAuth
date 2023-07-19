package com.shunnior.springsecurity;

import com.shunnior.springsecurity.entities.RoleEntity;
import com.shunnior.springsecurity.entities.UserEntity;
import com.shunnior.springsecurity.repositories.UserRepository;
import com.shunnior.springsecurity.utils.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class SpringsecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringsecurityApplication.class, args);
	}

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;
	@Bean
	CommandLineRunner init(){
		return args -> {
			UserEntity userEntity = UserEntity.builder()
					.email("joperez@gmail.com")
					.username("joperez")
					.password(passwordEncoder.encode("12345"))
					.roles(Set.of(RoleEntity.builder().name(ERole.valueOf(ERole.ADMIN.name()))
									.build()))
					.build();
			UserEntity userEntity2 = UserEntity.builder()
					.email("ari@gmail.com")
					.username("ari")
					.password(passwordEncoder.encode("12345"))
					.roles(Set.of(RoleEntity.builder().name(ERole.valueOf(ERole.USER.name()))
							.build()))
					.build();
			UserEntity userEntity3 = UserEntity.builder()
					.email("kira@gmail.com")
					.username("kira")
					.password(passwordEncoder.encode("12345"))
					.roles(Set.of(RoleEntity.builder().name(ERole.valueOf(ERole.INVITED.name()))
							.build()))
					.build();

			userRepository.save(userEntity);
			userRepository.save(userEntity2);
			userRepository.save(userEntity3);
		};
	}
}
