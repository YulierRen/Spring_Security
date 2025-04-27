package com.lty.www.security2_form.mapper;

import com.lty.www.security2_form.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface UserMapper {
    @Select("SELECT * FROM springDemo.my_users WHERE username = #{username}")
    User findByUsername(@Param("username") String username);
}
