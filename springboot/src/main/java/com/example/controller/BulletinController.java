package com.example.controller;

import com.example.common.Result;
import com.example.entity.Bulletin;
import com.example.service.BulletinService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bulletin")
public class BulletinController {

    @Resource
    private BulletinService bulletinService;

    /**
     *add new data
     */
    @PostMapping("/add")
    public Result add(@RequestBody Bulletin bulletin) {
        bulletinService.add(bulletin);
        return Result.success();
    }

    /**
     *update data
     */
    @PutMapping("/update")
    public Result update(@RequestBody Bulletin bulletin) {
        bulletinService.update(bulletin);
        return Result.success();
    }

    /**
     *delete data
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        bulletinService.deleteById(id);
        return Result.success();
    }

    //query all
    @GetMapping("/selectAll")
    public Result selectAll(Bulletin bulletin){
        List<Bulletin> list = bulletinService.selectAll(bulletin);
        return Result.success(list);
    }

    //pagination
    @GetMapping("/selectPage")
    public Result selectPage(Bulletin bulletin,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Bulletin> pageInfo = bulletinService.selectPage(bulletin, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
