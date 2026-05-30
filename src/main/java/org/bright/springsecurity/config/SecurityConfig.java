package org.bright.springsecurity.config;

import org.bright.springsecurity.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }
    //    @Bean
//    public SecurityFilterChain basicAuth(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth ->
//                        auth.anyRequest().authenticated())
//                .httpBasic(Customizer.withDefaults());
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain basicAuth(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/hello").permitAll()
                                .requestMatchers("/user/**").permitAll()
//                                .requestMatchers(HttpMethod.POST, "/rooms").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.GET, "/rooms/").hasAnyRole("ADMIN", "STAFF")
//                                .requestMatchers(HttpMethod.GET, "/rooms/**").hasAnyRole("ADMIN", "STAFF", "GUEST")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService(UserRepository userRepository){
//        return new UserService(userRepository);
//    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder){

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
