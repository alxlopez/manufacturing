package com.mes.modules.manufacturingEngine.event.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.event.ExecutionEvent;
import com.mes.dom.event.EventParameter;
import com.mes.dom.event.QEventParameter;

@RepositoryRestResource(collectionResourceRel = "eventProperties", path = "eventProperties")
@Repository
public interface EventPropertyRepository extends JpaRepository<EventParameter, Long> ,
QueryDslPredicateExecutor<EventParameter>,
QuerydslBinderCustomizer<QEventParameter> {
	public EventParameter findById(Long id);	
	public List<EventParameter> findByEvent(ExecutionEvent event);

	default void customize(QuerydslBindings bindings, QEventParameter eventProperty) {
		bindings.bind(eventProperty.description).first((path, value) -> path.contains(value));
	}
}
