package co.edu.autonoma.mercadolocalapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Paso 2: las contraseñas se guardan como hash BCrypt, nunca en texto plano
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService usuarios(PasswordEncoder encoder) {
        // Ana Gómez, compradora: publica, pide y consulta
        UserDetails ana = User.withUsername("ana")
                .password(encoder.encode("ana123"))
                .roles("USUARIO")
                .build();
        // Administrador del mercado: retira ítems y activa o desactiva productores
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(ana, admin);
    }

    // Paso 3: quién puede hacer qué. Se leen de arriba hacia abajo y gana la primera que coincide
    @Bean
    public SecurityFilterChain filtros(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/estado", "/api/ofertas/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/items-pedido/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/productores/*/estado").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
