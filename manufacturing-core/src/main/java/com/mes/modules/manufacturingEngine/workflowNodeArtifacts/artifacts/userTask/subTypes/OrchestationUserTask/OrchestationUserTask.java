package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.subTypes.OrchestationUserTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.WorkflowNodeStateMachine;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.Artifact;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.Routines.UserActionAborting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.Routines.UserActionHolding;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.Routines.UserActionInitiate;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.Routines.UserActionRestarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.Routines.UserActionStopping;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.subTypes.OrchestationUserTask.routines.OrchestationUserTaskRunning;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.subTypes.OrchestationUserTask.routines.OrchestationUserTaskStarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineAborting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineHolding;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineInitiate;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineRestarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineRunning;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineStarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineStopping;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.UserTaskArtifactService;

@Component
public class OrchestationUserTask implements Artifact {
	@Autowired
	protected UserActionInitiate initiateRoutine;
	@Autowired
	protected OrchestationUserTaskStarting startingRoutine;
	@Autowired
	protected OrchestationUserTaskRunning runningRoutine;
	@Autowired
	protected UserActionHolding holdingRoutine;
	@Autowired
	protected UserActionRestarting restartingRoutine;
	@Autowired
	protected UserActionStopping stoppingRoutine;
	@Autowired
	protected UserActionAborting abortingRoutine;	
	@Autowired
	protected UserTaskArtifactService artifactService;	
	@Autowired
	protected WorkflowNodeStateMachine stateMachine;


	@Override
	public ArtifactService getArtifactService() {		
		return artifactService;
	}
	
	@Override
	public WorkflowNodeStateMachine getStateMachine() {
		return stateMachine;
	}	

	@Override
	public WorkflowNodeArtifactRoutineInitiate getInitiateRoutine() {
		return initiateRoutine;
	}	

	@Override
	public WorkflowNodeArtifactRoutineStarting getStartingRoutine() {
		return startingRoutine;
	}
	
	@Override
	public WorkflowNodeArtifactRoutineRunning getRunningRoutine() {
		return runningRoutine;
	}	
	
	@Override
	public WorkflowNodeArtifactRoutineHolding getHoldingRoutine() {
		return holdingRoutine;
	}

	@Override
	public WorkflowNodeArtifactRoutineRestarting getRestartingRoutine() {
		return restartingRoutine;
	}

	@Override
	public WorkflowNodeArtifactRoutineStopping getStoppingRoutine() {
		return stoppingRoutine;
	}

	@Override
	public WorkflowNodeArtifactRoutineAborting getAbortingRoutine() {
		return abortingRoutine;
	}

	
}
