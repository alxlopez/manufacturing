package com.mes.modules.workSchedule.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.workSchedule.JobOrderMaterialRequirementProperty;

@Repository
@RestResource(exported = false)
public interface JobOrderMaterialRequirementPropertyRepository extends JpaRepository<JobOrderMaterialRequirementProperty,Long>, QueryByExampleExecutor<JobOrderMaterialRequirementProperty> { }
