package com.mes.modules.manufacturingEngine.history.dom;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.dom.enumerations.application.ArtifactTypes;
import com.mes.dom.enumerations.application.States;

import lombok.Data;

@Data
public class HistoricWorkflowNode implements Serializable {
	private static final long serialVersionUID = 7458084007616470796L;
	private String id;
	private String processInstanceId;
	private String name;
	private String processDefinitionName;
	private String executionId;
	@Enumerated (EnumType.STRING)
	private States state;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date initiateTime;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date startTime;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date endTime;
	private Long enlapsedTime;
	private ArtifactTypes artifact;
	private String assignee;
	private Map<String, Object> variables = new HashMap<String, Object>();	
	private List<HistoricWorkflowNode> activities;	
}
