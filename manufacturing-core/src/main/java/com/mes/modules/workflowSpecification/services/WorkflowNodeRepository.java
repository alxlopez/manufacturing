package com.mes.modules.workflowSpecification.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.WorkflowSpecification.QWorkflowNode;
import com.mes.dom.WorkflowSpecification.WorkflowNode;

@RepositoryRestResource(collectionResourceRel = "workflowSpecifications", path = "workflowSpecifications")
@RestResource(exported = true)
@Repository 
public interface WorkflowNodeRepository extends JpaRepository<WorkflowNode, Long>,
QueryDslPredicateExecutor<WorkflowNode>,
QuerydslBinderCustomizer<QWorkflowNode>{
	@RestResource(exported = false)
	public WorkflowNode findById(Long id);
	@RestResource(exported = false)
	public WorkflowNode findByCode(String code);	
	default void customize(QuerydslBindings bindings, QWorkflowNode workflowNode) { }
}
