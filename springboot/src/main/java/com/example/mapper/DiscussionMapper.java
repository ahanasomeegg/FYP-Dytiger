package com.example.mapper;

import com.example.entity.Discussion;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DiscussionMapper {

    void insert(Discussion discussion);

    void updateById(Discussion discussion);

    void deleteById(Integer id);

    List<Discussion> selectAll(Discussion discussion);

    Discussion selectById(Integer id);

    @Select("select count(*) from discussion where movie_id = #{movieId}")
    int selectTotal(Integer movieId);

}