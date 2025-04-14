package com.mes.dom.workDirective;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mes.dom.enumerations.application.AssemblyRelationShip;
import com.mes.dom.enumerations.application.AssemblyType;
import com.mes.dom.enumerations.application.MaterialUse;
import com.mes.dom.enumerations.application.ValueType;
import com.mes.dom.material.MaterialClass;
import com.mes.dom.material.MaterialDefinition;

import lombok.Data;
import lombok.EqualsAndHashCode;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
@EqualsAndHashCode(exclude={"materialClass","materialDefinition","workMaster","properties","parent","specifications"})
public class WorkMasterMaterialSpecification implements Serializable {
	private static final long serialVersionUID = 4682229717536285194L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=true,length=70)
	private String code;
	@Column(columnDefinition = "mediumtext")
	private String description;
	@Enumerated (EnumType.STRING)
	private ValueType valueType;
	@OneToOne
	@JoinColumn(name="materialClass",referencedColumnName="id")
	private MaterialClass materialClass;
	@OneToOne
	@JoinColumn(name="materialDefinition",referencedColumnName="id")
	private MaterialDefinition materialDefinition;
	@Enumerated (EnumType.STRING)	
	private MaterialUse materialUse;
	@Column(precision=8, scale=2)
	private Float quantity;
	private String unitOfMeasure;
	@Enumerated(EnumType.STRING)
	private AssemblyType assemblyType;
	@Enumerated(EnumType.STRING)
	private AssemblyRelationShip assemblyRelationShip;
	@ManyToOne
	@JoinColumn(name="workMaster", referencedColumnName="id")
	@JsonBackReference("workMaster-matSpec")	
	private WorkMaster workMaster; 
	@OneToMany(mappedBy="workMasterMaterialSpecification", cascade = CascadeType.ALL,orphanRemoval=true)
	@JsonManagedReference("workMasterMaterialSpecificationProperty")
	private Collection<WorkMasterMaterialSpecificationProperty> properties = new HashSet<WorkMasterMaterialSpecificationProperty>();
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=true,updatable=true)
	@JsonBackReference("workMasterMaterialSpecification-parent-child")
	private WorkMasterMaterialSpecification parent;
	@OneToMany(cascade={CascadeType.PERSIST}) 
	@JoinColumn(name = "parent_id", insertable = true, updatable = false) 
	@JsonManagedReference("workMasterMaterialSpecification-parent-child")
	private Set<WorkMasterMaterialSpecification> specifications = new HashSet<WorkMasterMaterialSpecification>();
	/*----------------------------------------------------------------------------*/

	public WorkMasterMaterialSpecification() { }

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

	public WorkMasterMaterialSpecification getSpecification(String code) {
		for (WorkMasterMaterialSpecification workMasterMaterialSpecification: this.specifications) {
			if (workMasterMaterialSpecification.getCode().equals(code)) {
				return workMasterMaterialSpecification;
			}
		}
		return null;
	}

	public List<WorkMasterMaterialSpecification> getSpecifications(String code) {
		ArrayList<WorkMasterMaterialSpecification> properties = new ArrayList<WorkMasterMaterialSpecification>();
		for (WorkMasterMaterialSpecification workMasterMaterialSpecification: this.specifications) {
			if (workMasterMaterialSpecification.getCode().equals(code)) {
				properties.add(workMasterMaterialSpecification);
			}
		}
		return properties;
	}

	public void addProperty(WorkMasterMaterialSpecificationProperty property) {
		property.setWorkMasterMaterialSpecification(this);
		this.properties.add(property);
	}

	public void addProperty(String code, String value) {
		WorkMasterMaterialSpecificationProperty property = new WorkMasterMaterialSpecificationProperty();
		property.setCode(code);
		property.setValue(value);
		property.setWorkMasterMaterialSpecification(this);
		this.properties.add(property);
	}

	public void addSpecification(WorkMasterMaterialSpecification specification) {
		specification.setParent(this);
		this.specifications.add(specification);
	}
}
