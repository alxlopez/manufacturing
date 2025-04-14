package com.mes.modules.workflowSpecification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import com.mes.modules.workflowSpecification.services.WorkflowNodeRepository;

@RepositoryRestController
public class WorkflowNodeController {
	@Autowired
	public WorkflowNodeController(WorkflowNodeRepository repo) { }
}
