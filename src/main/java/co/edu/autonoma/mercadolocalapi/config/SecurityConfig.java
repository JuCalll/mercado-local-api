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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService usuarios(PasswordEncoder encoder) {
        UserDetails ana = User.withUsername("ana")
                .password(encoder.encode("ana123"))
                .roles("USUARIO")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(ana, admin);
    }

    @Bean
    public SecurityFilterChain filtros(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/estado").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/ofertas").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/ofertas/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/ofertas/*/movimientos").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/ofertas").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/ofertas/*").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/ofertas/*/estado").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/productores").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/productores/*").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/productores").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/productores/*/estado").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/productores/*/ofertas").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/productores/*/pedidos").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/compradores").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/compradores/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/compradores/*/pedidos").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/pedidos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/*").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/pedidos/*/estado").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/items-pedido/*").hasRole("ADMIN")
                        .anyRequest().denyAll())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
