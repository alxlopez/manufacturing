package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.rest.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.ArtifactRestVariable;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.UserTaskArtifactService;

@RestController
@RequestMapping(path = "/mes")
public class UserTaskController {
	@Autowired
	private UserTaskArtifactService userTaskService;

	@RequestMapping(value = "/engine/artifacts/userTasks/{userTaskId}/variables", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void upsertVariable(@PathVariable String userTaskId,
			@RequestBody List<ArtifactRestVariable> variables)  {		
		userTaskService.upsertVariablesBasedOnScope(userTaskId, variables);
	}
}
