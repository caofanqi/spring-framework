package cn.caofanqi.service.impl;

import cn.caofanqi.repository.StudentRepository;
import cn.caofanqi.service.StudentService;

public class StudentServiceImpl implements StudentService {

//	private final StudentRepository studentRepository;
//
//	public StudentServiceImpl(StudentRepository studentRepository){
//		this.studentRepository = studentRepository;
//	}


	private StudentRepository studentRepository;


	public void setStudentRepository(StudentRepository studentRepository){
		this.studentRepository = studentRepository;
	}

	@Override
	public void saveStudent() {
		studentRepository.save();
	}
}
