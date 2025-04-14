package com.mes.modules.material.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.material.MaterialDefinition;
import com.mes.dom.material.MaterialDefinitionProperty;
import com.mes.dom.material.QMaterialDefinitionProperty;

@RepositoryRestResource(collectionResourceRel = "materialDefinitionProperties", path = "materialDefinitionProperties")
@RestResource(exported = true)
@Repository
public interface MaterialDefinitionPropertyRepository extends
JpaRepository<MaterialDefinitionProperty, Long>,
QueryDslPredicateExecutor<MaterialDefinitionProperty>,
QuerydslBinderCustomizer<QMaterialDefinitionProperty> {
	@Query("select p from MaterialDefinitionProperty p where p.materialDefinition = ?1 and p.code =?2")
	public MaterialDefinitionProperty findByCodeAndMaterialDefinition(	
			MaterialDefinition materialDefinition,		
			String propertyCode);
	default void customize(QuerydslBindings bindings, QMaterialDefinitionProperty materialDefinitionProperty) {
		bindings.bind(materialDefinitionProperty.description).first((path, value) -> path.contains(value));
	}
}
