package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.subProcess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.WorkflowNodeStateMachine;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.Artifact;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.subProcess.routines.SubProcessAborting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.subProcess.routines.SubProcessHolding;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.subProcess.routines.SubProcessInitiate;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.subProcess.routines.SubProcessRestarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.subProcess.routines.SubProcessRunning;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.subProcess.routines.SubProcessStarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.subProcess.routines.SubProcessStopping;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineAborting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineHolding;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineInitiate;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineRestarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineRunning;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineStarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineStopping;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.SubprocessArtifactService;

@Component()
public class Subprocess implements Artifact {	
	@Autowired
	protected SubProcessInitiate initiateRoutine;
	@Autowired
	protected SubProcessStarting startingRoutine;	
	@Autowired
	protected SubProcessRunning runningRoutine;
	@Autowired
	protected SubProcessHolding holdingRoutine;
	@Autowired
	protected SubProcessRestarting restartingRoutine;
	@Autowired
	protected SubProcessStopping stoppingRoutine;
	@Autowired
	protected SubProcessAborting abortingRoutine;	
	@Autowired
	protected SubprocessArtifactService artifactService;
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
