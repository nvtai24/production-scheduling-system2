package org.nvtai.ProductionSchedulingSystem.config;

import org.nvtai.ProductionSchedulingSystem.service.FeatureService;
import org.nvtai.ProductionSchedulingSystem.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return authProvider;
    }

    @Autowired
    private FeatureService featureService;

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> decisionVoters = List.of(new UrlMethodVoter());
        return new AffirmativeBased(decisionVoters);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/error", "/logout")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionFixation().migrateSession())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(filterSecurityInterceptor(), FilterSecurityInterceptor.class);

        return http.build();
    }

    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() {
        FilterSecurityInterceptor interceptor = new FilterSecurityInterceptor();
        interceptor.setAccessDecisionManager(accessDecisionManager());

        // Sử dụng FeatureService để tải requestMap từ database
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap =
                featureService.loadRequestMapFromDatabase();
        DefaultFilterInvocationSecurityMetadataSource metadataSource =
                new DefaultFilterInvocationSecurityMetadataSource(requestMap);
        interceptor.setSecurityMetadataSource(metadataSource);

        return interceptor;
    }
}