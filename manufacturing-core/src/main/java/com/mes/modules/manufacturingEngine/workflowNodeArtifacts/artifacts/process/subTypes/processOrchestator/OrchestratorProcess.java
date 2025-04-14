package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.subTypes.processOrchestator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.WorkflowNodeStateMachine;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.Process;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines.ProcessHolding;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines.ProcessRestarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines.ProcessRunning;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines.ProcessStarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines.ProcessStopping;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.subTypes.processOrchestator.routines.OrchestratorProcessAborting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.subTypes.processOrchestator.routines.OrchestratorProcessInitiate;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineAborting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineHolding;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineInitiate;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineRestarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineRunning;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineStarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineStopping;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ProcessArtifactService;

@Component
public class OrchestratorProcess extends Process  {	
	@Autowired
	protected OrchestratorProcessInitiate initiateRoutine;
	@Autowired
	protected ProcessStarting startingRoutine;	
	@Autowired
	protected ProcessRunning runningRoutine;
	@Autowired
	protected ProcessHolding holdingRoutine;
	@Autowired
	protected ProcessRestarting restartingRoutine;
	@Autowired
	protected ProcessStopping stoppingRoutine;
	@Autowired
	protected OrchestratorProcessAborting abortingRoutine;	
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
