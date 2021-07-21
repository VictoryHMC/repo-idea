package com.lagou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lagou.dao.PromotionAdMapper;
import com.lagou.domain.PromotionAd;
import com.lagou.domain.PromotionAdVO;
import com.lagou.service.PromotionAdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PromotionAdServiceImpl implements PromotionAdService {

    @Autowired
    private PromotionAdMapper promotionAdMapper;

    @Override
    public PageInfo<PromotionAd> findAllPromotionByPage(PromotionAdVO promotionAdVO) {
        //接收的当前页和每页条数进行设置
        //调用startPage，findAllPromotionByPage方法会变为分页查询,查询出的list集合已有分页操作
        PageHelper.startPage(promotionAdVO.getCurrentPage(),promotionAdVO.getPageSize());

        List<PromotionAd> promotionAdList = promotionAdMapper.findAllPromotionByPage();

        //带有分页数据的list集合作为参数调用，返回有相关分页信息的分页对象
        PageInfo<PromotionAd> pageInfo = new PageInfo<>(promotionAdList);

        return pageInfo;
    }

    @Override
    public void savePromotionAd(PromotionAd promotionAd) {
        Date date = new Date();
        promotionAd.setCreateTime(date);
        promotionAd.setUpdateTime(date);
        promotionAdMapper.savePromotionAd(promotionAd);
    }

    @Override
    public void updatePromotionAd(PromotionAd promotionAd) {
        promotionAd.setUpdateTime(new Date());
        promotionAdMapper.updatePromotionAd(promotionAd);
    }

    @Override
    public PromotionAd findPromotionAdById(int id) {
        return promotionAdMapper.findPromotionAdById(id);
    }

    @Override
    public void updatePromotionAdStatus(int id, int status) {
        //封装数据
        PromotionAd promotionAd = new PromotionAd();
        promotionAd.setId(id);
        promotionAd.setStatus(status);
        promotionAd.setUpdateTime(new Date());

        //调用mapper
        promotionAdMapper.updatePromotionAdStatus(promotionAd);
    }
}
