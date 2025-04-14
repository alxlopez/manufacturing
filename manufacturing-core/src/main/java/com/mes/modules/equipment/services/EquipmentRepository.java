package com.mes.modules.equipment.services;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.equipment.Equipment;
import com.mes.dom.equipment.QEquipment;

@RepositoryRestResource(collectionResourceRel = "equipments", path = "equipments")
@RestResource(exported = false)
@Repository 
public interface EquipmentRepository extends JpaRepository<Equipment, Long>,
QueryDslPredicateExecutor<Equipment>,
QuerydslBinderCustomizer<QEquipment>{
	public Equipment findById(Long id);
	public Equipment findByCode(String code);
	public ArrayList<Equipment> findByParentId(Long parentId);

	default void customize(QuerydslBindings bindings, QEquipment equipment) {
		bindings.bind(equipment.description).first((path, value) -> path.contains(value));
		bindings.bind(equipment.hierarchyScope).first((path, value) -> path.contains(value));
	}
}
