package com.mes.dom.workDirective;

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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mes.dom.common.Property;

import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
@EqualsAndHashCode(exclude={"properties"}, callSuper=true)
public class WorkDirectiveMaterialSpecificationProperty extends Property implements Serializable, Cloneable{	
	private static final long serialVersionUID = -2816587132964063967L;
	@ManyToOne
	@JoinColumn(name="workDirectiveMaterialSpecification", referencedColumnName="id")
	@JsonBackReference("workDirectiveMaterialSpecificationProperty")	
	private WorkDirectiveMaterialSpecification workDirectiveMaterialSpecification; 
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=false,updatable=true) 
	@JsonBackReference("workDirectiveMaterialSpecificationProperty-parent-child")
	private WorkDirectiveMaterialSpecificationProperty parent;
	@OneToMany(cascade={CascadeType.ALL}) 
	@JoinColumn(name = "parent_id") 
	@JsonManagedReference("workDirectiveMaterialSpecificationProperty-parent-child")
	private Set<WorkDirectiveMaterialSpecificationProperty> properties = new HashSet<WorkDirectiveMaterialSpecificationProperty>();
	/*----------------------------------------------------------------------------*/
	
	public WorkDirectiveMaterialSpecificationProperty() { }
	
	public WorkDirectiveMaterialSpecificationProperty clone(){
		WorkDirectiveMaterialSpecificationProperty obj = new WorkDirectiveMaterialSpecificationProperty();
		obj.setCode(this.getCode());
		obj.setDescription(this.getDescription());
		obj.setEnumeration(this.getEnumeration());
		obj.setUnitOfMeasure(this.getUnitOfMeasure());
		obj.setValue(this.getValue());
		obj.setValueType(this.getValueType());
		Set<WorkDirectiveMaterialSpecificationProperty> propertySet = new HashSet<WorkDirectiveMaterialSpecificationProperty>();
		for(WorkDirectiveMaterialSpecificationProperty property : this.properties){
			WorkDirectiveMaterialSpecificationProperty newProperty = property.clone();
			newProperty.setParent(obj);
			propertySet.add(newProperty);
		}
		obj.setProperties(propertySet);
		return obj;
	}

	public WorkDirectiveMaterialSpecificationProperty getProperty(String code) {
		for (WorkDirectiveMaterialSpecificationProperty workDirectiveMaterialSpecificationProperty: this.properties) {
			if (workDirectiveMaterialSpecificationProperty.getCode().equals(code)) {
				return workDirectiveMaterialSpecificationProperty;
			}
		}
		return null;
	}

	public Collection<WorkDirectiveMaterialSpecificationProperty> getProperties(String code) {
		Collection<WorkDirectiveMaterialSpecificationProperty> properties = Collections.emptyList();
		for (WorkDirectiveMaterialSpecificationProperty workDirectiveMaterialSpecificationProperty: this.properties) {
			if (workDirectiveMaterialSpecificationProperty.getCode().equals(code)) {
				properties.add(workDirectiveMaterialSpecificationProperty);
			}
		}
		return properties;
	}
}
