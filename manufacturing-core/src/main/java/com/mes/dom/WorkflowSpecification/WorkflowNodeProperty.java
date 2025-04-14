package com.mes.dom.WorkflowSpecification;

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
public class WorkflowNodeProperty extends Property implements Serializable{
	private static final long serialVersionUID = -7780719075677175570L;
	@ManyToOne
	@JoinColumn(name="workflowNode", referencedColumnName="id")
	@JsonBackReference("workflowNodeProperties")	
	private WorkflowNode workflowNode;
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=false,updatable=true) 
	@JsonBackReference("workFlowNodeProperty-parent-child")
	private WorkflowNodeProperty parent;
	@OneToMany(cascade={CascadeType.ALL}) 
	@JoinColumn(name = "parent_id") 
	@JsonManagedReference("workFlowNodeProperty-parent-child")
	private Set<WorkflowNodeProperty> properties = new HashSet<WorkflowNodeProperty>();
	/*----------------------------------------------------------------------------*/	

	public WorkflowNodeProperty() { }
	
	public WorkflowNodeProperty(Long id) {
		this.setId(id);
	}

	public WorkflowNodeProperty getProperty(String code) {
		for (WorkflowNodeProperty workFlowNodeProperty: this.properties) {
			if (workFlowNodeProperty.getCode().equals(code)) {
				return workFlowNodeProperty;
			}
		}
		return null;
	}

	public List<WorkflowNodeProperty> getProperties(String code) {
		ArrayList<WorkflowNodeProperty> properties = new ArrayList<WorkflowNodeProperty>();
		for (WorkflowNodeProperty workFlowNodeProperty: this.properties) {
			if (workFlowNodeProperty.getCode().equals(code)) {
				properties.add(workFlowNodeProperty);
			}
		}
		return properties;
	}

	public void addProperty(WorkflowNodeProperty property) {
		property.setParent(this);
		this.properties.add(property);
	}

	public void addProperty(String code, String value) {
		WorkflowNodeProperty property = new WorkflowNodeProperty();
		property.setCode(code);
		property.setValue(value);
		property.setParent(this);
		this.properties.add(property);
	}
}
