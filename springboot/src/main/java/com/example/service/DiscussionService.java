package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Discussion;
import com.example.mapper.DiscussionMapper;
import com.example.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DiscussionService {

    @Resource
    private DiscussionMapper discussionMapper;

    @Resource
    private UserMapper userMapper;

    @Transactional
    public int add(Discussion discussion) {
        // set time
        discussion.setTime(DateUtil.now());
        discussionMapper.insert(discussion);
        // Calculate points: ordinary discussion+2, hot discussion+5 extra
        int basePoints = 2;
        int bonus = (Boolean.TRUE.equals(discussion.getIsHot())) ? 5 : 0;
        int totalPoints = basePoints + bonus;

        //  Update user points
        Integer userId = discussion.getUserId();
        if (userId != null) {
            userMapper.updatePoints(userId, totalPoints);
        }

        return totalPoints;
    }


    public void update(Discussion discussion) {
        discussionMapper.updateById(discussion);
    }

    public void deleteById(Integer id) {
        discussionMapper.deleteById(id);
    }

    public Discussion getById(Integer id) {
        return discussionMapper.selectById(id);
    }


    public List<Discussion> selectAll(Discussion discussion) {
        return discussionMapper.selectAll(discussion);
    }

    // pagination
    public PageInfo<Discussion> selectPage(Discussion discussion, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Discussion> list = discussionMapper.selectAll(discussion);
        return PageInfo.of(list);
    }

    // return hot discussion
    public List<Discussion> selectHot() {
        return discussionMapper.selectHot(); // is_hot=1
    }

}

