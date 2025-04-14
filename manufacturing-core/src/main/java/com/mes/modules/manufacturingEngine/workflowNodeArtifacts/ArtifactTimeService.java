package com.mes.modules.manufacturingEngine.workflowNodeArtifacts;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.mes.config.exception.ManufacturingConflictException;
import com.mes.dom.enumerations.application.States;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class ArtifactTimeService {
	public final Date getCurrentDate() {
		return new Date();
	}

	public void setArtifactTimesBasedOnState(WorkflowNode workflowNode, States state) {
		ArtifactService artifactService = workflowNode.getArtifact().getArtifactService();
		if (state == States.READY) {
			this.setInitiateTime(workflowNode.getArtifactId(), artifactService);
		} else if (state == States.RUNNING) {
			if (artifactService.getVariableLocal(workflowNode.getArtifactId(), BaseWorkflowNode.PROP_START_TIME) == null) {
				this.setStartTime(workflowNode.getArtifactId(), artifactService);
			}
		} else if (state == States.HELD) {
		} else if (state == States.COMPLETE) {
			this.setEndTime(workflowNode.getArtifactId(), artifactService);
			this.setEnlapsedTime(workflowNode.getArtifactId(), artifactService);
		} else if (state == States.ABORTED) {
			this.setEndTime(workflowNode.getArtifactId(), artifactService);
			this.setEnlapsedTime(workflowNode.getArtifactId(), artifactService);
		} else if (state == States.STOPPED) {
			this.setEndTime(workflowNode.getArtifactId(), artifactService);
			this.setEnlapsedTime(workflowNode.getArtifactId(), artifactService);
		}
	}

	public Long getEnlapsedTime(Date startTimeDate, Date endTimeDate) {
		Long enlapsedTime = null;
		enlapsedTime = endTimeDate.getTime() - startTimeDate.getTime();
		return enlapsedTime;
	}

	public String getISODateString(Date time) {
		TimeZone timezone = TimeZone.getDefault();
		return ISO8601Utils.format(time, true, timezone);
	}

	public Date getISODate(String IsoDateString) {

		try {
			Date date = ISO8601Utils.parse(IsoDateString, new ParsePosition(0));
			return date;
		} catch (ParseException e) {
			throw new ManufacturingConflictException("date " + IsoDateString + " could no be parsed to ISO8601");
		}
	}

	public Date agregateTimeToDate(Date date, long timeToAgregate) {
		long agregatedDateLong = date.getTime() + timeToAgregate;
		Date agregatedDate = new Date(agregatedDateLong);
		return agregatedDate;
	}

	public Long calculateEnlapsedTime(String workflowNodeId, ArtifactService nodeService) {
		Object startTimeVariable = nodeService.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_START_TIME);
		if (startTimeVariable != null) {
			Date startTime = (Date) startTimeVariable;
			Date endTime = (Date) nodeService.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_END_TIME);
			Long enlapsedTime = this.getEnlapsedTime(startTime, endTime);
			return enlapsedTime;
		}
		return null;
	}

	public void setInitiateTime(String workflowNodeId, ArtifactService nodeService) {
		nodeService.setVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_INITIATE_TIME, new Date());
	}

	public void setStartTime(String workflowNodeId, ArtifactService nodeService) {
		nodeService.setVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_START_TIME, new Date());
	}

	public void setEndTime(String workflowNodeId, ArtifactService nodeService) {
		nodeService.setVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_END_TIME, new Date());
		if (this.getStartTime(workflowNodeId, nodeService) != null) {
			this.setEnlapsedTime(workflowNodeId, nodeService);
		}
	}

	public void setEnlapsedTime(String workflowNodeId, ArtifactService nodeService) {
		Long enlapsedTime = this.calculateEnlapsedTime(workflowNodeId, nodeService);
		nodeService.setVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_ENLAPSED_TIME, enlapsedTime);
	}

	public Date getStartTime(String workflowNodeId, ArtifactService nodeService) {
		Date startTime = (Date) nodeService.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_START_TIME);
		return startTime;
	}
}
