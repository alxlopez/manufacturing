package com.mes.dom.workSchedule;

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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mes.dom.common.Parameter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"parameters"}, callSuper=true)
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
@Table(name="schedule_job_order_parameter")
//@AttributeOverride(name = "code", column = @Column(nullable=false))
public class JobOrderParameter extends Parameter implements Serializable {
	private static final long serialVersionUID = 1025695716545908049L;
	@ManyToOne
	@JoinColumn(name = "jobOrder", referencedColumnName="id")
	@JsonBackReference("jobOrder-jobOrderParameter")
	private JobOrder jobOrder;
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=false,updatable=true) 
	@JsonBackReference("jobOrderParameter-parent-child")
	private JobOrderParameter parent;
	@OneToMany(cascade={CascadeType.ALL},orphanRemoval=true) 
	@JoinColumn(name = "parent_id") 
	@JsonManagedReference("jobOrderParameter-parent-child")
	private Set<JobOrderParameter> parameters = new HashSet<JobOrderParameter>();
	/*----------------------------------------------------------------------------*/

	public JobOrderParameter() { }

	public JobOrderParameter(String code, String value, JobOrder jobOrder) {
		this.setCode(code);
		this.setValue(value);
		this.jobOrder = jobOrder;
	}

	public JobOrderParameter(String code, String value) {
		this.setCode(code);
		this.setValue(value);
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

	public void addParameter(JobOrderParameter parameter) {
		parameter.setParent(this);
		this.parameters.add(parameter);
	}

	public void addParameter(String code, String value) {
		JobOrderParameter parameter = new JobOrderParameter();
		parameter.setCode(code);
		parameter.setValue(value);
		parameter.setParent(this);
		this.parameters.add(parameter);
	}
}
