package com.mes.dom.WorkflowSpecification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
@EqualsAndHashCode(exclude={"nodes"})
public class WorkflowSpecification implements Serializable{
	private static final long serialVersionUID = 7912919246023691932L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=false,length=70)
	private String code;
	private String version;	
	@OneToMany(mappedBy="workflowSpecification", cascade = CascadeType.ALL, orphanRemoval=true)
	@JsonManagedReference("workflowSpecification-nodes")
	private Collection<WorkflowNode> nodes = new HashSet<WorkflowNode>();
	public WorkflowSpecification(){}

	public WorkflowSpecification(String code) {
		this.code = code; 
	}

	public WorkflowNode getWorkflowNode(String code) {
		for (WorkflowNode workflowNode: this.nodes) {
			if (workflowNode.getCode().equals(code)) {
				return workflowNode;
			}
		}
		return null;
	}

	public List<WorkflowNode> getWorkflowNodes(String code) {
		ArrayList<WorkflowNode> WorkflowNodes = new ArrayList<WorkflowNode>();
		for (WorkflowNode workflowNode: this.nodes) {
			if (workflowNode.getCode().equals(code)) {
				WorkflowNodes.add(workflowNode);
			}
		}
		return WorkflowNodes;
	}

	public void addWorkflowNode(WorkflowNode workflowNode) {
		workflowNode.setWorkflowSpecification(this);
		this.nodes.add(workflowNode);
	}
}
