package com.example.mapper;

import com.example.entity.Movie;

import java.util.List;

public interface MovieMapper {

    void insert(Movie movie);

    void updateById(Movie movie);

    void deleteById(Integer id);

    List<Movie> selectAll(Movie movie);


}
