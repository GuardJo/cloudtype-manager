package org.github.guardjo.cloudtype.manager.config;

import lombok.RequiredArgsConstructor;
import org.github.guardjo.cloudtype.manager.config.auth.GoogleOAuth2UserService;
import org.github.guardjo.cloudtype.manager.config.auth.JwtAuthenticationFilter;
import org.github.guardjo.cloudtype.manager.config.auth.OAuth2AuthenticationSuccessHandler;
import org.github.guardjo.cloudtype.manager.config.properties.CorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsProperties corsProperties;
    private final GoogleOAuth2UserService googleOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(registry -> {
                    registry
                            .requestMatchers("/api/v1/auth/refresh").permitAll()
                            .requestMatchers("/api/**").authenticated()
                            .anyRequest().permitAll();
                })
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(registry -> registry.configurationSource(corsConfigurationSource()))
                .exceptionHandling(configurer -> configurer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2Login(configurer -> {
                    configurer.userInfoEndpoint(customizer -> customizer.userService(googleOAuth2UserService));
                    configurer.successHandler(oAuth2AuthenticationSuccessHandler);
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(corsProperties.allowCredentials());
        configuration.setAllowedOrigins(corsProperties.allowOrigins());
        configuration.setAllowedMethods(corsProperties.allowMethods());
        configuration.setAllowedHeaders(corsProperties.allowHeaders());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
