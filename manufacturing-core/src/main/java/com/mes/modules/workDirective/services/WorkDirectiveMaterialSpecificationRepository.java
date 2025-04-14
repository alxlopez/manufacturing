package com.mes.modules.workDirective.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;


import com.mes.dom.workDirective.WorkDirectiveMaterialSpecification;

@RepositoryRestResource(collectionResourceRel = "workDirectiveMaterialSpecifications", path = "workDirectiveMaterialSpecifications")
@RestResource(exported = false)
@Repository 
public interface WorkDirectiveMaterialSpecificationRepository extends JpaRepository<WorkDirectiveMaterialSpecification, Long> {
	public WorkDirectiveMaterialSpecification findById(Long id); 
}
