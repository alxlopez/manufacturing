package com.mes.dom.equipment;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany; 

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mes.dom.common.Property;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"properties"}, callSuper=true)
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
public class EquipmentProperty extends Property implements Serializable{
	private static final long serialVersionUID = 1565366275215166071L;
	@ManyToOne
	@JoinColumn(name="equipment", referencedColumnName="id")
	@JsonBackReference("equipment")	
	private Equipment equipment; 	
	/*----------------------------------------------------------------------------*/
	//Changed properties in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=false,updatable=true) 
	@JsonBackReference("equipmentProperty-parent-child")
	private EquipmentProperty parent;
	@OneToMany(cascade={CascadeType.ALL}) 
	@JoinColumn(name = "parent_id") 
	@JsonManagedReference("equipmentProperty-parent-child")
	private Set<EquipmentProperty> properties = new HashSet<EquipmentProperty>();
	/*-------------------------------------------------------------------------*/

	public EquipmentProperty() { }

	public EquipmentProperty(Long id) {
		this.setId(id);
	}
	
	public EquipmentProperty getProperty(String code) {
		for (EquipmentProperty equipmentProperty: this.properties) {
			if (equipmentProperty.getCode().equals(code)) {
				return equipmentProperty;
			}
		}
		return null;
	}

	public Collection<EquipmentProperty> getProperties(String code) {
		Collection<EquipmentProperty> properties = Collections.emptyList();
		for (EquipmentProperty equipmentProperty: this.properties) {
			if (equipmentProperty.getCode().equals(code)) {
				properties.add(equipmentProperty);
			}
		}
		return properties;
	}
}
