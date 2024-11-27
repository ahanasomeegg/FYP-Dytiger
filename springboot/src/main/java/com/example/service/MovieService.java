package com.example.service;

import com.example.entity.Movie;
import com.example.mapper.MovieMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Resource
    private MovieMapper movieMapper;

    public void add(Movie movie) {
        movieMapper.insert(movie);
    }

    public void update(Movie movie) {
        movieMapper.updateById(movie);
    }


    public void deleteById(Integer id) {
        movieMapper.deleteById(id);
    }

    //select all
    public List<Movie> selectAll(Movie movie){

        return movieMapper.selectAll(movie);
    }

    /**
     *
     * @param pageNum  current page number
     * @param pageSize  size of each page
     * @return  Paginated object
     */

    public PageInfo<Movie> selectPage(Movie movie, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Movie> list = movieMapper.selectAll(movie);
        return PageInfo.of(list);
    }


}
