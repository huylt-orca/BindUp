package com.fpt.fptproducthunt.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    @Autowired
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http = http.cors().and().csrf().disable();

        http.authorizeHttpRequests()
                .antMatchers( "/css/**",
                        "/js/**", "/img/**", "/lib/**",
                        "/favicon.ico", "/v3/api-docs/**", "/swagger-ui/**"
                ).permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/users").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/users/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/auth").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/projects").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/projects/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/members").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/members/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/changelogs").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/changelogs/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/topics").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/topics/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/majors").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/majors/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/mentors").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/mentors/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/jobs").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/jobs/**").permitAll()
//                .antMatchers(HttpMethod.POST,"/api/v1/account/**").permitAll()
//                .antMatchers("/api/v1/project/**, /api/v1/mentor/**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/api/v1/projects**, /api/v1/mentors**", "/api/v1/members**",
                        "/api/v1/accounts**", "/api/v1/majors**", "/api/v1/jobs**", "/api/v1/changelogs**",
                        "/api/v1/topics**", "/api/v1/applications**", "/api/v1/notifications**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/auth/**").hasAnyAuthority("USER", "ADMIN")
//                .antMatchers("/api/v1/account/**").hasAuthority("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
