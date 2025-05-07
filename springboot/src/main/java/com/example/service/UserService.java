package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * user business handle
 **/
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * add
     */
    public void add(User user) {
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (ObjectUtil.isNotNull(dbUser)) {
            throw new CustomException("user does not exist!");
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {
            user.setPassword("123");
        }
        if (ObjectUtil.isEmpty(user.getName())) {
            user.setName(user.getUsername());
        }
        user.setRole("USER");
        userMapper.insert(user);
    }

    /**
     * delete
     */
    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    /**
     * update
     */
    public void updateById(User user) {
        userMapper.updateById(user);
    }

    /**
     * selectById
     */
    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    /**
     * selectAll
     */
    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }

    /**
     * pagination
     */
    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    /**
     * login
     */
    public Account login(Account account) {
        Account dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException("user does not exist!");
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException("Account or password error");
        }
        return dbUser;
    }

    /**
     * update password
     */
    public void updatePassword(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException("user does not exist!");
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException("Original password is wrong!");
        }
        dbUser.setPassword(account.getNewPassword());
        userMapper.updateById(dbUser);
    }

    public List<User> getRank() {
        return userMapper.selectTopByPoints();
    }

}