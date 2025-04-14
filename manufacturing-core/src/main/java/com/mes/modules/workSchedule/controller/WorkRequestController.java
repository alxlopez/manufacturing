package com.mes.modules.workSchedule.controller;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
import com.mes.dom.enumerations.application.Priority;
import com.mes.dom.enumerations.application.WorkTypes;
import com.mes.dom.equipment.Equipment;

import com.mes.dom.workSchedule.WorkRequest;
import com.mes.dom.workSchedule.WorkSchedule;
import com.mes.modules.workSchedule.controller.utilities.WorkRequestResourceAssembler;
import com.mes.modules.workSchedule.services.WorkRequestRepository;
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import com.mes.modules.workSchedule.services.WorkRequestService;

@RepositoryRestController
public class WorkRequestController {
	@Autowired
	private WorkRequestRepository repository;
	@Autowired
	private EntityLinks entityLinks;
	@Autowired
	private WorkRequestResourceAssembler  workRequestResourceAssembler;
	@Autowired
	private WorkRequestService workRequestService;

	@RequestMapping(value = "workRequests/{id}/hierarchy", method = RequestMethod.GET)
	public ResponseEntity<WorkRequest> getHierarchy(@PathVariable long id) {
		return ResponseEntity.ok(repository.findOne(id));
	}

	@RequestMapping(value = "workRequests/search", method = RequestMethod.GET)
	public  ResponseEntity<Resources<WorkRequest>> workRequestFindByParams(
			@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "startTime", required = false) Timestamp startTime,
			@RequestParam(value = "endTime", required = false) Timestamp endTime,
			@RequestParam(value = "workType", required = false) WorkTypes workType,
			@RequestParam(value = "priority", required = false) Priority priority,
			@RequestParam(value = "workSchedule", required = false, defaultValue = "0") Long workSchedule,
			@RequestParam(value = "hierarchyScope", required = false) String hierarchyScope,
			@RequestParam(value = "location", required = false, defaultValue = "0") Long location
	) {
		WorkRequest workRequestToSearch = new WorkRequest();
		if (location > 0) {// Used to validate a default value obtained in queryString.
			Equipment locationParam = new Equipment();
			locationParam.setId(location);
			workRequestToSearch.setLocation(locationParam);
		}
		if (id > 0) {// Used to validate a default value obtained in queryString.
			workRequestToSearch.setId(id);
		}
		if (workSchedule > 0) {
			WorkSchedule workScheduleParam = new WorkSchedule();
			workScheduleParam.setId(workSchedule);
			workRequestToSearch.setWorkSchedule(workScheduleParam);
		}
		workRequestToSearch.setCode(code);
		workRequestToSearch.setDescription(description);
		workRequestToSearch.setHierarchyScope(hierarchyScope);
		workRequestToSearch.setStartTime(startTime);
		workRequestToSearch.setEndTime(endTime);
		workRequestToSearch.setWorkType(workType);
		workRequestToSearch.setPriority(priority);			
		List<WorkRequest> ObjectsFound = repository.findAll(Example.of(workRequestToSearch));
		Resources<WorkRequest> resources = new Resources<WorkRequest>(ObjectsFound);		
		return ResponseEntity.ok(resources);		
	}

	@RequestMapping(value = "/workRequests", method = RequestMethod.POST)
	public ResponseEntity<Resource<WorkRequest>> createWorkRequest(@RequestBody WorkRequest workRequest) {
		WorkRequest saved = workRequestService.create(workRequest);
		return ResponseEntity.ok(workRequestResourceAssembler.toResource(saved));
	}

	@RequestMapping(value = "/workRequests", method = RequestMethod.PUT)
	public ResponseEntity<Resources<WorkRequest>> createBulkWorkRequest(@RequestBody List<WorkRequest> workRequests) {
		List<WorkRequest> saved = repository.save(workRequests);		
		Resources<WorkRequest> resources = new Resources<WorkRequest>(saved);
		resources.add(entityLinks.linkToCollectionResource(WorkRequest.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/workRequests/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<WorkRequest>> updateWorkRequest(@PathVariable long id,@RequestBody WorkRequest workRequest) {
		WorkRequest saved = repository.save(workRequest);
		return ResponseEntity.ok(workRequestResourceAssembler.toResource(saved));
	}
}
