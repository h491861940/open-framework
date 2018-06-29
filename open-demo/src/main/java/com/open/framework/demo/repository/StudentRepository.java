package com.open.framework.demo.repository;


import com.open.framework.demo.model.Student;
import com.open.framework.dao.repository.base.BaseRepository;
import com.open.framework.demo.model.StudentBadDto;
import com.open.framework.demo.model.StudentGoodDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface StudentRepository extends BaseRepository<Student,String>,JpaSpecificationExecutor<Student> {

	@Modifying //说明该操作是修改类型操作，删除或者修改
	@Transactional //因为默认是readOnly=true的，这里必须自己进行声明
	@Query("delete from Student where cid=?1") //删除的语句
	public void deleteByCla(String cid);
	
	@Query("select new com.open.framework.demo.model.StudentBadDto(stu,cla) from Student stu,Classroom cla where stu.cid=cla.gid")
	public List<StudentBadDto> listBadStu();
	
	@Query("select new com.open.framework.demo.model.StudentGoodDto(stu.gid,stu.name,cla.gid,cla.name,cla.grade) from Student stu,Classroom cla where stu.cid=cla.gid")
	public List<StudentGoodDto> listGoodStu();
	
}
