package com.lty.www.security2_form.config;


import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Properties;


//在 Spring Security 6.x（基于 Spring Boot 3.x） 中，@EnableWebSecurity 不再自动包含 @Configuration，所以你必须手动添加 @Configuration 注解。
@EnableWebSecurity
@Configuration
public class SpringConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/api/**").hasRole("ADMIN")
                        .requestMatchers("/user/api/**").hasRole("USER")
                        .requestMatchers("/captcha.jpg").permitAll()
                        .requestMatchers("/app/api/**").permitAll()
                        .requestMatchers("/my_login", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/my_login.html")           // 自定义登录页面
                        .loginProcessingUrl("/auth/form")
                        .permitAll()
                        .failureHandler(new MyAuthenticationFailureHandler())
//                        .defaultSuccessUrl("/", true)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/my_login?logout")
                        .permitAll()
                )
                        .csrf(csrf -> csrf.disable());
        // 禁用 CSRF 防护（开发环境推荐）

        http.addFilterBefore(new VerificationCodeFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/my_login.html", "/css/**", "/js/**").permitAll()
//                        .requestMatchers("/admin/api/**").hasRole("ADMIN")
//                        .requestMatchers("/user/api/**").hasRole("USER")
//                        .requestMatchers("/app/api/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(login -> login
//                        .loginPage("/my_login.html")         // 自定义登录页（static 路径）
//                        .loginProcessingUrl("/login")        // 提交登录的 POST 路径,因为一个url对应一个类，默认处理表单的url就是login，所以这里改完之后就能正确跳转到之前的页面了
//                        .permitAll()
//                )
//                .csrf(csrf -> csrf.disable());           // 禁用 CSRF 防护（开发环境推荐）
//
//        return http.build();
    }


    @Bean
    public Producer captcha() {
        Properties properties = new Properties();

        properties.setProperty("kaptcha.image.width", "150");
        properties.setProperty("kaptcha.image.height", "50");
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        properties.setProperty("kaptcha.textproducer.char.length", "4");

        Config config = new Config(properties);

        DefaultKaptcha kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
