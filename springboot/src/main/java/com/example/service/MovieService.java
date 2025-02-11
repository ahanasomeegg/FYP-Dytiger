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
import java.util.List;

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
            //Check how many reviews there are for the current movie
            int total = commentMapper.selectTotal(m.getId());
            m.setCommentNum(total);
            if(total== 0){
                m.setScore(0D);
            }else{
                //Calculate the average score for the movie
               double sum = commentMapper.selectSum(m.getId());
               BigDecimal score = BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(total), 1, RoundingMode.HALF_UP);
               m.setScore(score.doubleValue());
            }
        }
        return PageInfo.of(list);
    }


}
