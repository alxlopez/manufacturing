package com.mes.dom.workFlowNode;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mes.dom.enumerations.application.States;
import com.mes.dom.enumerations.application.WorkflowNodeTypes;
import com.mes.dom.workDirective.WorkDirective;

import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
@EqualsAndHashCode(exclude = {"workFlowNodes"})
public class WorkFlowNode implements Serializable {
	private static final long serialVersionUID = 4114031762888417677L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=false,length=70)
	private String code;
	@Enumerated (EnumType.STRING)
	private WorkflowNodeTypes nodeType;
	private States state;
	private Timestamp initiateTime;
	private Timestamp scheduledStartTime;
	private Timestamp scheduledEndTime;
	private Timestamp startTime;
	private Timestamp endTime;
	private Long enlapsedTime;	
	private String executionId;
	@ManyToOne
	@JoinColumn(name="workDirective", referencedColumnName="id")
	@JsonBackReference("workDirective-workFlowNode")
	private WorkDirective workDirective;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", insertable = true, updatable = true)
	@JsonBackReference("workFlowNode-parent-child")
	private WorkFlowNode parent;
	@OneToMany(cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "parent_id", insertable = true, updatable = false)
	@JsonManagedReference("workFlowNode-parent-child")
	private Set<WorkFlowNode> workFlowNodes = new HashSet<WorkFlowNode>();

	public WorkFlowNode(String code, WorkflowNodeTypes nodeType) {
		super();
		this.code = code;
		this.nodeType = nodeType;
	}

	public WorkFlowNode getWorkFlowNode(String code) {
		for (WorkFlowNode workFlowNode: this.workFlowNodes) {
			if (workFlowNode.getCode().equals(code)) {
				return workFlowNode;
			}
		}
		return null;
	}

	public List<WorkFlowNode> getWorkFlowNodes(String code) {
		ArrayList<WorkFlowNode> WorkFlowNodes = new ArrayList<WorkFlowNode>();
		for (WorkFlowNode workFlowNode: this.workFlowNodes) {
			if (workFlowNode.getCode().equals(code)) {
				WorkFlowNodes.add(workFlowNode);
			}
		}
		return WorkFlowNodes;
	}

	public void addWorkFlowNode(WorkFlowNode workFlowNode) {
		workFlowNode.setParent(this);
		this.workFlowNodes.add(workFlowNode);
	}
}
