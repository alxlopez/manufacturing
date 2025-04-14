package com.mes.modules.material.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import com.mes.dom.material.MaterialSubLotProperty;
import com.mes.dom.material.QMaterialSubLotProperty;

@RepositoryRestResource(collectionResourceRel = "materialSubLotProperties", path = "materialSubLotProperties")
@RestResource(exported = true)
@Repository 
public interface MaterialSubLotPropertyRepository extends
JpaRepository<MaterialSubLotProperty, Long>,
QueryDslPredicateExecutor<MaterialSubLotProperty>,
QuerydslBinderCustomizer<QMaterialSubLotProperty>{
	default void customize(QuerydslBindings bindings, QMaterialSubLotProperty materialSubLotProperty) { }
}
