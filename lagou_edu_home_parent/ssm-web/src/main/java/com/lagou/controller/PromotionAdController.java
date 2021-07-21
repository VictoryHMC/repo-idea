package com.lagou.controller;

import com.github.pagehelper.PageInfo;
import com.lagou.domain.PromotionAd;
import com.lagou.domain.PromotionAdVO;
import com.lagou.domain.ResponseResult;
import com.lagou.service.PromotionAdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@RestController //@Controller + @ResponseBody
@RequestMapping("/PromotionAd")
public class PromotionAdController {

    @Autowired
    private PromotionAdService promotionAdService;

    //广告分页查询
    @RequestMapping("/findAllPromotionAdByPage")
    //前台是通过get方式传递两个参数，没有请求体，所以不需要使用@RequestBody进行json数据转换
    public ResponseResult findAllPromotionAdByPage(PromotionAdVO promotionAdVO){
        PageInfo<PromotionAd> pageInfo = promotionAdService.findAllPromotionByPage(promotionAdVO);
        return new ResponseResult(true,200,"广告分页查询成功",pageInfo);
    }

    //广告图片上传
    @RequestMapping("/PromotionAdUpload")
    public ResponseResult findUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {

        //1.判断接收到的上传文件是否为空
        if(file.isEmpty()){
            throw new RuntimeException();
        }

        //2.获取项目部署路径
        // D:\apache-tomcat-8.5.56\webapps\ssm-web\
        String realPath = request.getServletContext().getRealPath("/");
        // D:\apache-tomcat-8.5.56\webapps\
        String tomcatLocation = realPath.substring(0, realPath.indexOf("ssm-web"));

        //3.获取原文件名
        //xx.jpg
        String oldFileName = file.getOriginalFilename();

        //4.生成新文件名
        //123456.jpg                时间戳              +                       .jpg
        String newFileName = System.currentTimeMillis() + oldFileName.substring(oldFileName.lastIndexOf("."));

        //5.文件上传
        //  D:/apache-tomcat-8.5.56/webapps/upload
        String uploadPath = tomcatLocation + "upload\\";
        File filePath = new File(uploadPath, newFileName);

        //如果目录不存在就创建目录
        if(filePath.getParentFile().exists()){
            filePath.getParentFile().mkdirs();
            System.out.println("创建目录："+filePath);
        }

        //图片进行了真正的上传    （文件存放位置）
        file.transferTo(filePath);

        //6.将文件名和文件路径返回，进行响应
        HashMap<String, Object> map = new HashMap<>();
        map.put("fileName",newFileName);
        //http://localhost:8080/upload/1597112871741.JPG
        map.put("filePath","http://localhost:8080/upload/"+newFileName);

        ResponseResult responseResult = new ResponseResult(true, 200, "图片上传成功", map);

        return responseResult;
    }

    //新增和修改广告信息
    @RequestMapping("/saveOrUpdatePromotionAd")
    public ResponseResult saveOrUpdatePromotionAd(@RequestBody PromotionAd promotionAd){
        if(promotionAd.getId() == null){
            //新增
            promotionAdService.savePromotionAd(promotionAd);
            return new ResponseResult(true,200,"新增广告信息成功",null);
        }else {
            //修改
            promotionAdService.updatePromotionAd(promotionAd);
            return new ResponseResult(true,200,"更新广告信息成功",null);
        }
    }

    @RequestMapping("/findPromotionAdById")
    public ResponseResult findPromotionAdById(int id){
        PromotionAd promotionAd = promotionAdService.findPromotionAdById(id);
        return new ResponseResult(true,200,"回显广告信息成功",promotionAd);
    }

    //广告动态上下线
    @RequestMapping("/updatePromotionAdStatus")
    public ResponseResult updatePromotionAdStatus(Integer id,Integer status){
        promotionAdService.updatePromotionAdStatus(id,status);

        return new ResponseResult(true, 200, "广告动态上下线成功", null);
    }
}
