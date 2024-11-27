package com.example.mapper;

import com.example.entity.Genre;

import java.util.List;

public interface GenreMapper {

    void insert(Genre genre);

    void updateById(Genre genre);

    void deleteById(Integer id);

    List<Genre> selectAll(Genre genre);


}
