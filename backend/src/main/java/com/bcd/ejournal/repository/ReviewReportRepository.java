package com.bcd.ejournal.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bcd.ejournal.domain.entity.ReviewReport;

@Repository
public interface ReviewReportRepository extends CrudRepository<ReviewReport, Integer>{

}
