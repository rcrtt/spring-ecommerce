package com.curso.ecommerce.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class SpringBootSecurity {
    
	private UserDetailsService userDetailService;

    @Bean
    BCryptPasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
    	
    }
    
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	
        auth.userDetailsService(userDetailService).passwordEncoder(getEncoder());
        
    }
   
    @SuppressWarnings("deprecation")
	protected void configure(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).authorizeRequests(requests -> requests
                .requestMatchers("/administrador/**").hasRole("ADMIN")
                .requestMatchers("/productos/**").hasRole("ADMIN")).formLogin(login -> login.loginPage("/usuario/login")
                .permitAll().defaultSuccessUrl("/usuario/acceder"));
	}
	

}
