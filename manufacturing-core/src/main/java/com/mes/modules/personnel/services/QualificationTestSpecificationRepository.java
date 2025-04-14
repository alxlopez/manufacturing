package com.mes.modules.personnel.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.personnel.QualificationTestSpecification;

@RepositoryRestResource(collectionResourceRel = "qualificationTestSpecifications", path = "qualificationTestSpecifications")
@RestResource(exported = false)
@Repository 
public interface QualificationTestSpecificationRepository  extends JpaRepository<QualificationTestSpecification, Long> {
	public QualificationTestSpecification findById(Long id);
}
