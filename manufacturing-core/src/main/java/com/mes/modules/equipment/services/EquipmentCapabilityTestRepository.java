package com.mes.modules.equipment.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.equipment.EquipmentCapabilityTestSpecification;
import com.mes.dom.equipment.QEquipmentCapabilityTestSpecification;

@RepositoryRestResource(collectionResourceRel = "equipmentCapabilityTests", path = "equipmentCapabilityTests")
@Repository 
public interface EquipmentCapabilityTestRepository extends JpaRepository<EquipmentCapabilityTestSpecification, Long>, 
QueryDslPredicateExecutor<EquipmentCapabilityTestSpecification>,
QuerydslBinderCustomizer<QEquipmentCapabilityTestSpecification>{
	public EquipmentCapabilityTestSpecification findById(Long id);
	public EquipmentCapabilityTestSpecification findByCode(String code);

	default void customize(QuerydslBindings bindings, QEquipmentCapabilityTestSpecification equipmentCapabilityTestSpecification) {
		bindings.bind(equipmentCapabilityTestSpecification.description).first((path, value) -> path.contains(value));
	}
}
