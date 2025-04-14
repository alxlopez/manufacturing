package com.mes.modules.material.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.material.MaterialLotProperty;
import com.mes.dom.material.QMaterialLotProperty;

@RepositoryRestResource(collectionResourceRel = "materialLotProperties", path = "materialLotProperties")
@RestResource(exported = true)
@Repository 
public interface MaterialLotPropertyRepository extends
JpaRepository<MaterialLotProperty, Long>,
QueryDslPredicateExecutor<MaterialLotProperty>,
QuerydslBinderCustomizer<QMaterialLotProperty>{
	default void customize(QuerydslBindings bindings, QMaterialLotProperty materialLotProperty) {
		bindings.bind(materialLotProperty.description).first((path, value) -> path.contains(value));
	}
}
