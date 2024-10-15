package com.lyc.teamnav.common.config;


import com.lyc.teamnav.service.impl.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${login.enable}")
    private boolean loginEnable;

    @Value("${spring.security.permit-url:}")
    private String[] permitUrl;

    @Resource
    private UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Disable frame options and CSRF protection
        http.headers(headers ->
                headers.frameOptions(frameOptions -> frameOptions.disable())
        );
        http.csrf(csrf -> csrf.disable());
        if (!loginEnable) {
            return http.authorizeRequests().anyRequest().permitAll().and().build();
        }
        http.userDetailsService(userService);
        // 使用 Customizer 方法配置表单登录
        http.formLogin(formLogin ->
                formLogin.loginPage("/login")
                        .loginProcessingUrl("/api/v1/login")
        );
        // 使用 Customizer 方法配置登出逻辑
        http.logout(logout ->
                logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/admin/category")
                        .deleteCookies("JSESSIONID")
        );
        // 允许特定的 URL，其他 URL 需要身份验证
        http.authorizeHttpRequests(authorizeHttpRequest -> authorizeHttpRequest.requestMatchers(permitUrl).permitAll()
                .requestMatchers("/admin/**", "/api/v1/**").authenticated()
                .anyRequest().permitAll());

        // 处理 AJAX 请求的未授权请求
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling.defaultAuthenticationEntryPointFor(
                        (request, response, ex) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED),
                        request -> "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
                )
        );
        return http.build();
    }



    @Bean
    public PasswordEncoder cryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
