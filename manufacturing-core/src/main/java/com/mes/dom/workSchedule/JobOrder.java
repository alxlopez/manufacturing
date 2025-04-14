package com.mes.dom.workSchedule;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.enumerations.application.DispatchStatus;
import com.mes.dom.enumerations.application.Priority;
import com.mes.dom.enumerations.application.WorkTypes;
import com.mes.dom.equipment.Equipment;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.dom.workDirective.WorkMaster;

import lombok.Data;
import lombok.EqualsAndHashCode;;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"parameters", "materialRequirements"})
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
@Table(name="schedule_job_order")
public class JobOrder implements Serializable {
	private static final long serialVersionUID = 4248600541715458704L;
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column(name="id")	
	private Long id;
	@Column(nullable=true,length=70)
	private String code;
	@Column(columnDefinition = "mediumtext")
	private String description;
	@OneToOne
	@JoinColumn(name="location",referencedColumnName="id")
	private Equipment location;
	private String hierarchyScope;
	@Enumerated(EnumType.STRING)
	private WorkTypes workType;
	private Long workMasterVersion;
	private Timestamp startTime;
	private Timestamp endTime;
	@Enumerated(EnumType.STRING)
	private Priority priority;
	@Enumerated(EnumType.STRING)
	private Commands command;
	// Enumeration ??
	private String commandRule;
	@Enumerated(EnumType.STRING)
	private DispatchStatus dispatchStatus;	
	@OneToMany(mappedBy = "jobOrder",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
	@JsonManagedReference("jobOrder-jobOrderParameter")
	private Collection<JobOrderParameter> parameters  = new HashSet<JobOrderParameter>();
	@OneToMany( mappedBy = "jobOrder",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval=true)
	@JsonManagedReference("jobOrder-jobOrdeMatReq")
	private Collection<JobOrderMaterialRequirement> materialRequirements= new HashSet<JobOrderMaterialRequirement>();
	@OneToMany( mappedBy = "jobOrder",fetch = FetchType.LAZY)
	@JsonIgnore // jobOrder could not specify workDirectives, instead workdirective specified to which jobOrder it belongs to
	private Collection<WorkDirective> workDirectives  = new HashSet<WorkDirective>();
	@ManyToOne
	@JoinColumn(name = "workRequest", referencedColumnName="id")
	@JsonBackReference("workRequest")
	private WorkRequest workRequest;
	@ManyToOne
	@JoinColumn(name = "jobOrderList", referencedColumnName="id")
	@JsonBackReference("jobOrderList")
	private JobOrderList jobOrderList;
	@ManyToOne
	@JoinColumn(name="workMaster", referencedColumnName="id")
	@JsonBackReference("workMaster-jobOrd")
	private WorkMaster workMaster; 

	public JobOrder() { }

	protected JobOrder(Long id) {
		this.id = id; 
	}   
	public JobOrder(String code) {
		super();
		this.code = code;
	}

	public JobOrderParameter getParameter(String code) {
		for (JobOrderParameter jobOrderParameter: this.parameters) {
			if (jobOrderParameter.getCode().equals(code)) {
				return jobOrderParameter;
			}
		}
		return null;
	}

	public List<JobOrderParameter> getParameters(String code) {
		ArrayList<JobOrderParameter> parameters = new ArrayList<JobOrderParameter>();
		for (JobOrderParameter jobOrderParameter: this.parameters) {
			if (jobOrderParameter.getCode().equals(code)) {
				parameters.add(jobOrderParameter);
			}
		}
		return parameters;
	}

	public WorkDirective getWorkDirective(String code) {
		for (WorkDirective workDirective: this.workDirectives) {
			if (workDirective.getCode().equals(code)) {
				return workDirective;
			}
		}
		return null;
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

	public List<WorkDirective> getWorkDirectivesByWorkType(WorkTypes workType) {
		ArrayList<WorkDirective> workDirectives = new ArrayList<WorkDirective>();
		for (WorkDirective workDirective: this.workDirectives) {
			if (workDirective.getWorkType().equals(workType)) {
				workDirectives.add(workDirective);
			}
		}
		return workDirectives;
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

	public void addParameter(JobOrderParameter parameter) {
		parameter.setJobOrder(this);
		this.parameters.add(parameter);
	}

	public void addParameter(String code, String value) {
		JobOrderParameter parameter = new JobOrderParameter();
		parameter.setCode(code);
		parameter.setValue(value);
		parameter.setJobOrder(this);
		this.parameters.add(parameter);
	}

	public void addWorkDirective(WorkDirective workDirective) {
		workDirective.setJobOrder(this);
		this.workDirectives.add(workDirective);
	}

	public void addMaterialRequirements(JobOrderMaterialRequirement materialRequirement) {
		materialRequirement.setJobOrder(this);
		this.materialRequirements.add(materialRequirement);
	}
}
