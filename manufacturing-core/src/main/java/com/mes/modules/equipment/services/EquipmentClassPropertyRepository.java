package com.mes.modules.equipment.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.equipment.EquipmentClassProperty;
import com.mes.dom.equipment.QEquipmentClassProperty;

@RepositoryRestResource(collectionResourceRel = "equipmentClassProperties", path = "equipmentClassProperties")
@RestResource(exported = false)
@Repository 
public interface EquipmentClassPropertyRepository extends JpaRepository<EquipmentClassProperty, Long>,
QueryDslPredicateExecutor<EquipmentClassProperty>,
QuerydslBinderCustomizer<QEquipmentClassProperty>{
	public EquipmentClassProperty findById(Long id);
	public EquipmentClassProperty findByCode(String code);

	default void customize(QuerydslBindings bindings, QEquipmentClassProperty equipmentClassProperty) {
		bindings.bind(equipmentClassProperty.description).first((path, value) -> path.contains(value));
	}
}
