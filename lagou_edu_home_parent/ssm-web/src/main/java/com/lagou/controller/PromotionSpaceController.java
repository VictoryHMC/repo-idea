package com.lagou.controller;

import com.lagou.domain.PromotionSpace;
import com.lagou.domain.ResponseResult;
import com.lagou.service.PromotionSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController //@Controller + @ResponseBody
@RequestMapping("/PromotionSpace")
public class PromotionSpaceController {

    @Autowired
    private PromotionSpaceService promotionSpaceService;

    //查询所有广告位
    @RequestMapping("/findAllPromotionSpace")
    public ResponseResult findAllPromotionSpace(){
        List<PromotionSpace> spaceList = promotionSpaceService.findAllPromotionSpace();
        return new ResponseResult(true,200,"查询所有广告位成功",spaceList);
    }

    //新增和修改广告位
    @RequestMapping("/saveOrUpdatePromotionSpace")
    public ResponseResult saveOrUpdatePromotionSpace(@RequestBody PromotionSpace promotionSpace){
        if(promotionSpace.getId() == null){
            //新增
            promotionSpaceService.savePromotionSpace(promotionSpace);
            return new ResponseResult(true,200,"新增广告位成功",null);
        }else {
            //修改
            promotionSpaceService.updatePromotionSpace(promotionSpace);
            return new ResponseResult(true,200,"修改广告位成功",null);
        }
    }

    //回显广告位信息（根据id查询广告位信息）
    @RequestMapping("/findPromotionSpaceById")
    public ResponseResult findPromotionSpaceById(Integer id){
        PromotionSpace promotionSpace = promotionSpaceService.findPromotionSpaceById(id);

        return new ResponseResult(true,200,"回显广告位成功",promotionSpace);
    }



}
