package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.Routines;

import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.config.exception.ManufacturingIllegalArgumentException;
import com.mes.dom.equipment.Equipment;
import com.mes.dom.event.ExecutionEvent;
import com.mes.dom.material.MaterialLot;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.modules.equipment.services.EquipmentRepository;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineStarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.UserTaskArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;
import com.mes.modules.material.services.MaterialLotRepository;

@Component
public class UserActionStarting extends WorkflowNodeArtifactRoutineStarting {
	@Autowired
	private MaterialLotRepository materialLotRepository;
	@Autowired
	private EquipmentRepository equipmentRepository;

	@Override
	public void execute(WorkflowNode workflowNode,ExecutionEvent event) {
		if (event.getReporterId()==null){
			throw new ManufacturingIllegalArgumentException("event.ReporterId is missing, for starting a userTask reporterId is required to be set as task assignee");
		}
		super.execute(workflowNode, event);
		UserTaskArtifactService workflowNodeService= (UserTaskArtifactService)workflowNode.getArtifact().getArtifactService();
		workflowNodeService.claim(workflowNode.getArtifactId(), event.getReporterId());
	    super.completeRoutine(workflowNode, event);
	    setLocation(workflowNode,event);
	}

	public void setLocation(WorkflowNode workflowNode, ExecutionEvent event) {
		UserTaskArtifactService taskNodeService = (UserTaskArtifactService) workflowNode.getArtifact()
				.getArtifactService();
		WorkDirective workDirective = (WorkDirective) taskNodeService.getVariable(workflowNode.getArtifactId(),
				BaseWorkflowNode.PROP_WORK_DIRECTIVE);
		if (workDirective != null) {
			Long workDirectiveId = workDirective.getId();
			MaterialLot materialLot = materialLotRepository.findByCode(workDirectiveId.toString());
			if(materialLot != null)
			{
				TaskEntity taskEntity = taskNodeService.findTaskByExecutionId(workflowNode.getExecutionId());
				String taskId = taskEntity.getId();
				String equipmentBinding = (String) taskNodeService.getVariableLocal(taskId, "equipmentBinding");
				System.out.println("equipmentBinding: "+equipmentBinding);
				Equipment equipment = equipmentRepository.findByCode(equipmentBinding);
				materialLot.setStorageLocation(equipment);
				materialLotRepository.save(materialLot);
			}
		}	
	}
}
