package lk.ijse.buildflow.config;

import lk.ijse.buildflow.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. CORS සහ CSRF Configuration
                .cors(Customizer.withDefaults()) // පහත ඇති corsConfigurationSource Bean එක මෙයින් ක්‍රියාත්මක වේ
                .csrf(AbstractHttpConfigurer::disable) // API සඳහා CSRF අවශ්‍ය නොවේ

                // 2. Route Authorization (අවසර ලබා දීම්)
                .authorizeHttpRequests(auth -> auth
                        // Pre-flight (OPTIONS) requests සඳහා සෑම විටම අවසර දීම (CORS error එක විසඳීමට)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Public Endpoints (Token එකක් නොමැතිව පිවිසිය හැකි ඒවා)
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/models/**").permitAll()
                        .requestMatchers("/api/v1/requests/**").permitAll()
                        .requestMatchers("/api/v1/orders/**").permitAll()
                        .requestMatchers("/api/v1/inquiries/**").permitAll()

                        // වෙනත් ඕනෑම Request එකක් සඳහා Token එක (Authentication) අනිවාර්ය වේ
                        .anyRequest().authenticated()
                )

                // 3. Stateless Session Management (JWT භාවිතා කරන බැවින්)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Custom JWT Filter එක සාමාන්‍ය Filter එකට පෙර යෙදීම
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CORS සැකසුම් (Frontend එකෙන් එන Requests Block නොකිරීමට)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Frontend URLs (Live Server පාවිච්චි කරන Ports)
        configuration.setAllowedOrigins(List.of(
                "http://127.0.0.1:5500",
                "http://localhost:5500"
        ));

        // අවසර දෙන HTTP Methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // අවසර දෙන Headers (Authorization අනිවාර්යයි Token යැවීමට)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));

        // Credentials (Cookies/Auth headers) සඳහා අවසර දීම
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // සියලුම API Paths සඳහා මෙය අදාළ වේ

        return source;
    }

    // Password Encrypt කිරීම සඳහා Bean එක
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication ක්‍රියාවලිය හැසිරවීමට AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}