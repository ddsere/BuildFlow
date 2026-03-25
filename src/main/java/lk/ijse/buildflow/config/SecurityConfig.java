package lk.ijse.buildflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Testing වලදී මේක disable කරන්න ඕනේ
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/user/**").permitAll() // User register වෙන්න ඉඩ දෙන්න
                        .requestMatchers("/api/v1/models/**").permitAll() // Models බලන්න ඉඩ දෙන්න
                        .anyRequest().authenticated() // අනෙක් ඒවාට security ඕනේ
                );
        return http.build();
    }
}
