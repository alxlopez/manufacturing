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
import com.mes.dom.common.Parameter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
@EqualsAndHashCode(exclude={"parameters"}, callSuper=true)
//@AttributeOverride(name = "code", column = @Column(nullable=false))
public class WorkDirectiveParameter extends Parameter implements Serializable, Cloneable{
	private static final long serialVersionUID = 913101917419765703L;
	@ManyToOne
	@JoinColumn(name="workDirective", referencedColumnName="id")
	@JsonBackReference("workDirective-parameter")	
	private WorkDirective workDirective; 	
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=false,updatable=true) 
	@JsonBackReference("workDefinitionParameter-parent-child")
	private WorkDirectiveParameter parent;
	@OneToMany(cascade={CascadeType.ALL}) 
	@JoinColumn(name = "parent_id") 
	@JsonManagedReference("workDefinitionParameter-parent-child")
	private Set<WorkDirectiveParameter> parameters = new HashSet<WorkDirectiveParameter>();
	/*----------------------------------------------------------------------------*/	

	public WorkDirectiveParameter() { }
	
	public WorkDirectiveParameter clone(){
		WorkDirectiveParameter obj = new WorkDirectiveParameter();
		obj.setCode(this.getCode());
		obj.setDescription(this.getDescription());
		obj.setEnumeration(this.getEnumeration());
		obj.setUnitOfMeasure(this.getUnitOfMeasure());
		obj.setValue(this.getValue());
		obj.setValueType(this.getValueType());
		Set<WorkDirectiveParameter> parametersSet = new HashSet<WorkDirectiveParameter>();
		for(WorkDirectiveParameter parameter : this.parameters){
			WorkDirectiveParameter newParameter = parameter.clone();
			newParameter.setParent(obj);
			parametersSet.add(newParameter);
		}
		obj.setParameters(parametersSet);
		return obj;
	}

	public WorkDirectiveParameter getParameter(String code) {
		for (WorkDirectiveParameter workDirectiveParameter: this.parameters) {
			if (workDirectiveParameter.getCode().equals(code)) {
				return workDirectiveParameter;
			}
		}
		return null;
	}

	public Collection<WorkDirectiveParameter> getParameters(String code) {
		Collection<WorkDirectiveParameter> parameters = Collections.emptyList();
		for (WorkDirectiveParameter workDirectiveParameter: this.parameters) {
			if (workDirectiveParameter.getCode().equals(code)) {
				parameters.add(workDirectiveParameter);
			}
		}
		return parameters;
	}
}
