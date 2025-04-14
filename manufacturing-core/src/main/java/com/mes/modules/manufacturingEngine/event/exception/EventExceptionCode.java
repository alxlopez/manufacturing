package com.mes.modules.manufacturingEngine.event.exception;

public interface EventExceptionCode {
	public String requiredEventAttributesMissing ="Event could not be created,either eventDefinition,reporterId, entityType, entityId is required";
    public String eventDefinitionNotFound = "eventDefinitionNotFound for value set for event.eventDefinition.id ";
    public String eventDefinitionNotAllowedAsEvent = "event definition only allowed to be specified as event cause and not as event eventDefinition,event definition specified was: ";
    public String workflowNodeTypeNotSpecified = "Command could not be issued because workflowNodeType is not specified" ;
}