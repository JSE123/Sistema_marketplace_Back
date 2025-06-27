package com.marketplace.MarketBack.config;

import com.marketplace.MarketBack.config.filter.JwtValidator;
import com.marketplace.MarketBack.service.UserDetailServiceImp;
import com.marketplace.MarketBack.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    http.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/auth/**").permitAll();
                    http.requestMatchers("/uploads/**").permitAll();

                    //users
                    http.requestMatchers(HttpMethod.POST, "/api/users/**").hasAnyRole("USER", "ADMIN");
                    http.requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("USER", "ADMIN");
                    http.requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("ADMIN", "USER");
                    http.requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.PATCH, "/api/users/**").hasRole("ADMIN");
                    //products
                    http.requestMatchers(HttpMethod.GET, "/api/products/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/products/get-my-products").hasAnyRole("ADMIN", "USER");
                    http.requestMatchers(HttpMethod.POST, "/api/products/**").hasAnyRole("ADMIN", "USER");
                    http.requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyRole("ADMIN", "USER");
                    http.requestMatchers(HttpMethod.PATCH, "/api/products/**").hasAnyRole("ADMIN", "USER");
                    //categories
                    http.requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN");

                    http.requestMatchers(HttpMethod.GET, "/api/tags/**").hasAnyRole("ADMIN", "USER");
                    http.requestMatchers(HttpMethod.POST, "/api/tags/**").hasAnyRole("ADMIN", "USER");
                    http.requestMatchers(HttpMethod.GET, "/api/trade/**").hasAnyRole("ADMIN", "USER");
                    http.requestMatchers(HttpMethod.POST, "/api/trade/**").hasAnyRole("ADMIN", "USER");
                    http.requestMatchers(HttpMethod.PUT, "/api/trade/**").hasAnyRole("ADMIN", "USER");
                    http.requestMatchers(HttpMethod.POST, "/api/trades/{tradeId}/chat/**").hasAnyRole("ADMIN", "USER");
                    http.requestMatchers(HttpMethod.GET, "/api/trades/{tradeId}/chat/**").hasAnyRole("ADMIN", "USER");

                    http.anyRequest().denyAll();//niega el acceso a todos los endpoints que no estan especificados

                })
                .addFilterBefore(new JwtValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceImp userDetailService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // importante si usas JWT con cookies o headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
