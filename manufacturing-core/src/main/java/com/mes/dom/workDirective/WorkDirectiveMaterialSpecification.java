package com.mes.dom.workDirective;

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
@EqualsAndHashCode(exclude={"materialClass","materialDefinition","workDirective","properties","parent","specifications"})
public class WorkDirectiveMaterialSpecification implements Serializable, Cloneable{
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
	@JoinColumn(name="workDirective", referencedColumnName="id")
	@JsonBackReference("workDirective-matSpec")	
	private WorkDirective workDirective; 
	@OneToMany(mappedBy="workDirectiveMaterialSpecification", cascade = CascadeType.ALL,orphanRemoval=true)
	@JsonManagedReference("workDirectiveMaterialSpecificationProperty")
	private Collection<WorkDirectiveMaterialSpecificationProperty> properties = new HashSet<WorkDirectiveMaterialSpecificationProperty>();
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=true,updatable=true)
	@JsonBackReference("workDirectiveMaterialSpecification-parent-child")
	private WorkDirectiveMaterialSpecification parent;
	@OneToMany(cascade={CascadeType.PERSIST}) 
	@JoinColumn(name = "parent_id", insertable = true, updatable = false) 
	@JsonManagedReference("workDirectiveMaterialSpecification-parent-child")
	private Set<WorkDirectiveMaterialSpecification> specifications = new HashSet<WorkDirectiveMaterialSpecification>();
	/*----------------------------------------------------------------------------*/
	
	public WorkDirectiveMaterialSpecification() { }

	public WorkDirectiveMaterialSpecification clone(){
		WorkDirectiveMaterialSpecification obj = new WorkDirectiveMaterialSpecification();
		obj.setAssemblyRelationShip(this.assemblyRelationShip);
		obj.setAssemblyType(this.assemblyType);
    	obj.setCode(this.code);
    	obj.setDescription(this.description);
    	obj.setMaterialClass(this.materialClass);
    	obj.setMaterialDefinition(this.materialDefinition);
    	obj.setMaterialUse(this.materialUse);
    	obj.setQuantity(this.quantity);
    	obj.setUnitOfMeasure(this.unitOfMeasure);
    	obj.setValueType(this.valueType);
    	Collection<WorkDirectiveMaterialSpecificationProperty> propertyCollection = new HashSet<WorkDirectiveMaterialSpecificationProperty>();
    	for(WorkDirectiveMaterialSpecificationProperty property : this.properties){
    		WorkDirectiveMaterialSpecificationProperty newProperty = property.clone();
    		newProperty.setWorkDirectiveMaterialSpecification(obj);
    		propertyCollection.add(newProperty);
    	}
    	obj.setProperties(propertyCollection);
    	Set<WorkDirectiveMaterialSpecification> specificationSet = new HashSet<WorkDirectiveMaterialSpecification>();
    	for(WorkDirectiveMaterialSpecification specification : this.specifications){
    		WorkDirectiveMaterialSpecification newSpecification = specification.clone();
    		newSpecification.setParent(obj);
    		specificationSet.add(newSpecification);
    	}
    	obj.setSpecifications(specificationSet);
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
