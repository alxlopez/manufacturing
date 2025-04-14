package com.mes.modules.workDirective.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mes.dom.workDirective.WorkDirective;
import com.mes.modules.workDirective.controller.utilities.WorkDirectiveResourceAssembler;
import com.mes.modules.workDirective.services.WorkDirectiveRepository;
import com.mes.modules.workDirective.services.WorkDirectiveService;
import com.mes.modules.workSchedule.services.JobOrderRepository;
import com.mes.query.translators.QueryWorkDirective;
import com.mes.query.wrappers.Criteria;
import com.mes.query.wrappers.Link;
import com.mes.query.wrappers.QueryBody;

@RepositoryRestController
public class WorkDirectiveController {
	@PersistenceContext
    private EntityManager em;
	@Autowired
	private WorkDirectiveRepository repository;
	@Autowired
	public PagedResourcesAssembler<WorkDirective>  pagedResourcesAssembler;
	@Autowired
	public EntityLinks entityLinks;
	@Autowired
	public WorkDirectiveService workDirectiveService;
	@Autowired
	public JobOrderRepository jobOrderRepository;
	@Autowired
	public WorkDirectiveResourceAssembler workDirectiveResourceAssembler;

	@RequestMapping(value = "/workDirectives/query", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> test(Pageable pageable, @RequestBody QueryBody query) {
		QueryWorkDirective qwd = new QueryWorkDirective(em);
		for (Link join: query.getJoins()) {
			qwd.join(join.getParameter(), join.getEntity());
		}
		for (Criteria criteria: query.getFilters()) {
			qwd.filter(criteria.getKey(), criteria.getOperation(), criteria.getValue());
		}
		long size = qwd.size();
		qwd.page(pageable);
		List<WorkDirective> workDirectives = qwd.run();
		Page<WorkDirective> pages = new PageImpl<WorkDirective>(workDirectives, pageable, size);
		PagedResources<?> resources = (size == 0 || (pageable.getPageSize() * pages.getNumber()) > size)?
				pagedResourcesAssembler.
				toEmptyResource(pages, WorkDirective.class, entityLinks.linkFor(WorkDirective.class).withSelfRel()):
					pagedResourcesAssembler.
					toResource(pages, workDirectiveResourceAssembler);
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/workDirectives", method = RequestMethod.POST)
	public ResponseEntity<Resource<WorkDirective>> createWorkDirective(@RequestBody WorkDirective workDirective) {
		WorkDirective saved = workDirectiveService.create(workDirective);
		workDirectiveService.validateReferencedDirectivesIdsParameter(workDirective);
		return ResponseEntity.ok(workDirectiveResourceAssembler.toResource(saved));
	}

	@RequestMapping(value = "/workDirectives/jobOrder", method = RequestMethod.POST)
	public ResponseEntity<Resource<WorkDirective>> createWorkDirectiveJobOrder(
			@RequestBody WorkDirective workDirective) {
		WorkDirective saved = workDirectiveService.createBasedOnJobOrder(workDirective);
		return ResponseEntity.ok(workDirectiveResourceAssembler.toResource(saved));
	}

	@RequestMapping(value = "/workDirectives", method = RequestMethod.PUT)
	public ResponseEntity<Resources<WorkDirective>> createBulkWorkDefinition(
			@RequestBody List<WorkDirective> workDirectives) {
		List<WorkDirective> saved = repository.save(workDirectives);
		Resources<WorkDirective> resources = new Resources<WorkDirective>(saved);
		resources.add(entityLinks.linkToCollectionResource(WorkDirective.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/workDirectives/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<WorkDirective>> updateWorkDefinition(@PathVariable long id,
			@RequestBody WorkDirective workDirective) {
		WorkDirective saved = repository.save(workDirective);
		Resource<WorkDirective> resource = new Resource<WorkDirective>(saved);
		resource.add(entityLinks.linkToSingleResource(WorkDirective.class, saved.getId()).withSelfRel());
		return ResponseEntity.ok(workDirectiveResourceAssembler.toResource(saved));
	}

	@RequestMapping(value = "/workDirectives/{id}/description", method = RequestMethod.PATCH)
	public ResponseEntity<Resource<WorkDirective>> patchWorkDirective(@PathVariable long id,
			@RequestBody WorkDirective workDirective) {
		WorkDirective saved = workDirectiveService.updateDescription(id, workDirective.getDescription());
		return ResponseEntity.ok(workDirectiveResourceAssembler.toResource(saved));
	}
}
