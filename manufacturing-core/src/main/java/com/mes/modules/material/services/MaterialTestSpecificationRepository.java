package com.mes.modules.material.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.material.*;

@RepositoryRestResource(collectionResourceRel = "materialTestSpecifications", path = "materialTestSpecifications")
@RestResource(exported = true)
@Repository 
public interface MaterialTestSpecificationRepository extends
JpaRepository<MaterialTestSpecification, Long>,
QueryDslPredicateExecutor<MaterialTestSpecification>,
QuerydslBinderCustomizer<QMaterialTestSpecification>{
	default void customize(QuerydslBindings bindings, QMaterialTestSpecification materialTestSpecification) {
		bindings.bind(materialTestSpecification.description).first((path, value) -> path.contains(value));
	}
}
