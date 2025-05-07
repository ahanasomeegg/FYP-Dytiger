package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


public interface UserMapper {

    /**
      * add
    */
    int insert(User user);

    /**
      * delete
    */
    @Delete("delete from user where id = #{id}")
    int deleteById(Integer id);

    /**
      * update
    */
    int updateById(User user);

    /**
      * search by id
    */
    @Select("select * from user where id = #{id}")
    User selectById(Integer id);

    /**
      * select all
    */
    List<User> selectAll(User user);

    @Select("select * from user where username = #{username}")
    User selectByUsername(String username);

    void updatePoints(Integer userId, Integer points);


    List<User> selectTopByPoints();
}




