package com.open.framework.demo.web;

import com.open.framework.core.web.BaseController;
import com.open.framework.core.web.version.ApiVersion;
import com.open.framework.demo.model.Student;
import com.open.framework.demo.model.StudentDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/user")
public class UserController extends BaseController<Student,StudentDTO> {


}
