package com.example.service;

import com.example.entity.Genre;
import com.example.mapper.GenreMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    @Resource
    private GenreMapper genreMapper;

    public void add(Genre genre) {
        genreMapper.insert(genre);
    }

    public void update(Genre genre) {
        genreMapper.updateById(genre);
    }


    public void deleteById(Integer id) {
        genreMapper.deleteById(id);
    }

    //select all
    public List<Genre> selectAll(Genre genre){

        return genreMapper.selectAll(genre);
    }

    /**
     *
     * @param pageNum  current page number
     * @param pageSize  size of each page
     * @return  Paginated object
     */

    public PageInfo<Genre> selectPage(Genre genre, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Genre> list = genreMapper.selectAll(genre);
        return PageInfo.of(list);
    }


}
