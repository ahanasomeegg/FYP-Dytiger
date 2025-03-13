package com.example.controller;

import com.example.common.Result;
import com.example.entity.DiscussionReply;
import com.example.entity.LikeRequest;
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
    public Result list(@PathVariable Integer discussionId, @RequestParam Integer userId) {
        List<DiscussionReply> list = discussionReplyService.selectByDiscussionId(discussionId, userId);
        return Result.success(list);
    }

    // like the reply
    @PostMapping("/like")
    public Result likeReply(@RequestBody LikeRequest request) {
        Integer replyId = request.getReplyId();
        Integer userId = request.getUserId();
        discussionReplyService.likeReply(replyId, userId);
        return Result.success();
    }

    // cancel like the reply
    @PostMapping("/unlike")
    public Result unlikeReply(@RequestBody LikeRequest request) {
        Integer replyId = request.getReplyId();
        Integer userId = request.getUserId();
        discussionReplyService.likeReply(replyId, userId);
        return Result.success();
    }

}
