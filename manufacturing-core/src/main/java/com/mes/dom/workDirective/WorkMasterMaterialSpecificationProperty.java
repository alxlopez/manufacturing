package com.mes.dom.workDirective;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
public class WorkMasterMaterialSpecificationProperty extends Property implements Serializable {
	private static final long serialVersionUID = -2816587132964063967L;
	@ManyToOne
	@JoinColumn(name="workMasterMaterialSpecification", referencedColumnName="id")
	@JsonBackReference("workMasterMaterialSpecificationProperty")	
	private WorkMasterMaterialSpecification workMasterMaterialSpecification; 
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=false,updatable=true) 
	@JsonBackReference("workMasterMaterialSpecificationProperty-parent-child")
	private WorkMasterMaterialSpecificationProperty parent;
	@OneToMany(cascade={CascadeType.ALL}) 
	@JoinColumn(name = "parent_id") 
	@JsonManagedReference("workMasterMaterialSpecificationProperty-parent-child")
	private Set<WorkMasterMaterialSpecificationProperty> properties = new HashSet<WorkMasterMaterialSpecificationProperty>();
	/*----------------------------------------------------------------------------*/	
	
	public WorkMasterMaterialSpecificationProperty() { }
	
	public WorkMasterMaterialSpecificationProperty getProperty(String code) {
		for (WorkMasterMaterialSpecificationProperty workMasterMaterialSpecificationProperty: this.properties) {
			if (workMasterMaterialSpecificationProperty.getCode().equals(code)) {
				return workMasterMaterialSpecificationProperty;
			}
		}
		return null;
	}

	public List<WorkMasterMaterialSpecificationProperty> getProperties(String code) {
		ArrayList<WorkMasterMaterialSpecificationProperty> properties = new ArrayList<WorkMasterMaterialSpecificationProperty>();
		for (WorkMasterMaterialSpecificationProperty workMasterMaterialSpecificationProperty: this.properties) {
			if (workMasterMaterialSpecificationProperty.getCode().equals(code)) {
				properties.add(workMasterMaterialSpecificationProperty);
			}
		}
		return properties;
	}

	public void addProperty(WorkMasterMaterialSpecificationProperty property) {
		property.setParent(this);
		this.properties.add(property);
	}

	public void addProperty(String code, String value) {
		WorkMasterMaterialSpecificationProperty property = new WorkMasterMaterialSpecificationProperty();
		property.setCode(code);
		property.setValue(value);
		property.setParent(this);
		this.properties.add(property);
	}
}
