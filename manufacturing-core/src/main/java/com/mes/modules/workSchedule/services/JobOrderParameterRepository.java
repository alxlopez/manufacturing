package com.mes.modules.workSchedule.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.workSchedule.JobOrderParameter;

@Repository
@RestResource(exported = true)// if we want the object to be render as json instead of  uri we can enable this
public interface JobOrderParameterRepository extends JpaRepository<JobOrderParameter, Long> , QueryByExampleExecutor<JobOrderParameter> { }
