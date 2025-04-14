package com.mes.modules.manufacturingEngine.event.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.event.ExecutionEvent;
import com.mes.dom.event.QExecutionEvent;

@RepositoryRestResource(collectionResourceRel = "events", path = "events")
@Repository
public interface EventRepository extends JpaRepository<ExecutionEvent, Long> ,
QueryDslPredicateExecutor<ExecutionEvent>,
QuerydslBinderCustomizer<QExecutionEvent> {
	public ExecutionEvent findById(Long id);

	default void customize(QuerydslBindings bindings, QExecutionEvent event) { }
}
