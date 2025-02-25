package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Discussion;
import com.example.mapper.DiscussionMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussionService {

    @Resource
    private DiscussionMapper discussionMapper;

    public void add(Discussion discussion) {
        // set time
        discussion.setTime(DateUtil.now());
        discussionMapper.insert(discussion);
    }

    public void update(Discussion discussion) {
        discussionMapper.updateById(discussion);
    }

    public void deleteById(Integer id) {
        discussionMapper.deleteById(id);
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
}

