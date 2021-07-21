package com.lagou.controller;

import com.lagou.domain.Course;
import com.lagou.domain.CourseLesson;
import com.lagou.domain.CourseSection;
import com.lagou.domain.ResponseResult;
import com.lagou.service.CourseContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController //@controller + @ResponseBody
@RequestMapping("/courseContent")
public class CourserContentController {

    @Autowired
    private CourseContentService courseContentService;


    @RequestMapping("/findSectionAndLesson")
    public ResponseResult findSectionAndLesson(Integer courseId){
        //调用service
        List<CourseSection> sectionList = courseContentService.findSectionAndLessonByCourseId(courseId);

        ResponseResult responseResult = new ResponseResult(true, 200, "章节及课时查询成功", sectionList);
        return responseResult;
    }

    //回显章节对应的课程信息（课程名称）
    @RequestMapping("/findCourseByCourseId")
    public ResponseResult findCourseByCourseId(Integer courseId){
        Course course = courseContentService.findCourseByCourseId(courseId);
        ResponseResult responseResult = new ResponseResult(true, 200, "课程名称查询成功", course);
        return responseResult;
    }

    //新增和更新章节信息
    @RequestMapping("/saveOrUpdateSection")
    public ResponseResult saveOrUpdateSection(@RequestBody CourseSection courseSection){

        //判断是否携带章节ID
        if(courseSection.getId() == null){
            //新增
            courseContentService.saveSection(courseSection);
            return  new ResponseResult(true, 200, "新增章节成功", null);
        }else {
            //更新
            courseContentService.updateSection(courseSection);
            return  new ResponseResult(true, 200, "更新章节成功", null);
        }

    }

    //修改章节状态
    @RequestMapping("/updateSectionStatus")
    public ResponseResult updateSectionStatus(int id,int status){
        courseContentService.updateSectionStatus(id,status);
        Map<Object, Object> map = new HashMap<>();
        map.put("status",status);
        return  new ResponseResult(true, 200, "修改章节状态成功", map);
    }

    //新增课时
    @RequestMapping("/saveOrUpdateLesson")
    public ResponseResult saveLesson(@RequestBody CourseLesson courseLesson){
        if(courseLesson.getId() == null){
            courseContentService.saveLesson(courseLesson);
            return new ResponseResult(true,200,"新增课时成功",null);
        }else{
            courseContentService.updateLesson(courseLesson);
            return new ResponseResult(true,200,"修改课时成功",null);
        }

    }
}
