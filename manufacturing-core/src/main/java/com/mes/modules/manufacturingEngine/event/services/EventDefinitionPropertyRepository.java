package com.mes.modules.manufacturingEngine.event.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.event.EventDefinitionProperty;
import com.mes.dom.event.QEventDefinitionProperty;

@RepositoryRestResource(collectionResourceRel = "eventDefinitionProperties", path = "eventDefinitionProperties")
@Repository
public interface EventDefinitionPropertyRepository extends JpaRepository<EventDefinitionProperty, Long> ,
QueryDslPredicateExecutor<EventDefinitionProperty>,
QuerydslBinderCustomizer<QEventDefinitionProperty> {
	public EventDefinitionProperty findById(Long id);	
	
	default void customize(QuerydslBindings bindings, QEventDefinitionProperty eventDefinitionProperty) {
		bindings.bind(eventDefinitionProperty.description).first((path, value) -> path.contains(value));
	}
}
