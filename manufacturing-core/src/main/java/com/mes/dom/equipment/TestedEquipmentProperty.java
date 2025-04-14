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
@Table(name="equipment_tested_equipment_property")
public class TestedEquipmentProperty extends TestedProperty implements Serializable{
	private static final long serialVersionUID = 4431362801125035657L;
	@OneToOne
	@JoinColumn(name="equipment", referencedColumnName="id")	
	private Equipment equipment;	
	@OneToOne
	@JoinColumn(name="equipmentProperty", referencedColumnName="id")	
	private EquipmentProperty equipmentProperty;	
	@ManyToOne
	@JsonBackReference("testedEquipmentProperties")	
	@JoinColumn(name="equipmentCapabilityTestSpecification", referencedColumnName="id")
	private EquipmentCapabilityTestSpecification equipmentCapabilityTestSpecification;

	public TestedEquipmentProperty() { }
}
