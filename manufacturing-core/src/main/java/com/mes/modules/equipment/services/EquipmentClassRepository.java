package com.mes.modules.equipment.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.equipment.EquipmentClass;
import com.mes.dom.equipment.QEquipmentClass;

@RepositoryRestResource(collectionResourceRel = "equipmentClasses", path = "equipmentClasses")
@RestResource(exported = false)
@Repository 
public interface EquipmentClassRepository extends JpaRepository<EquipmentClass, Long>,
QueryDslPredicateExecutor<EquipmentClass>,
QuerydslBinderCustomizer<QEquipmentClass>{
	public EquipmentClass findById(Long id);
	public EquipmentClass findByCode(String code);
	
	default void customize(QuerydslBindings bindings, QEquipmentClass equipmentClass) {
		bindings.bind(equipmentClass.description).first((path, value) -> path.contains(value));
	}
}
