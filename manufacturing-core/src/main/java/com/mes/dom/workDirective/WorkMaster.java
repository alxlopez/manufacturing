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
import com.mes.dom.workSchedule.JobOrder;

import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "workMasters" })
@Entity
@Data
@EqualsAndHashCode(exclude = { "parameters", "materialSpecifications", "workMasters", "workDirectives"})
public class WorkMaster implements Serializable {
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
	private String workflowSpecificationId;	
	@OneToMany(mappedBy = "workMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("workMaster-parameter")
	private Collection<WorkMasterParameter> parameters = new HashSet<WorkMasterParameter>();
	@OneToMany(mappedBy = "workMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("workMaster-matSpec")
	private Collection<WorkMasterMaterialSpecification> materialSpecifications = new HashSet<WorkMasterMaterialSpecification>();
	@OneToMany(mappedBy = "workMaster")
	@JsonManagedReference("workMaster-workDirective")
	private Collection<WorkDirective> workDirectives = new HashSet<WorkDirective>();
	@OneToMany(mappedBy = "workMaster")
	@JsonManagedReference("workMaster-jobOrd")
	private Collection<JobOrder> jobOrders = new HashSet<JobOrder>();
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", insertable = true, updatable = true)
	@JsonBackReference("workMaster-parent-child")
	private WorkMaster parent;
	@OneToMany(cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "parent_id", insertable = true, updatable = false)
	@JsonManagedReference("workMaster-parent-child")
	private Set<WorkMaster> workMasters = new HashSet<WorkMaster>();
	/*----------------------------------------------------------------------------*/

	public WorkMaster() { }
	
	public WorkMasterParameter addParameter(String code, String value){
		WorkMasterParameter newParameter = new WorkMasterParameter();
		newParameter.setCode(code);
		newParameter.setValue(value);
		newParameter.setWorkMaster(this);
		this.parameters.add(newParameter);
		return newParameter;
	}

	public void addParameter(WorkMasterParameter newParameter){
		newParameter.setWorkMaster(this);
		this.parameters.add(newParameter);
	}

	public void addMaterialSpecification(WorkMasterMaterialSpecification materialSpecification){
		materialSpecification.setWorkMaster(this);
		this.materialSpecifications.add(materialSpecification);
	}

	public void addWorkDirective(WorkDirective workDirective){
		workDirective.setWorkMaster(this);
		this.workDirectives.add(workDirective);
	}

	public void addJobOrder(JobOrder jobOrder){
		jobOrder.setWorkMaster(this);
		this.jobOrders.add(jobOrder);
	}

	public void addWorkMaster(WorkMaster workMaster){
		workMaster.setParent(this);
		this.workMasters.add(workMaster);
	}

	public void removeParameter(String code) {
		List<WorkMasterParameter> parameters = this.getParameters(code);
		for (WorkMasterParameter parameter: parameters) {
			this.parameters.remove(parameter);
		}
	}

	public void removeWorkDirective(String code) {
		List<WorkDirective> workDirectives = this.getWorkDirectives(code);
		for (WorkDirective workDirective: workDirectives) {
			this.workDirectives.remove(workDirective);
		}
	}

	public WorkMasterParameter getParameter(String code) {
		for (WorkMasterParameter workMasterParameter: this.parameters) {
			if (workMasterParameter.getCode().equals(code)) {
				return workMasterParameter;
			}
		}
		return null;
	}

	public WorkDirective getWorkDirective(String code) {
		for (WorkDirective workDirective: this.workDirectives) {
			if (workDirective.getCode().equals(code)) {
				return workDirective;
			}
		}
		return null;
	}

	public List<WorkMasterParameter> getParameters(String code) {
		ArrayList<WorkMasterParameter> parameters = new ArrayList<WorkMasterParameter>();
		for (WorkMasterParameter workMasterParameter: this.parameters) {
			if (workMasterParameter.getCode().equals(code)) {
				parameters.add(workMasterParameter);
			}
		}
		return parameters;
	}

	public List<WorkDirective> getWorkDirectives(String code) {
		ArrayList<WorkDirective> workDirectives = new ArrayList<WorkDirective>();
		for (WorkDirective workDirective: this.workDirectives) {
			if (workDirective.getCode().equals(code)) {
				workDirectives.add(workDirective);
			}
		}
		return workDirectives;
	}

	//Get materialSpecifications by MaterialUse	
	public List<WorkMasterMaterialSpecification> getMaterialSpecifications(MaterialUse materialUse) {
		ArrayList<WorkMasterMaterialSpecification> materialSpecifications = new ArrayList<WorkMasterMaterialSpecification>();
		for (WorkMasterMaterialSpecification workMasterMaterialSpecification: this.materialSpecifications) {
			if (workMasterMaterialSpecification.getMaterialUse().equals(materialUse)) {
					materialSpecifications.add(workMasterMaterialSpecification);
			}
		}
		return materialSpecifications;
	}

	public WorkMasterMaterialSpecification getMaterialSpecificationByMaterialDefinition(MaterialUse materialUse, String materialDefinitionCode) {
		for (WorkMasterMaterialSpecification workMasterMaterialSpecification: this.materialSpecifications) {
			if (workMasterMaterialSpecification.getMaterialUse().equals(materialUse)) {
				if (workMasterMaterialSpecification.getMaterialDefinition().getCode().equals(materialDefinitionCode)) {
					return workMasterMaterialSpecification;
				}
			}
		}
		return null;
	}

	public WorkMasterMaterialSpecification getMaterialSpecificationByMaterialDefinition(String materialDefinitionCode){
		for (WorkMasterMaterialSpecification workMasterMaterialSpecification: this.materialSpecifications) {
			if (workMasterMaterialSpecification.getMaterialDefinition().getCode().equals(materialDefinitionCode)) {
					return workMasterMaterialSpecification;
			}
		}
		return null;
	}
}
