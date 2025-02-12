package com.example.service;

import com.example.entity.Movie;
import com.example.mapper.CommentMapper;
import com.example.mapper.MovieMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Resource
    private MovieMapper movieMapper;
    @Resource
    private CommentMapper commentMapper;



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
        for(Movie m : list){
           this.setScore(m);
        }
        return PageInfo.of(list);
    }


    public Movie selectById(Integer id) {
        Movie movie = movieMapper.selectById(id);
        this.setScore(movie);
        return movie;
    }

    //random recommendation
    public List<Movie> selectRecommended(Integer movieId) {
        List<Movie> movies = this.selectAll(null);
        //Exclude the movie from the current detail page
        movies = movies.stream().filter(movie -> !movie.getId().equals(movieId)).collect(Collectors.toList());
        Collections.shuffle(movies); //random recommendation
        List<Movie> movieList = movies.stream().limit(3).collect(Collectors.toList());
        for (Movie movie : movieList){
            this.setScore(movie);
        }
        return movieList;
    }

    private void setScore(Movie movie){
        //Check how many reviews there are for the current movie
        int total = commentMapper.selectTotal(movie.getId());
        movie.setCommentNum(total);
        if(total== 0){
            movie.setScore(0D);
        }else{
            //Calculate the average score for the movie
            double sum = commentMapper.selectSum(movie.getId());
            BigDecimal score = BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(total), 1, RoundingMode.HALF_UP);
            movie.setScore(score.doubleValue());
        }
    }
}


