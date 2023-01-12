package com.oopclass.breadapp.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oopclass.breadapp.models.Instructor;
import com.oopclass.breadapp.repository.InstructorRepository;
import com.oopclass.breadapp.services.IInstructorService;

/**
 *
 * @author alvin
 */
@Service
public class InstructorService implements IInstructorService{
    
    @Autowired
	private InstructorRepository instructorRepository;
	
	@Override
	public Instructor save(Instructor entity) {
		return instructorRepository.save(entity);
	}

	@Override
	public Instructor update(Instructor entity) {
		return instructorRepository.save(entity);
	}

	@Override
	public void delete(Instructor entity) {
		instructorRepository.delete(entity);
	}

	@Override
	public void delete(Long id) {
		instructorRepository.deleteById(id);
	}

	@Override
	public Instructor find(Long id) {
		return instructorRepository.findById(id).orElse(null);
	}

	@Override
	public List<Instructor> findAll() {
		return instructorRepository.findAll();
	}

	@Override
	public void deleteInBatch(List<Instructor> users) {
		instructorRepository.deleteInBatch(users);
	}

}
