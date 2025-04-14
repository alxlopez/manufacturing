package com.mes.dom.workSchedule;

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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mes.dom.enumerations.application.AssemblyRelationShip;
import com.mes.dom.enumerations.application.AssemblyType;
import com.mes.dom.enumerations.application.MaterialUse;
import com.mes.dom.equipment.Equipment;
import com.mes.dom.material.MaterialClass;
import com.mes.dom.material.MaterialDefinition;
import com.mes.dom.material.MaterialLot;
import com.mes.dom.material.MaterialSubLot;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"jobOrder", "parent", "properties", "materialRequirements"})
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
@Table(name="schedule_job_order_material_requirement")
public class JobOrderMaterialRequirement implements Serializable {
	private static final long serialVersionUID = -7160321360980008490L;
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable=true,length=70)
	private String code;
	@ManyToOne
	@JoinColumn(name = "jobOrder", referencedColumnName="id")
	@JsonBackReference("jobOrder-jobOrdeMatReq")
	private JobOrder jobOrder;
	private String hierarchyScope;
	@OneToOne
	@JoinColumn(name="location",referencedColumnName="id")
	private Equipment location;
	@OneToOne
	@JoinColumn(name="materialClass",referencedColumnName="id")
	private MaterialClass materialClass;
	@OneToOne
	@JoinColumn(name="materialDefinition",referencedColumnName="id")
	private MaterialDefinition materialDefinition;
	@OneToOne
	@JoinColumn(name="materialLot",referencedColumnName="id")
	private MaterialLot materialLot;
	@OneToOne
	@JoinColumn(name="materialSublot",referencedColumnName="id")
	private MaterialSubLot materialSubLot;
	@Column(columnDefinition = "mediumtext")
	private String description;
	@Enumerated(EnumType.STRING)
	private MaterialUse materialUse;
	@OneToOne
	@JoinColumn(name="storageLocation",referencedColumnName="id")
	private Equipment storageLocation;
	@Column(precision=8, scale=2)
	private Float quantity;	
	private String unitOfMeasure;
	@Enumerated(EnumType.STRING)
	private AssemblyType assemblyType;
	@Enumerated(EnumType.STRING)
	private AssemblyRelationShip assemblyRelationShip;
	@OneToMany(mappedBy = "jobOrderMaterialRequirement", cascade =CascadeType.ALL, orphanRemoval=true)
	@JsonManagedReference("jobOrderMaterialRequirement-property")
	private Collection<JobOrderMaterialRequirementProperty> properties= new HashSet<JobOrderMaterialRequirementProperty>() ;
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=true,updatable=true) 
	@JsonBackReference("jobOrderMaterialRequirement-parent-child")
	private JobOrderMaterialRequirement parent;
	@OneToMany(cascade={CascadeType.ALL},orphanRemoval=true ) 
	@JoinColumn(name = "parent_id", insertable = true, updatable = false) 
	@JsonManagedReference("jobOrderMaterialRequirement-parent-child")
	private Set<JobOrderMaterialRequirement> materialRequirements = new HashSet<JobOrderMaterialRequirement>();
	/*----------------------------------------------------------------------------*/

	protected JobOrderMaterialRequirement() { }

	public JobOrderMaterialRequirement(String code, String materialDefinition, float quantity, JobOrder jobOrder) {
		super();
		this.code = code;
		this.quantity = quantity;
		this.jobOrder= jobOrder;
	}

	public JobOrderMaterialRequirement(String code, String materialDefinition, float quantity) {
		super();
		this.code = code;
		this.quantity = quantity;
	}
	
	public JobOrderMaterialRequirement getMaterialRequirement(String code) {
		for (JobOrderMaterialRequirement materialRequirement: this.materialRequirements) {
			if (materialRequirement.getCode().equals(code)) {
				return materialRequirement;
			}
		}
		return null;
	}

	public List<JobOrderMaterialRequirement> getMaterialRequirements(String code) {
		ArrayList<JobOrderMaterialRequirement> materialRequirements = new ArrayList<JobOrderMaterialRequirement>();
		for (JobOrderMaterialRequirement materialRequirement: this.materialRequirements) {
			if (materialRequirement.getCode().equals(code)) {
				materialRequirements.add(materialRequirement);
			}
		}
		return materialRequirements;
	}

	public JobOrderMaterialRequirementProperty getProperty(String code) {
		for (JobOrderMaterialRequirementProperty jobOrderMaterialRequirementProperty: this.properties) {
			if (jobOrderMaterialRequirementProperty.getCode().equals(code)) {
				return jobOrderMaterialRequirementProperty;
			}
		}
		return null;
	}

	public List<JobOrderMaterialRequirementProperty> getProperties(String code) {
		ArrayList<JobOrderMaterialRequirementProperty> properties = new ArrayList<JobOrderMaterialRequirementProperty>();
		for (JobOrderMaterialRequirementProperty jobOrderMaterialRequirementProperty: this.properties) {
			if (jobOrderMaterialRequirementProperty.getCode().equals(code)) {
				properties.add(jobOrderMaterialRequirementProperty);
			}
		}
		return properties;
	}

	public void addMaterialRequirement(JobOrderMaterialRequirement materialRequirement) {
		materialRequirement.setParent(this);
		this.materialRequirements.add(materialRequirement);
	}

	public void addProperty(JobOrderMaterialRequirementProperty property) {
		property.setJobOrderMaterialRequirement(this);
		this.properties.add(property);
	}

	public void addProperty(String code, String value) {
		JobOrderMaterialRequirementProperty property = new JobOrderMaterialRequirementProperty();
		property.setCode(code);
		property.setValue(value);
		property.setJobOrderMaterialRequirement(this);
		this.properties.add(property);
	}
}
