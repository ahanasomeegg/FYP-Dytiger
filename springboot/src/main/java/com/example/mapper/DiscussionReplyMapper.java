package com.example.mapper;

import com.example.entity.DiscussionReply;
import java.util.List;

public interface DiscussionReplyMapper {
    void insert(DiscussionReply reply);

    void deleteById(Integer id);


    List<DiscussionReply> selectByDiscussionId(Integer discussionId);


}

