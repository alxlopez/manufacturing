package com.mes.modules.manufacturingEngine.event.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.event.EventDefinition;
import com.mes.dom.event.QEventDefinition;

@RepositoryRestResource(collectionResourceRel = "eventDefinitions", path = "eventDefinitions")
@Repository
public interface EventDefinitionRepository extends JpaRepository<EventDefinition, Long> ,
QueryDslPredicateExecutor<EventDefinition>,
QuerydslBinderCustomizer<QEventDefinition> {
	public EventDefinition findById(Long id);
	public EventDefinition findByCode(Commands code);

	default void customize(QuerydslBindings bindings, QEventDefinition eventDefinition) { }
}
