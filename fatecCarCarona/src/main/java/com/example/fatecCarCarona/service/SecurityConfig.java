package com.example.fatecCarCarona.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	CustomUserDetailsService userDetailsService;
	
	@Autowired
	SecurityFilter securityFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(HttpMethod.GET,"/cep/**").permitAll()
				.requestMatchers(HttpMethod.GET,"/userType").permitAll()
				.requestMatchers(HttpMethod.GET,"/courses").permitAll()
				.requestMatchers(HttpMethod.GET,"/genders").permitAll()
				.requestMatchers(HttpMethod.GET,"/cities/**").permitAll()
				.requestMatchers(HttpMethod.GET,"/states/**").permitAll()
				.requestMatchers(HttpMethod.GET,"/states").permitAll()
				.requestMatchers(HttpMethod.POST,"/users/criarPassageiro").permitAll()
				.requestMatchers(HttpMethod.POST,"/users/criarMotorista").permitAll()
				.requestMatchers(HttpMethod.POST,"/users/login").permitAll()
				.anyRequest().authenticated()
			)
			.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public AuthenticationManager authenticationMenager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
