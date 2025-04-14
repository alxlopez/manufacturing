package com.mes.modules.equipment.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.equipment.EquipmentProperty;
import com.mes.dom.equipment.QEquipmentProperty;

@RepositoryRestResource(collectionResourceRel = "equipmentProperties", path = "equipmentProperties")
@RestResource(exported = false)
@Repository 
public interface EquipmentPropertyRepository  extends JpaRepository<EquipmentProperty, Long>,
QueryDslPredicateExecutor<EquipmentProperty>,
QuerydslBinderCustomizer<QEquipmentProperty>{
	public EquipmentProperty findById(Long id);
	public EquipmentProperty findByCode(String code);

	default void customize(QuerydslBindings bindings, QEquipmentProperty equipmentProperty) {
		bindings.bind(equipmentProperty.description).first((path, value) -> path.contains(value));
	}
}
