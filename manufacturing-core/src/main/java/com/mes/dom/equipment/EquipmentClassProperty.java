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
public class EquipmentClassProperty extends Property implements Serializable {	
	private static final long serialVersionUID = -2246719688242243368L;
	/*----------------------------------------------------------------------------*/
	//Changed properties in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=false,updatable=true) 
	@JsonBackReference("equipmentClassProperty-parent-child")
	private EquipmentClassProperty parent;
	@OneToMany(cascade={CascadeType.ALL}) 
	@JoinColumn(name = "parent_id") 
	@JsonManagedReference("equipmentClassProperty-parent-child")
	private Set<EquipmentClassProperty> properties = new HashSet<EquipmentClassProperty>();
	/*-------------------------------------------------------------------------*/
	@ManyToOne
	@JoinColumn(name="equipmentClass", referencedColumnName="id")	
	@JsonBackReference("equipmentClass")	
	private EquipmentClass equipmentClass;

	public EquipmentClassProperty() { }

	public EquipmentClassProperty(Long id){
		this.setId(id);
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
