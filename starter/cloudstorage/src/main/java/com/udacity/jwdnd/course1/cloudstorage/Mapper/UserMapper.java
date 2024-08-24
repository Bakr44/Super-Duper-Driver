package com.udacity.jwdnd.course1.cloudstorage.Mapper;

import com.udacity.jwdnd.course1.cloudstorage.Model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE username = #{userName}")
    User getUserByUserName(String userName);

@Insert("INSERT INTO users (username, salt,password, firstName, lastName) VALUES (#{userName},#{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true ,keyProperty = "userId")
    int insertUser(User user);



}
