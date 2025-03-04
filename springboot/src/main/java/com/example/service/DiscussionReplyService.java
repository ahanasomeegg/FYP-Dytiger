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
        discussionReplyMapper.deleteById(id);
    }

    public List<DiscussionReply> selectByDiscussionId(Integer discussionId) {
        return discussionReplyMapper.selectByDiscussionId(discussionId);
    }
}
