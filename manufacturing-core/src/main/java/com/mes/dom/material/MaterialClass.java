package com.mes.dom.material;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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
@EqualsAndHashCode(exclude={"materialClasses", "parent", "properties", "materialDefinitions"})
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
public class MaterialClass implements Serializable{
	private static final long serialVersionUID = 5824729849185827842L;
	@Id
	@GeneratedValue
	@Column(name="id")
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
	/*To associate properties*/
	@OneToMany(mappedBy = "materialClass", cascade = CascadeType.ALL, orphanRemoval=true)
	@JsonManagedReference("materialClassProperties")
	private Collection<MaterialClassProperty> properties;
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*---------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=true,updatable=true) 
	@JsonBackReference("materialClass-parent-child")
	private MaterialClass parent;
	@OneToMany(cascade={CascadeType.PERSIST} ) 
	@JoinColumn(name = "parent_id", insertable = true, updatable = false)  
	@JsonManagedReference("materialClass-parent-child")
	private Set<MaterialClass> materialClasses = new HashSet<MaterialClass>();
	/*----------------------------------------------------------------------------*/	
	//New :: Bi-Directional Relation (Use only material definitions created previously)
	@ManyToMany(mappedBy="materialClasses")
	private Collection<MaterialDefinition> materialDefinitions;	

	public MaterialClass() { }

	public MaterialClass(Long id) {
		this.id=id;
	}

	public MaterialClassProperty getProperty(String code) {
		for (MaterialClassProperty materialClassProperty: this.properties) {
			if (materialClassProperty.getCode().equals(code)) {
				return materialClassProperty;
			}
		}
		return null;
	}

	public Collection<MaterialClassProperty> getProperties(String code) {
		Collection<MaterialClassProperty> properties = Collections.emptyList();
		for (MaterialClassProperty materialClassProperty: this.properties) {
			if (materialClassProperty.getCode().equals(code)) {
				properties.add(materialClassProperty);
			}
		}
		return properties;
	}
}
