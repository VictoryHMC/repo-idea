package com.lagou.service;

import com.github.pagehelper.PageInfo;
import com.lagou.domain.PromotionAd;
import com.lagou.domain.PromotionAdVO;


public interface PromotionAdService {

    //分页查询广告信息  接收参数（当前页，每页显示条数）
    //使用vo对象接收参数，对象中有   当前页，每页显示条数 两个属性
    public PageInfo<PromotionAd> findAllPromotionByPage(PromotionAdVO promotionAdVO);

    //保存广告信息
    public void savePromotionAd(PromotionAd promotionAd);

    //更新广告信息
    public void updatePromotionAd(PromotionAd promotionAd);

    //回显广告信息（根据id查询广告信息）
    public PromotionAd findPromotionAdById(int id);

    //广告动态上下线
    public void updatePromotionAdStatus(int id,int status);
}
