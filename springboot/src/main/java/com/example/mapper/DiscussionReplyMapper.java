package com.example.mapper;

import com.example.entity.DiscussionReply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DiscussionReplyMapper {
    void insert(DiscussionReply reply);

    void deleteById(Integer id);


    List<DiscussionReply> selectByDiscussionId(@Param("discussionId") Integer discussionId, @Param("userId") Integer userId);

    List<Integer> findChildReplies(Integer parentId);

    boolean existsReplyLike(@Param("replyId") Integer replyId, @Param("userId") Integer userId);

    void insertReplyLike(@Param("replyId") Integer replyId, @Param("userId") Integer userId);

    void deleteReplyLike(@Param("replyId") Integer replyId, @Param("userId") Integer userId);

    void increaseLikeCount(Integer replyId);

    void decreaseLikeCount(Integer replyId);


}

