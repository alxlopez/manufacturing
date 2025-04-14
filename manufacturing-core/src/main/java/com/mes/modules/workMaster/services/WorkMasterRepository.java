package com.mes.modules.workMaster.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.workDirective.QWorkMaster;
import com.mes.dom.workDirective.WorkMaster;

@RepositoryRestResource(collectionResourceRel = "workMasters", path = "workMasters")
@RestResource(exported = false)
@Repository
public interface WorkMasterRepository extends JpaRepository<WorkMaster, Long> ,
QueryDslPredicateExecutor<WorkMaster> ,
QuerydslBinderCustomizer<QWorkMaster> {
	public WorkMaster findById(Long id);
	default void customize(QuerydslBindings bindings, QWorkMaster workMaster) {		
		bindings.bind(workMaster.description).first((path, value) -> path.contains(value));
	}
}
