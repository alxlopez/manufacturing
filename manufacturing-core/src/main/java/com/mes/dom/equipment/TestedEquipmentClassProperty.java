package com.mes.dom.equipment;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mes.dom.common.TestedProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(callSuper=true)
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
@Table(name="equipment_tested_class_property")
public class TestedEquipmentClassProperty extends TestedProperty implements Serializable {
	private static final long serialVersionUID = 8891973939552729409L;
	@OneToOne
	@JoinColumn(name="equipmentClass", referencedColumnName="id")	
	private EquipmentClass equipmentClass;
	@OneToOne
	@JoinColumn(name="equipmentClassProperty", referencedColumnName="id")	
	private EquipmentClassProperty equipmentClassProperty;	
	@ManyToOne
	@JsonBackReference("testedEquipmentClassProperties")
	@JoinColumn(name="equipmentCapabilityTestSpecification", referencedColumnName="id")
	private EquipmentCapabilityTestSpecification equipmentCapabilityTestSpecification;

	public TestedEquipmentClassProperty() { }
}
