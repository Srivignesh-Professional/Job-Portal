package com.jobportal.findworks.security;
import com.jobportal.findworks.security.auth.PhonePasswordAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            PhonePasswordAuthenticationProvider phonePasswordAuthenticationProvider
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(phonePasswordAuthenticationProvider)
                /*.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register").permitAll()
                        .requestMatchers("/worker/**").hasRole("WORKER")
                        .requestMatchers("/employer/**").hasRole("EMPLOYER")
                        .anyRequest().authenticated()
                )*/
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/jobs/**").permitAll()

                        .requestMatchers("/worker/**").hasRole("WORKER")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/jobs/*/apply").hasRole("WORKER")
                        .requestMatchers("/employer/**").hasRole("EMPLOYER")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("phone")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
