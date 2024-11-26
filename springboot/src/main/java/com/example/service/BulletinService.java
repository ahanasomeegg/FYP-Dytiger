package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import com.example.entity.Bulletin;
import com.example.mapper.BulletinMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BulletinService {

    @Resource
    private BulletinMapper bulletinMapper;

    public void add(Bulletin bulletin) {
        bulletin.setTime(DateUtil.now()); //get current time when adding
        bulletinMapper.insert(bulletin);
    }

    public void update(Bulletin bulletin) {
        bulletinMapper.updateById(bulletin);
    }


    public void deleteById(Integer id) {
        bulletinMapper.deleteById(id);
    }

    //select all
    public List<Bulletin> selectAll(Bulletin bulletin){

        return bulletinMapper.selectAll(bulletin);
    }

    /**
     *
     * @param pageNum  current page number
     * @param pageSize  size of each page
     * @return  Paginated object
     */

    public PageInfo<Bulletin> selectPage(Bulletin bulletin, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Bulletin> list = bulletinMapper.selectAll(bulletin);
        return PageInfo.of(list);
    }


}
