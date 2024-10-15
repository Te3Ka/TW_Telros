package ru.te3ka.telrostestwork.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Класс для настройки конфигурации доступа к базе данных.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Метод производит конфигурацию фильтров безопасности.
     *
     * @param http - объект типа HttpSecurity, используется для конфигурации.
     * @return настроенная цепочка фильтров.
     * @throws Exception непредвиденная ошибка.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/**").authenticated()
                )
                .httpBasic()
                .and()
                .csrf().disable();
        return http.build();
    }

    /**
     * Глобальная конфигурация доступа к базе данных "из памяти".
     *
     * @param auth - объект для настройки прав доступа к базе данных.
     * @throws Exception непредвиденная ошибка.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("{noop}admin").roles("USER"); // Создаем пользователя admin
    }
}
