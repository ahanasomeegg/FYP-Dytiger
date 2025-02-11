package com.example.mapper;

import com.example.entity.Comment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentMapper {

    void insert(Comment comment);

    void updateById(Comment comment);

    void deleteById(Integer id);

    List<Comment> selectAll(Comment comment);

    @Select("select count(*) from comment where movie_id = #{movieId}")
    int selectTotal(Integer movieId);

    @Select("select sum(score) from comment where movie_id = #{movieId}")
    double selectSum(Integer movieId);

}
