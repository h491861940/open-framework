package com.open.framework.demo.repository;

import com.open.framework.dao.repository.base.BaseRepository;
import com.open.framework.demo.model.Classroom;
import com.open.framework.demo.model.ClassroomStuNumDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClassroomRepository extends BaseRepository<Classroom, String>,ClassroomRepositoryCustom, JpaSpecificationExecutor<Classroom> {

	@Query("select new com.open.framework.demo.model.ClassroomStuNumDto(cla.gid,cla.name,cla.grade,count(stu.gid)) from Classroom cla ,Student stu where cla.gid=stu.cid group by cla.gid")
	public List<ClassroomStuNumDto> listClassrooms();
}
