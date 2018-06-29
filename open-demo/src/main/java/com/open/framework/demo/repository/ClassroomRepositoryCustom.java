package com.open.framework.demo.repository;

import com.open.framework.demo.model.ClassroomDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClassroomRepositoryCustom {
	
	@Transactional
	public void delete(String cla);
	
	/**
	 * 查询班级dto
	 * @return
	 */
	public List<ClassroomDto> listClassroomDto();
}
