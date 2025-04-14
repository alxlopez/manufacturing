package com.mes.dom.WorkflowSpecification;

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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mes.dom.enumerations.application.States;
import com.mes.dom.enumerations.application.WorkflowNodeTypes;
import com.mes.dom.enumerations.application.ProceduralElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
@EqualsAndHashCode(exclude={ "properties"})
public class WorkflowNode implements Serializable {
	private static final long serialVersionUID = 6842173777555977308L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=false,length=70)
	private String code;
	private String version;	
	@Enumerated (EnumType.STRING)
	private WorkflowNodeTypes nodeType;
	@Enumerated (EnumType.STRING)
	private ProceduralElement proceduralElement;	
	@ManyToOne
	@JoinColumn(name="workflowSpecification", referencedColumnName="id")
	@JsonBackReference("workflowSpecification-nodes")	
	private WorkflowSpecification workflowSpecification;
	private States state;
	private Timestamp startTime;
	private Timestamp endTime;
	private Long enlapsedTime;		
	private String workMasterId;
	private String executionId;
	/*----------------------------------------------------------------------------*/
    /* properties-composition                                                             */
	/*----------------------------------------------------------------------------*/
	@OneToMany(mappedBy="workflowNode",  cascade = CascadeType.ALL, orphanRemoval=true)
	@JsonManagedReference("workflowNodeProperties")
	private Collection<WorkflowNodeProperty> properties;	
	/*----------------------------------------------------------------------------*/
    /* parent child realtionship - composition                                                                 */
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", insertable = false, updatable = true)
	@JsonBackReference("workflowNode-parent-child")
	private WorkflowNode parent;
	@OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
	@JoinColumn(name = "parent_id")
	@JsonManagedReference("workflowNode-parent-child")
	private Collection<WorkflowNode> nodes = new HashSet<WorkflowNode>();

	public WorkflowNode(String code, WorkflowNodeTypes nodeType) {
		super();
		this.code = code;
		this.nodeType = nodeType;
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

	public void addProperty(WorkflowNodeProperty property) {
		property.setWorkflowNode(this);
		this.properties.add(property);
	}

	public void addProperty(String code, String value) {
		WorkflowNodeProperty property = new WorkflowNodeProperty();
		property.setCode(code);
		property.setValue(value);
		property.setWorkflowNode(this);
		this.properties.add(property);
	}

	public void addWorkflowNode(WorkflowNode workflowNode) {
		workflowNode.setParent(this);
		this.nodes.add(workflowNode);
	}
}
