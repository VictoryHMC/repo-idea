package com.lagou.dao;

import com.lagou.domain.Course;
import com.lagou.domain.CourseLesson;
import com.lagou.domain.CourseSection;

import java.util.List;

public interface CourseContentMapper {
    //根据课程id查询关联的章节信息及章节信息关联的课时信息
    public List<CourseSection> findSectionAndLessonByCourseId(Integer courseId);

    //回显章节对应的课程信息（显示课程名称）
    public Course findCourseByCourseId(Integer courseId);

    //新增章节信息
    public void saveSection(CourseSection courseSection);

    //更新章节信息
    void updateSection(CourseSection courseSection);

    //修改章节状态
    public void updateSectionStatus(CourseSection courseSection);

    //新增课时
    public void saveLesson(CourseLesson lesson);

    //更新课时信息
    public void updateLesson(CourseLesson lesson);
}
