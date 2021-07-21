package com.lagou.controller;

import com.lagou.domain.Course;
import com.lagou.domain.CourseVO;
import com.lagou.domain.ResponseResult;
import com.lagou.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

@RestController //@Controller + @ResponseBody
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @RequestMapping("/findAllCourse")
    public ResponseResult findCourseByCondition(@RequestBody CourseVO courseVO){

        List<Course> courseList = courseService.findCourseByCondition(courseVO);
        ResponseResult responseResult = new ResponseResult(true, 200, "响应成功", courseList);
        return responseResult;
    }

    //课程图片上传
    @RequestMapping("/courseUpload")
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

    /*
        新增课程信息及讲师信息
        新增和修改课程信息要写在同一个方法中
     */
    @RequestMapping("/saveOrUpdateCourse")
    public ResponseResult saveOrUpdateCourse(@RequestBody CourseVO courseVO) throws InvocationTargetException, IllegalAccessException {

        //判断课程表的id是否为空，决定新增还是更新
        if(courseVO.getId() == null){
            //调用service 新增保存
            courseService.saveCourseOrTeacher(courseVO);
            ResponseResult responseResult = new ResponseResult(true, 200, "新增成功", null);
            return responseResult;
        }else {
            //不为空，更新
            courseService.updateCourseOrTeacher(courseVO);
            ResponseResult responseResult = new ResponseResult(true, 200, "修改成功", null);
            return responseResult;
        }
    }

    //根据id查询课程信息及关联的讲师信息（回显课程信息）
    @RequestMapping("/findCourseById")
    public ResponseResult findCourseById(Integer id){
        CourseVO courseVO = courseService.findCourseById(id);

        ResponseResult responseResult = new ResponseResult(true, 200, "根据ID查询课程信息成功", courseVO);
        return responseResult;
    }

    //课程状态管理
    @RequestMapping("/updateCourseStatus")  //参数名需与请求参数相同，具体查看接口文档
    public ResponseResult updateCourseStatus(Integer id,Integer status){
        //调用service，传递参数，完成课程状态变更
        courseService.updateCourseStatus(id,status);

        //响应数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("status",status);
        ResponseResult responseResult = new ResponseResult(true, 200, "响应成功", map);
        return responseResult;
    }

}
