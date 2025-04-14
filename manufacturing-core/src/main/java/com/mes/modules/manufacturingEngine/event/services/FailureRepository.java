package com.mes.modules.manufacturingEngine.event.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.event.Failure;
import com.mes.dom.event.QFailure;

@RepositoryRestResource(collectionResourceRel = "failures", path = "failures")
@Repository
public interface FailureRepository extends JpaRepository<Failure, Long> ,
QueryDslPredicateExecutor<Failure>,
QuerydslBinderCustomizer<QFailure> {
	public Failure findById(Long id);	
	
	default void customize(QuerydslBindings bindings, QFailure eventDefinition) {
		bindings.bind(eventDefinition.description).first((path, value) -> path.contains(value));
	}
}