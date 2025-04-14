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
public class MaterialDefinitionProperty extends Property implements Serializable{
	private static final long serialVersionUID = -6351006977245042780L;
	@ManyToOne
	@JoinColumn(name="materialDefinition", referencedColumnName="id")	
	@JsonBackReference("materialDefinitionProperties")
	private MaterialDefinition materialDefinition;
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=false,updatable=true) 
	@JsonBackReference("materialDefinitionProperty-parent-child")
	private MaterialDefinitionProperty parent;
	@OneToMany(cascade={CascadeType.ALL},orphanRemoval = true) 
	@JoinColumn(name = "parent_id") 
	@JsonManagedReference("materialDefinitionProperty-parent-child")
	private Set<MaterialDefinitionProperty> properties = new HashSet<MaterialDefinitionProperty>();
	/*----------------------------------------------------------------------------*/	

	public MaterialDefinitionProperty() { }

	public MaterialDefinitionProperty(String code, String value, MaterialDefinition material) {
		this.setCode(code);
		this.setValue(value);
		this.setMaterialDefinition(material);
	}

	public MaterialDefinitionProperty(String code, String value, MaterialDefinitionProperty property) {
		this.setCode(code);
		this.setValue(value);
		this.setParent(property);
	}

	public MaterialDefinitionProperty addProperty(String code, String value) {
		MaterialDefinitionProperty property = new MaterialDefinitionProperty(code, value, this);
		this.properties.add(property);
		return property;
	}

	public MaterialDefinitionProperty getProperty(String code) {
		for (MaterialDefinitionProperty property: this.properties) {
			if (property.getCode().equals(code)) {
				return property;
			}
		}
		return null;
	}

	public Collection<MaterialDefinitionProperty> getProperties(String code) {
		Collection<MaterialDefinitionProperty> properties = Collections.emptyList();
		for (MaterialDefinitionProperty property: this.properties) {
			if (property.getCode().equals(code)) {
				properties.add(property);
			}
		}
		return properties;
	}
}
