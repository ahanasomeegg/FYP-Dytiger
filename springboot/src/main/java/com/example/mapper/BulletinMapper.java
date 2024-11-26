package com.example.mapper;

import com.example.entity.Bulletin;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BulletinMapper {

    void insert(Bulletin bulletin);

    void updateById(Bulletin bulletin);

    void deleteById(Integer id);

    List<Bulletin> selectAll(Bulletin bulletin);


}
