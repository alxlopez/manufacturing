package com.mes.dom.equipment;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"testedEquipmentProperties"})
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
public class EquipmentCapabilityTestSpecification implements Serializable{
	private static final long serialVersionUID = -892696132466227517L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=false,length=70)
	private String code;	
	@Column(length=70)
	private String name;
	@Column(columnDefinition = "mediumtext")
	private String description;
	private String version;
	@OneToOne
	@JoinColumn(name="location", referencedColumnName="id")
	private Equipment location;
	private String hierarchyScope;
	@OneToMany(mappedBy="equipmentCapabilityTestSpecification", cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	@JsonManagedReference("testedEquipmentClassProperties")	
	private Collection<TestedEquipmentClassProperty> testedEquipmentClassProperties;  
	@OneToMany(mappedBy="equipmentCapabilityTestSpecification", cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	@JsonManagedReference("testedEquipmentProperties")
	private Collection<TestedEquipmentProperty> testedEquipmentProperties; 

	public EquipmentCapabilityTestSpecification() { }

	public EquipmentCapabilityTestSpecification(Long id) {
		this.id =id; 
	}

	public TestedEquipmentClassProperty getTestedEquipmentClassProperty(Long id) {
		for (TestedEquipmentClassProperty testedEquipmentClassProperty: this.testedEquipmentClassProperties) {
			if (testedEquipmentClassProperty.getId().equals(id)) {
				return testedEquipmentClassProperty;
			}
		}
		return null;
	}

	public Collection<TestedEquipmentClassProperty> getTestedEquipmentClassProperties(Long id) {
		Collection<TestedEquipmentClassProperty> testedEquipmentClassProperties = Collections.emptyList();
		for (TestedEquipmentClassProperty testedEquipmentClassProperty: this.testedEquipmentClassProperties) {
			if (testedEquipmentClassProperty.getId().equals(id)) {
				testedEquipmentClassProperties.add(testedEquipmentClassProperty);
			}
		}
		return testedEquipmentClassProperties;
	}

	public TestedEquipmentProperty getTestedEquipmentProperty(Long id) {
		for (TestedEquipmentProperty testedEquipmentProperty: this.testedEquipmentProperties) {
			if (testedEquipmentProperty.getId().equals(id)) {
				return testedEquipmentProperty;
			}
		}
		return null;
	}

	public Collection<TestedEquipmentProperty> TestedEquipmentProperties(Long id) {
		Collection<TestedEquipmentProperty> testedEquipmentProperties = Collections.emptyList();
		for (TestedEquipmentProperty testedEquipmentProperty: this.testedEquipmentProperties) {
			if (testedEquipmentProperty.getId().equals(id)) {
				testedEquipmentProperties.add(testedEquipmentProperty);
			}
		}
		return testedEquipmentProperties;
	}
}
