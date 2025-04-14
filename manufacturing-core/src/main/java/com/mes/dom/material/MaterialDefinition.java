package com.mes.dom.material;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mes.dom.enumerations.application.AssemblyRelationShip;
import com.mes.dom.enumerations.application.AssemblyType;
import com.mes.dom.equipment.Equipment;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"materialClasses", "parent", "properties"})
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
public class MaterialDefinition implements Serializable{
	private static final long serialVersionUID = 5691930213236367715L;
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable=false,length=70)	
	private String code;
	@Column(columnDefinition = "mediumtext")
	private String description;
	@OneToOne
	@JoinColumn(name="location",referencedColumnName="id")
	private Equipment location;
	private String hierarchyScope;
	@Enumerated(EnumType.STRING)
	private AssemblyType assemblyType;
	@Enumerated(EnumType.STRING)
	private AssemblyRelationShip assemblyRelationship;
	//New :: Bi-Directional Relation
    @ManyToMany
    private Collection<MaterialClass> materialClasses;
	/*To associate properties*/
    @OneToMany(mappedBy = "materialDefinition",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
	@JsonManagedReference("materialDefinitionProperties")
	private Collection<MaterialDefinitionProperty> properties = new HashSet<MaterialDefinitionProperty>();
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=true,updatable=true) 
	@JsonBackReference("materialDefinition-parent-child")
	private MaterialDefinition parent;
	@OneToMany(cascade={CascadeType.PERSIST} )  
	@JoinColumn(name = "parent_id", insertable = true, updatable = false) 
	@JsonManagedReference("materialDefinition-parent-child")
	private Set<MaterialDefinition> materialDefinitions = new HashSet<MaterialDefinition>();
	/*----------------------------------------------------------------------------*/	

	public MaterialDefinition() { }

	public MaterialDefinition(Long id) {
		this.id = id; 
	}

	public MaterialDefinitionProperty addProperty(String code, String value) {
		MaterialDefinitionProperty property = new MaterialDefinitionProperty(code, value, this);
		this.properties.add(property);
		return property;
	}

	public MaterialDefinitionProperty getProperty(String code) {
		for (MaterialDefinitionProperty property: this.properties) {
			if (code.equals(property.getCode())) {
				return property;
			}
		}
		return null;
	}

	public List<MaterialDefinitionProperty> getProperties(String code) {
		//Collection<MaterialDefinitionProperty> properties = Collections.emptyList();
		ArrayList<MaterialDefinitionProperty> properties = new ArrayList<MaterialDefinitionProperty>();
		for (MaterialDefinitionProperty property: this.properties) {
			if (property.getCode().equals(code)) {
				properties.add(property);
			}
		}
		return properties;
	}
}
