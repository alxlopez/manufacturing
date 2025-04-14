package com.mes.modules.workSchedule.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.workSchedule.WorkSchedule;

@RepositoryRestResource(collectionResourceRel = "workSchedules", path = "workSchedules")
@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> , QueryByExampleExecutor<WorkSchedule> {
	 public WorkSchedule findById(@Param(value = "id") Long id);
}
