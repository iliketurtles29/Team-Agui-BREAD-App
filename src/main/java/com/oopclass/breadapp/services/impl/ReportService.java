package com.oopclass.breadapp.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oopclass.breadapp.models.Report;
import com.oopclass.breadapp.repository.ReportRepository;
import com.oopclass.breadapp.services.IReportService;

/**
 *
 * @author alvin
 */
@Service
public class ReportService implements IReportService{
    
    @Autowired
	private ReportRepository instructorRepository;
	
	@Override
	public Report save(Report entity) {
		return instructorRepository.save(entity);
	}

	@Override
	public Report update(Report entity) {
		return instructorRepository.save(entity);
	}

	@Override
	public void delete(Report entity) {
		instructorRepository.delete(entity);
	}

	@Override
	public void delete(Long id) {
		instructorRepository.deleteById(id);
	}

	@Override
	public Report find(Long id) {
		return instructorRepository.findById(id).orElse(null);
	}

	@Override
	public List<Report> findAll() {
		return instructorRepository.findAll();
	}

	@Override
	public void deleteInBatch(List<Report> users) {
		instructorRepository.deleteInBatch(users);
	}

}
