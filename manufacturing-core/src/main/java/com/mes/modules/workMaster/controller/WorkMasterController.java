package com.mes.modules.workMaster.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mes.dom.workDirective.WorkMaster;
import com.mes.modules.workMaster.controller.utilities.WorkMasterResourceAssembler;
import com.mes.modules.workMaster.services.WorkMasterRepository;
import com.mes.modules.workMaster.services.WorkMasterService;

@RepositoryRestController
public class WorkMasterController {
	private final WorkMasterRepository repository;
	@Autowired
	public EntityLinks entityLinks;
	@Autowired
	public WorkMasterService workMasterService;
	@Autowired
	public WorkMasterResourceAssembler workMasterResourceAssembler;

	@Autowired
	public WorkMasterController(WorkMasterRepository repo) {
		repository = repo;
	}

	@RequestMapping(value = "/workMasters", method = RequestMethod.POST)
	public ResponseEntity<Resource<WorkMaster>> createWorkDirective(@RequestBody WorkMaster workMaster) {
		WorkMaster saved = workMasterService.create(workMaster);
		return ResponseEntity.ok(workMasterResourceAssembler.toResource(saved));
	}	

	@RequestMapping(value = "/workMasters", method = RequestMethod.PUT)
	public ResponseEntity<Resources<WorkMaster>> createBulkWorkDefinition(
			@RequestBody List<WorkMaster> workMasters) {		
		List<WorkMaster> saved = repository.save(workMasters);
		Resources<WorkMaster> resources = new Resources<WorkMaster>(saved);
		resources.add(entityLinks.linkToCollectionResource(WorkMaster.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/workMasters/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<WorkMaster>> updateWorkDefinition(@PathVariable long id,
			@RequestBody WorkMaster workMaster) {
		WorkMaster saved = repository.save(workMaster);
		Resource<WorkMaster> resource = new Resource<WorkMaster>(saved);
		resource.add(entityLinks.linkToSingleResource(WorkMaster.class, saved.getId()).withSelfRel());
		return ResponseEntity.ok(workMasterResourceAssembler.toResource(saved));
	}
}
