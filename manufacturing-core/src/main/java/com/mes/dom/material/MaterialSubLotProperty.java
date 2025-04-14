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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mes.dom.common.Property;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name="material_sublot_property")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"properties"}, callSuper=true)
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
public class MaterialSubLotProperty extends Property implements Serializable {
	private static final long serialVersionUID = 2815688565541199141L;
	@OneToOne
	@JoinColumn(name="materialLotProperty",referencedColumnName="id")
	private MaterialLotProperty materialLotProperty;
	@ManyToOne
	@JoinColumn(name="materialSublot", referencedColumnName="id")	
	@JsonBackReference("materialSublot")
	private MaterialSubLot materialSublot;
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=false,updatable=true) 
	@JsonBackReference("materialSubLotProperty-parent-child")
	private MaterialSubLotProperty parent;
	@OneToMany(cascade={CascadeType.ALL},orphanRemoval=true) 
	@JoinColumn(name = "parent_id") 
	@JsonManagedReference("materialSubLotProperty-parent-child")
	private Set<MaterialSubLotProperty> properties = new HashSet<MaterialSubLotProperty>();
	/*----------------------------------------------------------------------------*/

	public MaterialSubLotProperty() { }
	
	public MaterialSubLotProperty(Long id) {
		this.setId(id);
	}

	public MaterialSubLotProperty getProperty(String code) {
		for (MaterialSubLotProperty materialSubLotProperty: this.properties) {
			if (materialSubLotProperty.getCode().equals(code)) {
				return materialSubLotProperty;
			}
		}
		return null;
	}

	public Collection<MaterialSubLotProperty> getProperties(String code) {
		Collection<MaterialSubLotProperty> properties = Collections.emptyList();
		for (MaterialSubLotProperty materialSubLotProperty: this.properties) {
			if (materialSubLotProperty.getCode().equals(code)) {
				properties.add(materialSubLotProperty);
			}
		}
		return properties;
	}
}
