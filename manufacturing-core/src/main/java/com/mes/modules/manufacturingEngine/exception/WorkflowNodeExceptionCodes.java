package com.mes.modules.manufacturingEngine.exception;

public interface WorkflowNodeExceptionCodes {
    String unknownCommand="unKnown command";
    String workDirectiveIdNotSpecified="Command could not be issued because workDirectiveId property is not defined in command event ";
    String workDirectiveNotFound= "Command could not be issued because workDirectiveId defined in event property does not mapped to a  WorkDirective, workDirectiveId specified was :  ";
    String workflowSpecificationAlreadyInitialized="Command could not be issued because workflowSpecification was already initialized for workDirective, current workDirective.workflowSpecification is : ";    
    String FailureIdUnKnown= "Command could not be issued because failureId is unknown by the system ";
}
