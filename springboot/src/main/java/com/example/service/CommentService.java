package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Comment;
import com.example.mapper.CommentMapper;
import com.example.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * Add comments and distribute points based on type
     * @param comment The comment entity passed in by the front-end (including userId, type, etc.)
     * @return The points earned this time
     */
    @Transactional
    public int add(Comment comment) {
        // 1) record time
        comment.setTime(DateUtil.now());
        commentMapper.insert(comment);

        // 2) Calculate basic integral based on type
        int points;
        if ("short".equalsIgnoreCase(comment.getType())) {
            points = 1;
        } else if ("long".equalsIgnoreCase(comment.getType())) {
            points = 2;
        } else {
            points = 0;
        }

        // 3) If there is a valid userId, perform point update
        if (points > 0 && comment.getUserId() != null) {
            userMapper.updatePoints(comment.getUserId(), points);
        }

        return points;
    }

    public void update(Comment comment) {
        commentMapper.updateById(comment);
    }


    public void deleteById(Integer id) {
        commentMapper.deleteById(id);
    }

    //select all
    public List<Comment> selectAll(Comment comment){

        return commentMapper.selectAll(comment);
    }

    /**
     *
     * @param pageNum  current page number
     * @param pageSize  size of each page
     * @return  Paginated object
     */

    public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentMapper.selectAll(comment);
        return PageInfo.of(list);
    }


}
