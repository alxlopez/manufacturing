package com.mes.modules.material.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import com.mes.dom.material.MaterialLot;
import com.mes.dom.material.QMaterialLot;

@RepositoryRestResource(collectionResourceRel = "materialLots", path = "materialLots")
@RestResource(exported = true)
@Repository 
public interface MaterialLotRepository extends
JpaRepository<MaterialLot, Long>,
QueryDslPredicateExecutor<MaterialLot>,
QuerydslBinderCustomizer<QMaterialLot>{
	public MaterialLot findById(Long id);	
	public MaterialLot findByCode(String code);
	
	default void customize(QuerydslBindings bindings, QMaterialLot materialLot) {
		bindings.bind(materialLot.description).first((path, value) -> path.contains(value));
	}
}
