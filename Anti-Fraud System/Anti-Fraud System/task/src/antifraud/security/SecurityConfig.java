package antifraud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.AuthenticationEntryPoint;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    @Autowired
    private AuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                ).csrf((csrf) -> csrf.disable()
                ).headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.
                        frameOptions(frameOptionsConfig -> frameOptionsConfig.disable())
                ).authorizeHttpRequests((authz) -> authz
                // course-specific endpoints handling for tests: START
                        .requestMatchers(HttpMethod.POST, "/api/antifraud/transaction/").denyAll()
                        .requestMatchers(HttpMethod.PUT, "/api/auth/access/").denyAll()
                        .requestMatchers(HttpMethod.PUT, "/api/auth/role/").denyAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/auth/user/").denyAll()
                // END
                        .requestMatchers(HttpMethod.POST, "/api/auth/user/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/auth/**").authenticated()
                        .requestMatchers(toH2Console()).permitAll()
                        .requestMatchers("/actuator/shutdown").permitAll()
                        .requestMatchers("/api/auth/access/**").authenticated()
                        .requestMatchers("/api/auth/role/**").authenticated()
                        .requestMatchers("/api/antifraud/**").authenticated()
                        .requestMatchers("/api/auth/user/**").authenticated()
                        .anyRequest().permitAll()
                ).sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
