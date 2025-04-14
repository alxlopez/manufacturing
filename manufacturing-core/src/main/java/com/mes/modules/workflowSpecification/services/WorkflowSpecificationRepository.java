package com.mes.modules.workflowSpecification.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.WorkflowSpecification.QWorkflowSpecification;
import com.mes.dom.WorkflowSpecification.WorkflowSpecification;

@RepositoryRestResource(collectionResourceRel = "workflowSpecifications", path = "workflowSpecifications")
@RestResource(exported = false)
@Repository 
public interface WorkflowSpecificationRepository extends JpaRepository<WorkflowSpecification, Long>,
QueryDslPredicateExecutor<WorkflowSpecification> ,
QuerydslBinderCustomizer<QWorkflowSpecification> {
	@RestResource(exported = false)
	public WorkflowSpecification findById(Long id);
	@RestResource(exported = false)
	public WorkflowSpecification findByCode(String code);
	default void customize(QuerydslBindings bindings, QWorkflowSpecification workflowSpecification) { }
}