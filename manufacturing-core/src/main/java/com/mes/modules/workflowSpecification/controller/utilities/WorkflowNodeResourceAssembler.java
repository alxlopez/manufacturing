package com.mes.modules.workflowSpecification.controller.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mes.dom.WorkflowSpecification.WorkflowNode;

@SuppressWarnings("rawtypes")
@Component
public class WorkflowNodeResourceAssembler extends ResourceAssemblerSupport<WorkflowNode, Resource> {
	@Autowired
	private EntityLinks entityLinks;

	public WorkflowNodeResourceAssembler() {
		super(WorkflowNodeResourceAssembler.class, Resource.class);
	}

	@Override
	public Resource<WorkflowNode> toResource(WorkflowNode workflowNode) {
		Resource<WorkflowNode> resource = new Resource<WorkflowNode>(workflowNode);
		resource.add(entityLinks.linkForSingleResource(WorkflowNode.class, workflowNode.getId()).slash("nodes").withRel("nodes"));
		resource.add(entityLinks.linkForSingleResource(WorkflowNode.class, workflowNode.getId()).slash("properties").withRel("properties"));
		return resource;
	}
}
