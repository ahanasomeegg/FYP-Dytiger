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
        reply.setCreateTime(DateUtil.now());
        discussionReplyMapper.insert(reply);
    }


    public void deleteById(Integer id) {
        // Recursive deletion: First delete all sub comments, then delete the current comment
        List<Integer> childIds = discussionReplyMapper.findChildReplies(id);
        for (Integer childId : childIds) {
            deleteById(childId);  // Recursive deletion of sub comments
        }
        discussionReplyMapper.deleteById(id);  // delete the current comment
    }

    public List<DiscussionReply> selectByDiscussionId(Integer discussionId, Integer userId) {
        return discussionReplyMapper.selectByDiscussionId(discussionId, userId);
    }


    public void likeReply(Integer replyId, Integer userId) {
        // First check if the reply_like table already exists (replyId, userId)
        boolean exist = discussionReplyMapper.existsReplyLike(replyId, userId);
        if (!exist) {
            // If it does not exist, insert a record
            discussionReplyMapper.insertReplyLike(replyId, userId);
            // discussion_reply.like_count + 1
            discussionReplyMapper.increaseLikeCount(replyId);
        }
    }

    public void unlikeReply(Integer replyId, Integer userId) {
        // First check if the reply_like table already exists (replyId, userId)
        boolean exist = discussionReplyMapper.existsReplyLike(replyId, userId);
        if (exist) {
            //If you have indeed liked it, then 'cancel liking'
            discussionReplyMapper.deleteReplyLike(replyId, userId);
            discussionReplyMapper.decreaseLikeCount(replyId);
        }
    }

}
