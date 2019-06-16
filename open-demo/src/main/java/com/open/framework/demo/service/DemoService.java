package com.open.framework.demo.service;

import com.open.framework.commmon.BaseConstant;
import com.open.framework.commmon.enums.EnumBase;
import com.open.framework.commmon.exceptions.BusinessException;
import com.open.framework.commmon.exceptions.PlatformException;
import com.open.framework.core.event.OpenEvent;
import com.open.framework.core.event.OpenEventListener;
import com.open.framework.core.startrun.StartRun;
import com.open.framework.dao.dynamic.ChangeDs;
import com.open.framework.dao.other.PageUtil;
import com.open.framework.dao.other.SortUtil;
import com.open.framework.dao.specification.SimpleSpecBuilder;
import com.open.framework.demo.exception.BusinessExCode;
import com.open.framework.demo.model.*;
import com.open.framework.demo.repository.ClassroomRepository;
import com.open.framework.demo.repository.StudentRepository;
import com.open.framework.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@StartRun(methodName = {"test","test1"}, seq = 4)
@Component
public class DemoService {
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;
    @Autowired(required = false)
    private UserRepository userRepository;

    public void test() {
        System.out.println("我是第一个");
    }
    public void test1() {
        System.out.println("我是第二个");
    }

    public void testDs1() {
        String sql = "select * from user1";
        List list = jdbcTemplate.queryForList(sql);
        System.out.println("默认数据源" + list);
    }

    @ChangeDs(key = "ds1")
    public void testDs2() {
        String sql = "select * from demo";
        List list = jdbcTemplate.queryForList(sql);
        System.out.println("数据源1" + list);

    }

    @ChangeDs(key = "ds2")
    public void testDs3() {
        String sql = "select * from sm_code_rule_reg";
        List list = jdbcTemplate.queryForList(sql);
        System.out.println("数据源2" + list);
    }

    public void testEx1() {
        throw new PlatformException(EnumBase.PlatformCode.TRANSACTION_ERROR);
    }

    public void testEx2() {
        throw new BusinessException(BusinessExCode.CODE_ERROR);
    }

    public void testEx3() {
        throw new BusinessException(BusinessExCode.NAME_ERROR, new Object[]{"test"});
    }

    public void save(User user) {
        //需要回滚的事物,需要手动申明
        userRepository.save(user);
    }

    public void query() {
        List list = userRepository.findByName("hsj");
        List list2 = userRepository.getInfo("hsj");
        System.out.println("我是1:" + list);
        System.out.println("我是2:" + list2);
    }

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassroomRepository classroomRepository;


    public void testSort() {
        List<Student> stus = studentRepository.findAll(
                new SimpleSpecBuilder<Student>("address", "eq", "zt").getSpec(),
                SortUtil.sort("gid_d"));
        System.out.println(stus);
    }

    public void testPage() {
        //注意:page是从0开始的
        Specification specification= new SimpleSpecBuilder()/*.and("address", "eq", "zt").and("address", "eq", "zt")*/
                //.or(new String[]{"address","address"},new String[]{"eq","eq"},new Object[]{"zt","zt"})
        .or(new String[]{"address","address"},new String[]{"eq","eq"},new Object[]{"zt","zt"}).and(new String[]{"address","address"},new String[]{"eq","eq"},new Object[]{"zt","zt"}).getSpec();
        Page<Student> stus = studentRepository.findAll(
                specification,
                PageUtil.getPage(1, SortUtil.sort("name_d")));
        for(Student u : stus) {
            System.out.println(u.getAddress());
        }
        System.out.println(stus.getContent());
    }

    public void testDeleStu1() {
        studentRepository.deleteByCla("222");

    }

    public void delete() {
        Student aa=new Student();
        aa.setGid("1");
        studentRepository.delete(aa);
        //studentRepository.deleteById("1");
        //studentRepository.updateByHql("delete from Student where cid=?", "1");
    }

    public void testDeleteClassroom() {
        classroomRepository.delete("4");
    }

    public void testListStu() {
        List<StudentBadDto> sds = studentRepository.listBadStu();
        System.out.println(sds);
    }


    public void testListGoodStu() {
        List<StudentGoodDto> sds = studentRepository.listGoodStu();
        System.out.println(sds);
    }

    public void testListClassroomStuNum() {
        List<ClassroomStuNumDto> sns = classroomRepository.listClassrooms();
        System.out.println(sns);
    }


    public void testListClassroomDto() {
        List<ClassroomDto> clas = classroomRepository.listClassroomDto();
        for (ClassroomDto cla : clas) {
            System.out.println(cla.getName() + "," + cla.getStus());
        }
    }


    public void testAddClassroom() {
        Classroom cla = new Classroom();
        cla.setGrade("2016");
        cla.setName("16计算机网络");
        classroomRepository.save(cla);
        cla = new Classroom();
        cla.setGrade("2015");
        cla.setName("15计算机网络");
        classroomRepository.save(cla);
    }
    public void  listBySQL(){
       List list= classroomRepository.listBySQL("select cla.gid as cid,cla.name as cname,cla.grade,stu.gid,stu.name,stu.age,stu.address from t_Classroom cla,t_Student stu where cla.gid=stu.cid");
        System.out.println(list);
    }
    public Page testQuery() {
        //注意:page是从0开始的
        Page<Student> stus = studentRepository.findAll(
                new SimpleSpecBuilder<Student>("address", BaseConstant.IS_NOT_IN, "zt,bb").getSpec(),
                PageUtil.getPage(3, SortUtil.sort("name_d")));
       return  stus;
    }
    @Async("openAsyncServiceExecutor")
    public void executeAsync() {
        System.out.println("start executeAsync");
        try{
            Thread.sleep(10000);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("end executeAsync");
    }

    @Autowired
    private ApplicationContext context;

    public void publishEvent() {
        context.publishEvent(new OpenEvent("这是我发布的消息"));
    }
}
