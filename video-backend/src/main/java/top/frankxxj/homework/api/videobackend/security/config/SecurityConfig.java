package top.frankxxj.homework.api.videobackend.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import top.frankxxj.homework.api.videobackend.security.user.UserRepository;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsManager(userRepository);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/", "/register", "/user/check").permitAll();
            auth.requestMatchers("/public/**").permitAll();
            auth.requestMatchers("/admin/**").hasRole("ADMIN");
            auth.requestMatchers(HttpMethod.POST, "/user/**").permitAll();
            auth.requestMatchers("/user/**").hasRole("USER");
            auth.anyRequest().authenticated();

        });
        http.httpBasic(withDefaults());
        http.formLogin(withDefaults());
        http.csrf(cus -> {
            cus.disable();
        });
        http.cors(config -> {
            config.configurationSource(request -> {
                var cors = new CorsConfiguration();
                cors.setAllowedOrigins(List.of("http://localhost:5678"));
                cors.setAllowCredentials(true);
                cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                cors.setAllowedHeaders(List.of("*"));
                return cors;
            });
        });
        return http.build();
    }

}
