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
import com.mes.dom.enumerations.application.WorkTypes;
import com.mes.dom.equipment.Equipment;
import com.mes.dom.workSchedule.JobOrderList;
import com.mes.modules.workSchedule.services.JobOrderListRepository;

@RepositoryRestController
public class JobOrderListController {
	@Autowired
	private JobOrderListRepository repository;
	@Autowired
	private EntityLinks entityLinks;

	@RequestMapping(value = "jobOrderLists/search", method = RequestMethod.GET)
	public @ResponseBody List<JobOrderList> jobOrderListFindByParams(
			@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "startTime", required = false) Timestamp startTime,
			@RequestParam(value = "endTime", required = false) Timestamp endTime,
			@RequestParam(value = "workType", required = false) WorkTypes workType,
			@RequestParam(value = "hierarchyScope", required = false) String hierarchyScope,
			@RequestParam(value = "location", required = false, defaultValue = "0") Long location
	) {
		JobOrderList jobOrderListToSearch = new JobOrderList();
		if (location > 0) {// Used to validate a default value obtained in queryString.
			Equipment locationParam = new Equipment();
			locationParam.setId(location);
			jobOrderListToSearch.setLocation(locationParam);
		}
		if (id > 0) { // Used to validate a default value obtained in queryString.
			jobOrderListToSearch.setId(id);
		}
		jobOrderListToSearch.setCode(code);
		jobOrderListToSearch.setDescription(description);
		jobOrderListToSearch.setHierarchyScope(hierarchyScope);
		jobOrderListToSearch.setStartTime(startTime);
		jobOrderListToSearch.setEndTime(endTime);
		jobOrderListToSearch.setWorkType(workType);
		return repository.findAll(Example.of(jobOrderListToSearch));
	}

	@RequestMapping(value = "/jobOrderLists", method = RequestMethod.POST)
	public ResponseEntity<Resource<JobOrderList>> createJobOrderList(@RequestBody JobOrderList jobOrderList) {
		JobOrderList saved = repository.save(jobOrderList);
		Resource<JobOrderList>	resource = new Resource<JobOrderList>(saved);
		resource.add(entityLinks.linkToSingleResource(JobOrderList.class,saved.getId()).withSelfRel());		
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/jobOrderLists", method = RequestMethod.PUT)
	public ResponseEntity<Resources<JobOrderList>> createBulkJobOrderList(@RequestBody List<JobOrderList> jobOrderLists) {
		List<JobOrderList> saved = repository.save(jobOrderLists);		
		Resources<JobOrderList> resources = new Resources<JobOrderList>(saved);
		resources.add(entityLinks.linkToCollectionResource(JobOrderList.class).withSelfRel());		
		return ResponseEntity.ok(resources);

	}

	@RequestMapping(value = "/jobOrderLists/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<JobOrderList>> updateJobOrderList(@PathVariable long id, @RequestBody JobOrderList jobOrderList) {
		JobOrderList saved = repository.save(jobOrderList);
		Resource<JobOrderList>	resource = new Resource<JobOrderList>(saved);
		resource.add(entityLinks.linkToSingleResource(JobOrderList.class,saved.getId()).withSelfRel());		
		return ResponseEntity.ok(resource);
	}
}
