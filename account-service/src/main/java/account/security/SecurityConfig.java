package account.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(httpBasic -> httpBasic
                        .authenticationEntryPoint(restAuthenticationEntryPoint))
                .exceptionHandling(exceptionHandler -> exceptionHandler.accessDeniedHandler(getAccessDeniedHandler()))
                .csrf((csrf) -> csrf.disable())
                .headers(headers -> headers
                        .frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
                .authorizeHttpRequests((authz) -> authz
                        // controller-specific endpoints
                        .requestMatchers("/api/auth/signup/", "/api/auth/signup").permitAll()
                        .requestMatchers("/api/acct/payments/", "/api/acct/payments").hasAuthority("ROLE_ACCOUNTANT")
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMINISTRATOR")
                        // other endpoints
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(toH2Console()).permitAll()
                        .requestMatchers("/actuator/shutdown").permitAll()
                        .anyRequest().authenticated()
                ).sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(13);
    }

    @Bean
    public AccessDeniedHandler getAccessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }
}
