package com.lagou.dao;

import com.lagou.domain.PromotionAd;

import java.util.List;

//广告实体类
public interface PromotionAdMapper {

    //分页查询广告信息
    public List<PromotionAd> findAllPromotionByPage();

    //保存广告信息
    public void savePromotionAd(PromotionAd promotionAd);

    //更新广告信息
    public void updatePromotionAd(PromotionAd promotionAd);

    //回显广告信息（根据id查询广告信息）
    public PromotionAd findPromotionAdById(int id);

    //广告动态上下线
    public void updatePromotionAdStatus(PromotionAd promotionAd);
}
