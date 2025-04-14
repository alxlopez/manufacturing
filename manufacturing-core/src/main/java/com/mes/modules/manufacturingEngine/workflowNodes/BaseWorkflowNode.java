package com.mes.modules.manufacturingEngine.workflowNodes;

public interface BaseWorkflowNode {
	String PROP_WORKFLOWNODE_TYPE = "workflowNodeType";
	String PROP_ARTIFACT = "artifact";
    String PROP_STATE = "state";
	String PROP_INITIATE_TIME = "initiateTime";
	String PROP_START_TIME = "startTime";
	String PROP_END_TIME = "endTime";
	String PROP_ENLAPSED_TIME = "enlapsedTime";
	String PROP_REMOVED_TIME = "removedTime";
	String PROP_EQUIPMENT_BINDING = "equipmentBinding";	
	String PROP_COMMENT = "comment";		
	String PROP_ROUTINE_ID ="routineId";
	String PROP_OWNER_ID ="ownerId";
	String PROP_WORK_DIRECTIVE= "workDirective";
	String PROP_WORK_DIRECTIVE_ID= "workDirectiveId";
	String PROP_JOB_ORDER= "jobOrder";
	String PROP_JOB_ORDER_ID= "jobOrderId";
	String PROP_HELD_TIME="heldTime";
	String PROP_SCHEDULED_START_TIME ="scheduledStartTime";
	String PROP_SCHEDULED_END_TIME ="scheduledEndTime";
	String PROP_FAILURE_ID ="failureId";
	String PROP_WORK_TYPE="directiveWorkType";
	String PROP_HIERARCHY_SCOPE_ID = "hierarchyScopeId";	
	String PROP_BPM_CATEGORY="bpmCategory";
}
