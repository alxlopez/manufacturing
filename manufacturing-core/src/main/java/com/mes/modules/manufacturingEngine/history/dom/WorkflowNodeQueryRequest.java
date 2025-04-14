package com.mes.modules.manufacturingEngine.history.dom;

import java.util.Date;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.dom.enumerations.application.ArtifactTypes;
import com.mes.dom.enumerations.application.States;

import lombok.Data;
@Data
public class WorkflowNodeQueryRequest {
    private String id;
	private String processInstanceId;
	private String processDefinitionName;
	private String name;
	private String executionId;
	@Enumerated (EnumType.STRING)
	private States state;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date initiateTime;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date beginInitiateTime;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date finalInitiateTime;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date beginStartTime;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date finalStartTime;	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date endTime;	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date beginEndTime;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date finalEndTime;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date scheduledStartTime;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date beginScheduledStartTime;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date finalScheduledStartTime;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date scheduledEndTime;	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date beginScheduledEndTime;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date finalScheduledEndTime;
	private ArtifactTypes artifact;
	private String assignee;
	private List<WorkflowNodeVariable> variables;
}
