package com.lty.www.security2_form.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

//@Configuration
public class UserDetailService {
//    @Autowired
    DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource, PasswordEncoder passwordEncoder) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
        userDetailsManager.setDataSource(dataSource);

        // 先检查user是否存在
        if (!userDetailsManager.userExists("user")) {
            userDetailsManager.createUser(
                    User.withUsername("user")
                            .password(passwordEncoder.encode("123"))  // 密码需要加密存储
                            .roles("USER")
                            .build()
            );
        }

        // 先检查admin是否存在
        if (!userDetailsManager.userExists("admin")) {
            userDetailsManager.createUser(
                    User.withUsername("admin")
                            .password(passwordEncoder.encode("123"))  // 密码需要加密存储
                            .roles("USER", "ADMIN")
                            .build()
            );
        }

        return userDetailsManager;
    }
}
