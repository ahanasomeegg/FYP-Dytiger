package com.example.controller;

import com.example.common.Result;
import com.example.entity.DiscussionReply;
import com.example.service.DiscussionReplyService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discussionReply")
public class DiscussionReplyController {

    @Resource
    private DiscussionReplyService discussionReplyService;

    // add
    @PostMapping("/add")
    public Result add(@RequestBody DiscussionReply reply) {
        discussionReplyService.add(reply);
        return Result.success();
    }

    // delete
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        discussionReplyService.deleteById(id);
        return Result.success();
    }


    // list all reply
    @GetMapping("/list/{discussionId}")
    public Result list(@PathVariable Integer discussionId) {
        List<DiscussionReply> list = discussionReplyService.selectByDiscussionId(discussionId);
        return Result.success(list);
    }
}
