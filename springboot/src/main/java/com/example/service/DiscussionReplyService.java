package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.DiscussionReply;
import com.example.mapper.DiscussionReplyMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussionReplyService {

    @Resource
    private DiscussionReplyMapper discussionReplyMapper;

    public void add(DiscussionReply reply) {
        reply.setCreateTime(DateUtil.now());  // 填充时间
        discussionReplyMapper.insert(reply);
    }


    public void deleteById(Integer id) {
        // 递归删除：先删除所有子评论，再删除当前评论
        List<Integer> childIds = discussionReplyMapper.findChildReplies(id);
        for (Integer childId : childIds) {
            deleteById(childId);  // 递归删除子评论
        }
        discussionReplyMapper.deleteById(id);  // 删除当前评论
    }

    public List<DiscussionReply> selectByDiscussionId(Integer discussionId, Integer userId) {
        return discussionReplyMapper.selectByDiscussionId(discussionId, userId);
    }


    public void likeReply(Integer replyId, Integer userId) {
        // 先检查 reply_like 表，是否已存在 (replyId, userId)
        boolean exist = discussionReplyMapper.existsReplyLike(replyId, userId);
        if (!exist) {
            // 如果不存在，插入一条记录
            discussionReplyMapper.insertReplyLike(replyId, userId);
            // 同时将 discussion_reply.like_count + 1
            discussionReplyMapper.increaseLikeCount(replyId);
        }
    }

    public void unlikeReply(Integer replyId, Integer userId) {
        // 先检查 reply_like 表，是否已存在 (replyId, userId)
        boolean exist = discussionReplyMapper.existsReplyLike(replyId, userId);
        if (!exist) {
            // 如果不存在，插入一条记录
            discussionReplyMapper.insertReplyLike(replyId, userId);
            // 同时将 discussion_reply.like_count + 1
            discussionReplyMapper.increaseLikeCount(replyId);
        }
    }
}
