package com.ecm.configs;

import com.ecm.filter.JwtTokenFilter;
import com.ecm.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Value("${api.prefix}")
    private String apiPrefix;

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            // users
                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix))
                            .permitAll()

                            // roles
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/roles", apiPrefix))
                            .hasAnyRole(Role.ADMIN)

                            // categories
                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/categories/**", apiPrefix))
                            .hasAnyRole(Role.ADMIN)

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/categories**", apiPrefix))
                            .permitAll()

                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/categories/**", apiPrefix))
                            .hasAnyRole(Role.ADMIN)

                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/categories/**", apiPrefix))
                            .hasAnyRole(Role.ADMIN)

                            // products
                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/products/**", apiPrefix)
                            ).hasAnyRole(Role.ADMIN)

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/products**", apiPrefix)
                            )
                            .permitAll()

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/products/**", apiPrefix)
                            )
                            .permitAll()

                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/products/**", apiPrefix)
                            ).hasAnyRole(Role.ADMIN)

                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/products/**", apiPrefix)
                            ).hasAnyRole(Role.ADMIN)

                            //order_details
                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/order_details/**", apiPrefix)
                            ).hasAnyRole(Role.USER)

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/order_details/**", apiPrefix)
                            ).hasAnyRole(Role.USER, Role.ADMIN)

                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/order_details/**", apiPrefix)
                            ).hasAnyRole(Role.ADMIN)

                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/order_details/**", apiPrefix)
                            ).hasAnyRole(Role.ADMIN)

                            // orders
                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/orders/**", apiPrefix)
                            ).hasAnyRole(Role.USER)

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/orders/**", apiPrefix)
                            ).hasAnyRole(Role.USER, Role.ADMIN)

                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/orders/**", apiPrefix)
                            ).hasAnyRole(Role.ADMIN)

                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/orders/**", apiPrefix)
                            ).hasAnyRole(Role.ADMIN)

                            .anyRequest().authenticated();
                });
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return http.build();
    }
}
