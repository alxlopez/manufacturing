package com.mes.dom.material;

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
import com.mes.dom.equipment.Equipment;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"testedMaterialClassProperties", "testedMaterialDefinitionProperties"})
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
public class MaterialTestSpecification implements Serializable{
	private static final long serialVersionUID = -869551972317107160L;
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
	@OneToMany(mappedBy="materialTestSpecification", cascade = CascadeType.ALL)
	@JsonManagedReference("testedMaterialClassProperties")		
	private Collection<TestedMaterialClassProperty> testedMaterialClassProperties; 
	@OneToMany(mappedBy="materialTestSpecification", cascade = CascadeType.ALL)
	@JsonManagedReference("testedMaterialDefinitionProperties")		
	private Collection<TestedMaterialDefinitionProperty> testedMaterialDefinitionProperties; 

	public MaterialTestSpecification() { }
	
	public MaterialTestSpecification(Long id) {
		this.id = id; 
	}

	public TestedMaterialClassProperty getTestedMaterialClassProperty(Long id) {
		for (TestedMaterialClassProperty testedMaterialClassProperty: this.testedMaterialClassProperties) {
			if (testedMaterialClassProperty.getId().equals(id)) {
				return testedMaterialClassProperty;
			}
		}
		return null;
	}

	public Collection<TestedMaterialClassProperty> getTestedMaterialClassProperties(Long id) {
		Collection<TestedMaterialClassProperty> testedMaterialClassProperties = Collections.emptyList();
		for (TestedMaterialClassProperty testedMaterialClassProperty: this.testedMaterialClassProperties) {
			if (testedMaterialClassProperty.getId().equals(id)) {
				testedMaterialClassProperties.add(testedMaterialClassProperty);
			}
		}
		return testedMaterialClassProperties;
	}

	public TestedMaterialDefinitionProperty getTestedMaterialDefinitionProperty(Long id) {
		for (TestedMaterialDefinitionProperty testedMaterialDefinitionProperty: this.testedMaterialDefinitionProperties) {
			if (testedMaterialDefinitionProperty.getId().equals(id)) {
				return testedMaterialDefinitionProperty;
			}
		}
		return null;
	}

	public Collection<TestedMaterialDefinitionProperty> getTestedMaterialDefinitionProperties(Long id) {
		Collection<TestedMaterialDefinitionProperty> testedMaterialDefinitionProperties = Collections.emptyList();
		for (TestedMaterialDefinitionProperty testedMaterialDefinitionProperty: this.testedMaterialDefinitionProperties) {
			if (testedMaterialDefinitionProperty.getId().equals(id)) {
				testedMaterialDefinitionProperties.add(testedMaterialDefinitionProperty);
			}
		}
		return testedMaterialDefinitionProperties;
	}
}
