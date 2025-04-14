package com.mes.modules.workSchedule.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.workSchedule.JobOrderList;

@RepositoryRestResource(collectionResourceRel = "jobOrderLists", path = "jobOrderLists")
@Repository
public interface JobOrderListRepository extends JpaRepository<JobOrderList, Long>, QueryByExampleExecutor<JobOrderList> {	
	 public JobOrderList findById(@Param(value = "id") Long id);
}
