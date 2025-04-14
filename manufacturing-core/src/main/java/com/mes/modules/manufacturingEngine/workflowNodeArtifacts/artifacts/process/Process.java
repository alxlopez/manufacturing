package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.WorkflowNodeStateMachine;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.Artifact;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines.ProcessAborting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines.ProcessHolding;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines.ProcessInitiate;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines.ProcessRestarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines.ProcessRunning;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines.ProcessStarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines.ProcessStopping;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineAborting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineHolding;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineInitiate;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineRestarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineRunning;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineStarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineStopping;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ProcessArtifactService;

@Component()
public class Process implements Artifact {	
	@Autowired
	@Qualifier("processInitiate")
	protected ProcessInitiate initiateRoutine;
	@Autowired
	protected ProcessStarting startingRoutine;	
	@Autowired
	@Qualifier("processRunning")
	protected ProcessRunning runningRoutine;
	@Autowired
	protected ProcessHolding holdingRoutine;
	@Autowired
	protected ProcessRestarting restartingRoutine;
	@Autowired
	protected ProcessStopping stoppingRoutine;
	@Autowired
	@Qualifier("processAborting")
	protected ProcessAborting abortingRoutine;	
	@Autowired
	protected ProcessArtifactService artifactService;
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
