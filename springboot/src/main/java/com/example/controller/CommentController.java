package com.example.controller;

import com.example.common.Result;
import com.example.entity.Comment;
import com.example.service.CommentService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    /**
     *add new data
     */
    @PostMapping("/add")
    public Result add(@RequestBody Comment comment) {
        int pointsAdded = commentService.add(comment);
        Map<String, Object> data = new HashMap<>();
        data.put("pointsAdded", pointsAdded);
        return Result.success(data);
    }

    /**
     *update data
     */
    @PutMapping("/update")
    public Result update(@RequestBody Comment comment) {
        commentService.update(comment);
        return Result.success();
    }

    /**
     *delete data
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        commentService.deleteById(id);
        return Result.success();
    }

    //query all
    @GetMapping("/selectAll")
    public Result selectAll(Comment comment){
        List<Comment> list = commentService.selectAll(comment);
        return Result.success(list);
    }

    //pagination
    @GetMapping("/selectPage")
    public Result selectPage(Comment comment,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Comment> pageInfo = commentService.selectPage(comment, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
