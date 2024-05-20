package main.java.com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // This method defines the security filter chain.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configures CSRF protection. Here, it's disabled.
        http.csrf().disable()
            // Configures authorization rules.
            .authorizeRequests()
            // Permits access to "/api/register" and "/api/login" without authentication.
            .mvcMatchers("/api/register", "/api/login").permitAll()
            // Requires authentication for any other request.
            .anyRequest().authenticated()
            // Configures HTTP Basic authentication.
            .and()
            .httpBasic();
        
        // Builds the security filter chain.
        return http.build();
    }

    // This method defines a password encoder bean, which is used for encoding passwords.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
