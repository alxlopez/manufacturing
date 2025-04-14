package com.mes.dom.equipment;

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
import javax.persistence.GenerationType;
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
import com.mes.dom.enumerations.application.EquipmentLevel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"equipmentClasses", "parent", "properties", "equipments"})
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
public class EquipmentClass implements Serializable {
	private static final long serialVersionUID = -6987612972351638028L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=false,length=70)
	private String code;
	@Column(columnDefinition = "mediumtext")
	private String description;
	@Enumerated(EnumType.STRING)
	private EquipmentLevel equipmentLevel;
	@OneToOne
	@JoinColumn(name="location", referencedColumnName="id")
	private Equipment location;
	private String hierarchyScope;
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=true,updatable=true) 
	@JsonBackReference("equipmentClass-parent-child")
	private EquipmentClass parent;
	@OneToMany(cascade={CascadeType.PERSIST}) 
	@JoinColumn(name = "parent_id", insertable = true, updatable = false) 
	@JsonManagedReference("equipmentClass-parent-child")
	private Set<EquipmentClass> equipmentClasses = new HashSet<EquipmentClass>();
	/*----------------------------------------------------------------------------*/
	/*To associate properties*/
	@OneToMany(mappedBy="equipmentClass", cascade={CascadeType.ALL})
	@JsonManagedReference("equipmentClass")
	private Collection<EquipmentClassProperty> properties;
	//New :: Bi-Directional Relation
	@ManyToMany
	private Collection<Equipment> equipments;	

	public EquipmentClass() { }

	public EquipmentClass (Long id) {
		this.id = id; 
	}

	public EquipmentClassProperty getProperty(String code) {
		for (EquipmentClassProperty equipmentClassProperty: this.properties) {
			if (equipmentClassProperty.getCode().equals(code)) {
				return equipmentClassProperty;
			}
		}
		return null;
	}

	public Collection<EquipmentClassProperty> getProperties(String code) {
		Collection<EquipmentClassProperty> properties = Collections.emptyList();
		for (EquipmentClassProperty equipmentClassProperty: this.properties) {
			if (equipmentClassProperty.getCode().equals(code)) {
				properties.add(equipmentClassProperty);
			}
		}
		return properties;
	}
}
