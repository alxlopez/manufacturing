package com.mes.modules.material.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import com.mes.dom.material.MaterialClassProperty;
import com.mes.dom.material.QMaterialClassProperty;

@RepositoryRestResource(collectionResourceRel = "materialClassProperties", path = "materialClassProperties")
@RestResource(exported = true)
@Repository 
public interface MaterialClassPropertyRepository extends
JpaRepository<MaterialClassProperty, Long>,
QueryDslPredicateExecutor<MaterialClassProperty>,
QuerydslBinderCustomizer<QMaterialClassProperty> {
	default void customize(QuerydslBindings bindings, QMaterialClassProperty materialClassProperty) {
		bindings.bind(materialClassProperty.description).first((path, value) -> path.contains(value));
	}
}
