package com.mes.modules.manufacturingEngine.event.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.event.EventType;
import com.mes.dom.event.QEventType;

@RepositoryRestResource(collectionResourceRel = "eventTypes", path = "eventTypes")
@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long>,
QueryDslPredicateExecutor<EventType>,
QuerydslBinderCustomizer<QEventType> {
	public EventType findById(Long id);	
	
	default void customize(QuerydslBindings bindings, QEventType eventType) { }
}