package com.example.controller;

import com.example.common.Result;
import com.example.entity.Genre;
import com.example.service.GenreService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genre")
public class GenreController {

    @Resource
    private GenreService genreService;

    /**
     *add new data
     */
    @PostMapping("/add")
    public Result add(@RequestBody Genre genre) {
        genreService.add(genre);
        return Result.success();
    }

    /**
     *update data
     */
    @PutMapping("/update")
    public Result update(@RequestBody Genre genre) {
        genreService.update(genre);
        return Result.success();
    }

    /**
     *delete data
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        genreService.deleteById(id);
        return Result.success();
    }

    //query all
    @GetMapping("/selectAll")
    public Result selectAll(Genre genre){
        List<Genre> list = genreService.selectAll(genre);
        return Result.success(list);
    }

    //pagination
    @GetMapping("/selectPage")
    public Result selectPage(Genre genre,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Genre> pageInfo = genreService.selectPage(genre, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
