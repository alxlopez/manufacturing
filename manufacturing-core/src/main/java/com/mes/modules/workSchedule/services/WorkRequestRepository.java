package com.mes.modules.workSchedule.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.workSchedule.WorkRequest;

@RepositoryRestResource(collectionResourceRel = "workRequests", path = "workRequests")
@Repository
public interface WorkRequestRepository extends JpaRepository<WorkRequest, Long> , QueryByExampleExecutor<WorkRequest> {
	 public WorkRequest findById(@Param(value = "id") Long id);
}

