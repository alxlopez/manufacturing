package com.mes.modules.material.services;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;


import com.mes.dom.material.MaterialDefinition;
import com.mes.dom.material.QMaterialDefinition;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;

@RepositoryRestResource(collectionResourceRel = "materialDefinitions", path = "materialDefinitions")
@RestResource(exported = true)
@Repository 
public interface MaterialDefinitionRepository extends
JpaRepository<MaterialDefinition, Long>,
QueryDslPredicateExecutor<MaterialDefinition>,
QuerydslBinderCustomizer<QMaterialDefinition> {
	public MaterialDefinition findByCode(String code);
	@RestResource(path="description" ,rel = "description")
	public Page<MaterialDefinition> findByDescriptionContaining(@Param(value="like")String description, Pageable p);
	@RestResource(path="code" ,rel = "code")
	public Page<MaterialDefinition> findByCodeContaining(@Param(value="like")String code,Pageable p);
	
	default void customize(QuerydslBindings bindings, QMaterialDefinition materialDefinition) {
		bindings.bind(materialDefinition.description).first((path, value) -> path.contains(value));
	}
}

