package com.example.UserManagement.User.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterchain(HttpSecurity http)throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails user = User.builder()
                .username("rajinikanth")
                .password(passwordEncoder().encode("Rajinikanth06"))
                .roles("USER")
                .build();
        UserDetails user2 = User.builder()
                .username("yashwanth")
                .password(passwordEncoder().encode("yashwanth00"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("sathish")
                .password(passwordEncoder().encode("Sathish01"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user,user2,admin);
    }
}
  