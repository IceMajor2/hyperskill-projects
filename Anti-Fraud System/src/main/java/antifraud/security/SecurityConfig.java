package antifraud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    @Autowired
    private AuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    private UserDetailsService userDetailsService;

    private static final String API_URL_PATTERN = "/api/**";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer
                        .authenticationEntryPoint(restAuthenticationEntryPoint))

                .csrf(CsrfConfigurer::disable)

                .headers(httpSecurityHeadersConfigurer ->
                        httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/auth/user/**")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/auth/**")).authenticated()
                        .requestMatchers(mvc.pattern("/actuator/shutdown")).permitAll()
                        .requestMatchers(mvc.pattern("/api/auth/access/**")).authenticated()
                        .requestMatchers(mvc.pattern("/api/auth/role/**")).authenticated()
                        .requestMatchers(mvc.pattern("/api/antifraud/**")).authenticated()
                        .requestMatchers(mvc.pattern("/api/auth/user/**")).authenticated()
                        .anyRequest().permitAll())

                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
