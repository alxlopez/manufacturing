package com.mes.modules.workflowSpecification.controller.utilities;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.flowable.rest.service.api.runtime.process.ProcessInstanceDiagramResource;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResource;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import com.mes.dom.WorkflowSpecification.WorkflowNode;

@Component
public class WorkflowNodeProcessor implements ResourceProcessor<Resource<WorkflowNode>> {
	@Override
	public Resource<WorkflowNode> process(Resource<WorkflowNode> resource) {	
		WorkflowNode workflowNode = resource.getContent();
		resource.add(linkTo(methodOn(ProcessInstanceResource.class).getProcessInstance(workflowNode.getCode(), null)).withRel("processInstance"));
		resource.add(linkTo(methodOn(ProcessInstanceDiagramResource.class).getProcessInstanceDiagram(workflowNode.getCode(), null)).withRel("diagram"));
		return resource;
	}
}
