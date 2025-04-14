package com.mes.dom.material;

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
import javax.persistence.OneToOne;
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
public class MaterialLotProperty extends Property implements Serializable{
	private static final long serialVersionUID = 8731362473719003599L;
	@OneToOne
	@JoinColumn(name="materialDefinitionProperty",referencedColumnName="id")
	private MaterialDefinitionProperty materialDefinitionProperty;
	@ManyToOne
	@JoinColumn(name="materialLot", referencedColumnName="id")	
	@JsonBackReference("materialLotProperties")
	private MaterialLot materialLot;
	/*----------------------------------------------------------------------------*/
	// MaterialLotProperty-MaterialLotProperty composition RelationShip
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=false,updatable=true) 
	@JsonBackReference("materialLotProperty-parent-child")
	private MaterialLotProperty parent;
	@OneToMany(cascade={CascadeType.ALL} ,orphanRemoval =true) 
	@JoinColumn(name = "parent_id") 
	@JsonManagedReference("materialLotProperty-parent-child")
	private Set<MaterialLotProperty> properties = new HashSet<MaterialLotProperty>();
	/*----------------------------------------------------------------------------*/	

	public MaterialLotProperty() { }

	public MaterialLotProperty(Long id) {
		this.setId(id);
	}

	public MaterialLotProperty getProperty(String code) {
		for (MaterialLotProperty materialLotProperty: this.properties) {
			if (materialLotProperty.getCode().equals(code)) {
				return materialLotProperty;
			}
		}
		return null;
	}

	public Collection<MaterialLotProperty> getProperties(String code) {
		Collection<MaterialLotProperty> properties = Collections.emptyList();
		for (MaterialLotProperty materialLotProperty: this.properties) {
			if (materialLotProperty.getCode().equals(code)) {
				properties.add(materialLotProperty);
			}
		}
		return properties;
	}
}
