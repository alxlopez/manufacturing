package com.mes.modules.milanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mes.dom.workDirective.WorkDirective;
import com.mes.dom.workSchedule.JobOrder;
import com.mes.dom.workSchedule.WorkRequest;
import com.mes.modules.milanes.fact.MaterialDefinitionWorkMaster;
import com.mes.modules.milanes.fact.StimatedScheduledTime;
import com.mes.modules.milanes.services.MilanesDirectiveService;
import com.mes.modules.milanes.services.RulesService;
import com.mes.modules.workSchedule.services.WorkRequestRepository;

@RestController
public class RulesController {
	@Autowired
	private RulesService rulesService;
	@Autowired
	private MilanesDirectiveService milanesDirectiveService;
	@Autowired
	private WorkRequestRepository workRequestRepository;

	@GetMapping("milanes/rules/scheduledProduction")
	public @ResponseBody ResponseEntity<StimatedScheduledTime> getProcessdays(
			@RequestParam(value = "workRequestId", required = false) Long workRequestId
	) {	
		StimatedScheduledTime  stimatedScheduledTime = null;
		WorkRequest workRequest = workRequestRepository.findById(workRequestId);		
		for(JobOrder jobOrder : workRequest.getJobOrders()){			
			if(jobOrder.getHierarchyScope().equals("PRODUCTION")){
				stimatedScheduledTime =rulesService.getScheduledTotalFinishedDate(jobOrder.getId());
			}
		}
		stimatedScheduledTime.setWorkRequestId(workRequestId);
		return ResponseEntity.ok(stimatedScheduledTime);
	}

	@GetMapping("milanes/rules/materialDefinitionWorkMaster")
	public @ResponseBody ResponseEntity<MaterialDefinitionWorkMaster> getMaterialDefinitionEndPoint(
			//@RequestParam(value = "workType", required = false) String workType,
			@RequestParam(value = "materialCode", required = false) String materialCode
	) {
		MaterialDefinitionWorkMaster materialDefinitionWorkMaster = rulesService.getMaterialDefinitionWorkMaster(materialCode);
		return ResponseEntity.ok(materialDefinitionWorkMaster);
	}
	
	@GetMapping("milanes/rules/cloneWorkDirective")
	public @ResponseBody ResponseEntity<Long> cloneWorkDirective(
			@RequestParam(value = "workDirectiveId", required = false) Long workDirectiveId
	) {
		Long clonedWorkDirective = milanesDirectiveService.clone(workDirectiveId);
		return ResponseEntity.ok(clonedWorkDirective);
	}
	
	@RequestMapping(value = "milanes/rules/reentry", method = RequestMethod.PATCH)
	public @ResponseBody ResponseEntity<WorkDirective> reentry(@RequestBody WorkDirective workDirective) {
		WorkDirective saved = milanesDirectiveService.reentry(workDirective);
		return ResponseEntity.ok(saved);
	}
}
