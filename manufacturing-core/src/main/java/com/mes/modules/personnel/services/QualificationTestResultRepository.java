package com.mes.modules.personnel.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.personnel.QualificationTestResult;
import com.mes.dom.personnel.QualificationTestSpecification;

@RepositoryRestResource(collectionResourceRel = "qualificationTestResults", path = "qualificationTestResults")
@RestResource(exported = false)
@Repository 
public interface QualificationTestResultRepository  extends JpaRepository<QualificationTestResult, Long> {
	public QualificationTestResult findByDescription(String description);
	public QualificationTestResult findById(Long id);
	public QualificationTestResult findByQualificationTestSpecification(QualificationTestSpecification qualificationTestSpecification);
}
