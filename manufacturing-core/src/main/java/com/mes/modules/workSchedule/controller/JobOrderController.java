package com.mes.modules.workSchedule.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mes.dom.workSchedule.JobOrder;
import com.mes.modules.workSchedule.controller.utilities.JobOrderResourceAssembler;
import com.mes.modules.workSchedule.services.JobOrderRepository;
import com.mes.query.translators.QueryJobOrder;
import com.mes.query.wrappers.Criteria;
import com.mes.query.wrappers.Link;
import com.mes.query.wrappers.QueryBody;

@RepositoryRestController
public class JobOrderController {
	@PersistenceContext
    private EntityManager em;
	@Autowired
	private JobOrderRepository repository;
	@Autowired
	private RepositoryEntityLinks entityLinks;
	@Autowired
	private JobOrderResourceAssembler jobOrderResourceAssembler;
	@Autowired
	private PagedResourcesAssembler<JobOrder>  pagedResourcesAssembler;

	@RequestMapping(value = "/jobOrders/query", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> test(Pageable pageable, @RequestBody QueryBody query) {
		QueryJobOrder qjo = new QueryJobOrder(em);
		for (Link join: query.getJoins()) {
			qjo.join(join.getParameter(), join.getEntity());
		}
		for (Criteria criteria: query.getFilters()) {
			qjo.filter(criteria.getKey(), criteria.getOperation(), criteria.getValue());
		}
		long size = qjo.size();
		qjo.page(pageable);
		List<JobOrder> jobOrders = qjo.run();
		Page<JobOrder> pages = new PageImpl<JobOrder>(jobOrders, pageable, size);
		PagedResources<?> resources = (size == 0 || (pageable.getPageSize() * pages.getNumber()) > size)?
				pagedResourcesAssembler.
				toEmptyResource(pages, JobOrder.class, entityLinks.linkFor(JobOrder.class).withSelfRel()):
					pagedResourcesAssembler.
					toResource(pages, jobOrderResourceAssembler);
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/jobOrders", method = RequestMethod.POST)
	public ResponseEntity<Resource<JobOrder>> createJobOrder(@RequestBody JobOrder jobOrder) {
		JobOrder saved = repository.save(jobOrder);
		return ResponseEntity.ok(jobOrderResourceAssembler.toResource(saved));
	}

	@RequestMapping(value = "/jobOrders", method = RequestMethod.PUT)
	public ResponseEntity<Resources<JobOrder>> createBulkJobOrder(@RequestBody List<JobOrder> jobOrders) {
		List<JobOrder> saved = repository.save(jobOrders);
		Resources<JobOrder> resources = new Resources<JobOrder>(saved);
		resources.add(entityLinks.linkToCollectionResource(JobOrder.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/jobOrders/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<JobOrder>> updateJobOrder(@PathVariable long id, @RequestBody JobOrder jobOrder) {
		JobOrder saved = repository.save(jobOrder);
		return ResponseEntity.ok(jobOrderResourceAssembler.toResource(saved));
	}
}
