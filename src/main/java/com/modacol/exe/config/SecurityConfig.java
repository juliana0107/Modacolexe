package com.modacol.exe.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService customUserDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
            .requestMatchers("/login").permitAll()
            .requestMatchers("/usuarios/**", "/roles/**").hasRole("ADMIN")
            .requestMatchers("/compras/**", "/proveedores/**", "/productos/**").hasRole("ADMIN")
            .requestMatchers("/flujo-caja/**").hasAnyRole("ADMIN", "FLUJO_DE_CAJA")
            .requestMatchers("/categorias/**").hasAnyRole("ADMIN", "OPERATIVO", "FLUJO_DE_CAJA")
            .requestMatchers("/ventas/**", "/clientes/**").hasAnyRole("ADMIN", "OPERATIVO")
            .requestMatchers("/perfil/**", "/dashboard/**", "/reportes-view/**", "/mensajes/**").authenticated()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/dashboard", true)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .permitAll()
        );
    return http.build();
}
}