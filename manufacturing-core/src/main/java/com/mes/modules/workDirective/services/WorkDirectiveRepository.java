package com.mes.modules.workDirective.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.enumerations.application.WorkTypes;
import com.mes.dom.workDirective.QWorkDirective;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.dom.workSchedule.JobOrder;

@RepositoryRestResource(collectionResourceRel = "workDirectives", path = "workDirectives")
@RestResource(exported = false)
@Repository 
public interface WorkDirectiveRepository extends JpaRepository<WorkDirective, Long>,
QueryDslPredicateExecutor<WorkDirective> ,
QuerydslBinderCustomizer<QWorkDirective> {
	@RestResource(exported = false)
	public WorkDirective findById(Long id);
	@RestResource(exported = false)
	public List <WorkDirective> findByJobOrder(JobOrder jobOrder);
	@RestResource(exported = false)
	public List <WorkDirective> findByWorkType(WorkTypes workType);
	@RestResource(exported = false)
	public List <WorkDirective> findByWorkflowSpecificationId(String workFlowSpecificationId);
	
	default void customize(QuerydslBindings bindings, QWorkDirective workDirective) {
		bindings.bind(workDirective.description).first((path, value) -> path.contains(value));
	}
}
