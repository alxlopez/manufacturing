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
import com.mes.dom.common.Parameter;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
@EqualsAndHashCode(exclude={"parameters"}, callSuper=true)
//@AttributeOverride(name = "code", column = @Column(nullable=false))
public class WorkMasterParameter extends Parameter implements Serializable{
	private static final long serialVersionUID = 913101917419765703L;
	@ManyToOne
	@JoinColumn(name="workMaster", referencedColumnName="id")
	@JsonBackReference("workMaster-parameter")	
	private WorkMaster workMaster; 	
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=false,updatable=true) 
	@JsonBackReference("workDefinitionParameter-parent-child")
	private WorkMasterParameter parent;
	@OneToMany(cascade={CascadeType.ALL}) 
	@JoinColumn(name = "parent_id") 
	@JsonManagedReference("workDefinitionParameter-parent-child")
	private Set<WorkMasterParameter> parameters = new HashSet<WorkMasterParameter>();
	/*----------------------------------------------------------------------------*/	

	public WorkMasterParameter() { }

	public WorkMasterParameter getParameter(String code) {
		for (WorkMasterParameter workMasterParameter: this.parameters) {
			if (workMasterParameter.getCode().equals(code)) {
				return workMasterParameter;
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

	public void addParameter(WorkMasterParameter parameter) {
		parameter.setParent(this);
		this.parameters.add(parameter);
	}

	public void addParameter(String code, String value) {
		WorkMasterParameter parameter = new WorkMasterParameter();
		parameter.setCode(code);
		parameter.setValue(value);
		parameter.setParent(this);
		this.parameters.add(parameter);
	}
}
