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
import com.mes.dom.enumerations.application.MaterialUse;
import com.mes.dom.enumerations.application.WorkTypes;
import com.mes.dom.equipment.Equipment;
import com.mes.dom.material.MaterialDefinitionProperty;
import com.mes.dom.workSchedule.JobOrder;

import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "workDirectives" })
@Entity
@Data
@EqualsAndHashCode(exclude = { "parameters", "materialSpecifications", "workDirectives" })
public class WorkDirective implements Serializable, Cloneable{
	private static final long serialVersionUID = -7048273554367359281L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	@Column(nullable = true, length = 70)
	private String code;
	@Column(columnDefinition = "mediumtext")
	private String description;	
	@Enumerated(EnumType.STRING)
	private WorkTypes workType;
	@OneToOne
	@JoinColumn(name = "hierarchyScope", referencedColumnName = "id")
	private Equipment hierarchyScope;
	@ManyToOne()
	@JoinColumn(name="jobOrder",referencedColumnName="id")
	private JobOrder jobOrder;	
	private String workflowSpecificationId;	
	@OneToMany(mappedBy = "workDirective", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("workDirective-parameter")
	private Collection<WorkDirectiveParameter> parameters = new HashSet<WorkDirectiveParameter>();
	@OneToMany(mappedBy = "workDirective", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("workDirective-matSpec")
	private Collection<WorkDirectiveMaterialSpecification> materialSpecifications = new HashSet<WorkDirectiveMaterialSpecification>();
	@ManyToOne
	@JoinColumn(name="workMaster", referencedColumnName="id")
	@JsonBackReference("workMaster-workDirective")
	private WorkMaster workMaster;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", insertable = true, updatable = true)
	@JsonBackReference("workDirective-parent-child")
	private WorkDirective parent;
	@OneToMany(cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "parent_id", insertable = true, updatable = false)
	@JsonManagedReference("workDirective-parent-child")
	private Set<WorkDirective> workDirectives = new HashSet<WorkDirective>();

	public WorkDirective() { }

	public WorkDirective clone() {
		WorkDirective obj = new WorkDirective();
        obj.setCode(this.code);
        obj.setDescription(this.description);
        obj.setHierarchyScope(this.hierarchyScope);
        obj.setJobOrder(this.jobOrder);
        obj.setWorkMaster(this.workMaster);
        obj.setWorkType(this.workType);
        List<WorkDirectiveMaterialSpecification> materialSpecificationList = new ArrayList<WorkDirectiveMaterialSpecification>();
        for(WorkDirectiveMaterialSpecification materialSpecification: this.materialSpecifications){
        	WorkDirectiveMaterialSpecification newMaterialSpecification = materialSpecification.clone();
        	newMaterialSpecification.setWorkDirective(obj);
        	materialSpecificationList.add(newMaterialSpecification);
        }
        obj.setMaterialSpecifications(materialSpecificationList);
        Collection<WorkDirectiveParameter> parameterCollection = new HashSet<WorkDirectiveParameter>();
        for(WorkDirectiveParameter parameter: this.parameters){
        	WorkDirectiveParameter newParameter = parameter.clone();
        	newParameter.setWorkDirective(obj);
        	parameterCollection.add(newParameter);
        }
        obj.setParameters(parameterCollection);
        Set<WorkDirective> workDirectiveSet = new HashSet<WorkDirective>();
        for(WorkDirective workDirective: this.workDirectives){
        	WorkDirective newWorkDirective = workDirective.clone();
        	newWorkDirective.setParent(obj);
        	workDirectiveSet.add(newWorkDirective);
        }
        obj.setWorkDirectives(workDirectiveSet);
        return obj;
    }

	public WorkDirectiveParameter addParameter(String code, String value){
		WorkDirectiveParameter newParameter = new WorkDirectiveParameter();
		newParameter.setCode(code);
		newParameter.setValue(value);
		newParameter.setWorkDirective(this);
		this.parameters.add(newParameter);
		return newParameter;
	}

	public void removeParameter(String code) {
		List<WorkDirectiveParameter> parameters = this.getParameters(code);
		for (WorkDirectiveParameter parameter: parameters) {
			this.parameters.remove(parameter);
		}
	}

	public WorkDirectiveParameter getParameter(String code) {
		for (WorkDirectiveParameter workDirectiveParameter: this.parameters) {
			if (workDirectiveParameter.getCode().equals(code)) {
				return workDirectiveParameter;
			}
		}
		return null;
	}

	public List<WorkDirectiveParameter> getParameters(String code) {
		ArrayList<WorkDirectiveParameter> parameters = new ArrayList<WorkDirectiveParameter>();
		for (WorkDirectiveParameter workDirectiveParameter: this.parameters) {
			if (workDirectiveParameter.getCode().equals(code)) {
				parameters.add(workDirectiveParameter);
			}
		}
		return parameters;
	}

	//Get materialSpecifications by MaterialUse	
	public List<WorkDirectiveMaterialSpecification> getMaterialSpecifications(MaterialUse materialUse) {
		ArrayList<WorkDirectiveMaterialSpecification> materialSpecifications = new ArrayList<WorkDirectiveMaterialSpecification>();
		for (WorkDirectiveMaterialSpecification workDirectiveMaterialSpecification: this.materialSpecifications) {
			if (workDirectiveMaterialSpecification.getMaterialUse().equals(materialUse)) {
					materialSpecifications.add(workDirectiveMaterialSpecification);
			}
		}
		return materialSpecifications;
	}

	public WorkDirectiveMaterialSpecification getMaterialSpecificationByMaterialDefinition(MaterialUse materialUse, String materialDefinitionCode) {
		for (WorkDirectiveMaterialSpecification workDirectiveMaterialSpecification: this.materialSpecifications) {
			if (workDirectiveMaterialSpecification.getMaterialUse().equals(materialUse)) {
				if (workDirectiveMaterialSpecification.getMaterialDefinition().getCode().equals(materialDefinitionCode)) {
					return workDirectiveMaterialSpecification;
				}
			}
		}
		return null;
	}

	public WorkDirectiveMaterialSpecification getMaterialSpecificationByMaterialDefinition(String materialDefinitionCode){
		for (WorkDirectiveMaterialSpecification workDirectiveMaterialSpecification: this.materialSpecifications) {
			if (workDirectiveMaterialSpecification.getMaterialDefinition().getCode().equals(materialDefinitionCode)) {
					return workDirectiveMaterialSpecification;
			}
		}
		return null;
	}

	public WorkDirectiveMaterialSpecification getMaterialSpecificationByMaterialProperty(String code, String value){
		for (WorkDirectiveMaterialSpecification workDirectiveMaterialSpecification: this.materialSpecifications) {
			MaterialDefinitionProperty materialDefinitionProperty = workDirectiveMaterialSpecification.getMaterialDefinition().getProperty(code);
			if(materialDefinitionProperty != null)
			{
				if (materialDefinitionProperty.getValue().equals(value)) {
						return workDirectiveMaterialSpecification;
				}
			}
		}
		return null;
	}
}
