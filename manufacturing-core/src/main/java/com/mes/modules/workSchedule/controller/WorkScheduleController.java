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
import org.springframework.web.bind.annotation.ResponseBody;

import com.mes.dom.enumerations.application.ScheduleState;
import com.mes.dom.enumerations.application.WorkTypes;
import com.mes.dom.equipment.Equipment;
import com.mes.dom.workSchedule.WorkSchedule;
import com.mes.modules.workSchedule.services.WorkScheduleRepository;

@RepositoryRestController
public class WorkScheduleController {
	@Autowired
	private EntityLinks entityLinks;
	@Autowired
	private WorkScheduleRepository repository;

	@RequestMapping(value = "workSchedules/search", method = RequestMethod.GET)
	public @ResponseBody List<WorkSchedule> workScheduleFindByParams(
			@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "publishedDate", required = false) Timestamp publishedDate,
			@RequestParam(value = "scheduleState", required = false) ScheduleState scheduleState,
			@RequestParam(value = "startTime", required = false) Timestamp startTime,
			@RequestParam(value = "endTime", required = false) Timestamp endTime,
			@RequestParam(value = "workType", required = false) WorkTypes workType,
			@RequestParam(value = "hierarchyScope", required = false) String hierarchyScope,
			@RequestParam(value = "location", required = false, defaultValue = "0") Long location
	) {
		WorkSchedule workScheduleToSearch = new WorkSchedule();
		if (location > 0) {// Used to validate a default value obtained in queryString.
			Equipment locationParam = new Equipment();
			locationParam.setId(location);
			workScheduleToSearch.setLocation(locationParam);
		}
		if (id > 0) {// Used to validate a default value obtained in queryString.
			workScheduleToSearch.setId(id);
		}
		workScheduleToSearch.setCode(code);
		workScheduleToSearch.setDescription(description);
		workScheduleToSearch.setHierarchyScope(hierarchyScope);
		workScheduleToSearch.setStartTime(startTime);
		workScheduleToSearch.setEndTime(endTime);
		workScheduleToSearch.setWorkType(workType);
		workScheduleToSearch.setPublishedDate(publishedDate);
		workScheduleToSearch.setScheduleState(scheduleState);
		return repository.findAll(Example.of(workScheduleToSearch));
	}

	@RequestMapping(value = "/workSchedules", method = RequestMethod.POST)
	public ResponseEntity<Resource<WorkSchedule>> createWorkSchedule(@RequestBody WorkSchedule workSchedule) {
		WorkSchedule saved = repository.save(workSchedule);
		Resource<WorkSchedule>	resource = new Resource<WorkSchedule>(saved);
		resource.add(entityLinks.linkToSingleResource(WorkSchedule.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/workSchedules", method = RequestMethod.PUT)
	public ResponseEntity<Resources<WorkSchedule>> createBulkWorkSchedule(@RequestBody List<WorkSchedule> workSchedules) {
		List<WorkSchedule> saved = repository.save(workSchedules);		
		Resources<WorkSchedule> resources = new Resources<WorkSchedule>(saved);
		resources.add(entityLinks.linkToCollectionResource(WorkSchedule.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/workSchedules/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<WorkSchedule>> updateWorkSchedule(@PathVariable long id, @RequestBody WorkSchedule workSchedule) {
		WorkSchedule saved = repository.save(workSchedule);
		Resource<WorkSchedule>	resource = new Resource<WorkSchedule>(saved);
		resource.add(entityLinks.linkToSingleResource(WorkSchedule.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}
}
