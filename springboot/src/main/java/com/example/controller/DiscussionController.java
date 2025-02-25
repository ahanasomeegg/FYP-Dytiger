package com.example.controller;

import com.example.common.Result;
import com.example.entity.Discussion;
import com.example.service.DiscussionService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discussion")
public class DiscussionController {

    @Resource
    private DiscussionService discussionService;

    // add
    @PostMapping("/add")
    public Result add(@RequestBody Discussion discussion) {
        discussionService.add(discussion);
        return Result.success();
    }

    // update
    @PutMapping("/update")
    public Result update(@RequestBody Discussion discussion) {
        discussionService.update(discussion);
        return Result.success();
    }

    // delete
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        discussionService.deleteById(id);
        return Result.success();
    }

    // select all
    @GetMapping("/selectAll")
    public Result selectAll(Discussion discussion) {
        List<Discussion> list = discussionService.selectAll(discussion);
        return Result.success(list);
    }

    // pagination
    @GetMapping("/selectPage")
    public Result selectPage(Discussion discussion,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Discussion> pageInfo = discussionService.selectPage(discussion, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
