package com.example.controller;

import com.example.common.Result;
import com.example.entity.Movie;
import com.example.service.MovieService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Resource
    private MovieService movieService;

    /**
     *add new data
     */
    @PostMapping("/add")
    public Result add(@RequestBody Movie movie) {
        movieService.add(movie);
        return Result.success();
    }

    /**
     *update data
     */
    @PutMapping("/update")
    public Result update(@RequestBody Movie movie) {
        movieService.update(movie);
        return Result.success();
    }

    /**
     *delete data
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        movieService.deleteById(id);
        return Result.success();
    }

    //query all
    @GetMapping("/selectAll")
    public Result selectAll(Movie movie){
        List<Movie> list = movieService.selectAll(movie);
        return Result.success(list);
    }

    //Recommended movies
    @GetMapping("/selectRecommended/{id}")
    public Result selectRecommended(@PathVariable Integer id) {
        List<Movie> list = movieService.selectRecommended(id);
        return Result.success(list);
    }

    //query one movie by id
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Movie movie = movieService.selectById(id);
        return Result.success(movie);
    }

    //pagination
    @GetMapping("/selectPage")
    public Result selectPage(Movie movie,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Movie> pageInfo = movieService.selectPage(movie, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
