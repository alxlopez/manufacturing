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
public class MaterialClassProperty extends Property implements Serializable {
	private static final long serialVersionUID = -5854140291498141990L;
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=false,updatable=true) 
	@JsonBackReference("materialClassProperty-parent-child")
	private MaterialClassProperty parent;
	@OneToMany(cascade={CascadeType.ALL},orphanRemoval = true) 
	@JoinColumn(name = "parent_id") 
	@JsonManagedReference("materialClassProperty-parent-child")
	private Set<MaterialClassProperty> properties = new HashSet<MaterialClassProperty>();
	/*----------------------------------------------------------------------------*/	
	@ManyToOne
	@JoinColumn(name="materialClass", referencedColumnName="id")	
	@JsonBackReference("materialClassProperties")
	private MaterialClass materialClass;

	public MaterialClassProperty() { }

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
