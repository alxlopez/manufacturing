package com.mes.modules.material.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.material.*;

@RepositoryRestResource(collectionResourceRel = "materialClasses", path = "materialClasses")
@RestResource(exported = true)
@Repository 
public interface MaterialClassRepository extends 
JpaRepository<MaterialClass, Long>,
QueryDslPredicateExecutor<MaterialClass>,
QuerydslBinderCustomizer<QMaterialClass> {
	@RestResource(path="description" ,rel = "description")
	public Page<MaterialClass> findByDescriptionContaining(@Param(value="like")String description, Pageable p);
	@RestResource(path="code" ,rel = "code")
	public Page<MaterialClass> findByCodeContaining(@Param(value="like")String code,Pageable p);

	default void customize(QuerydslBindings bindings, QMaterialClass materialClass) {
		bindings.bind(materialClass.description).first((path, value) -> path.contains(value));
	}
}
