package com.mes.modules.manufacturingEngine.event.controller;

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

import com.mes.dom.enumerations.application.EventEntities;
import com.mes.dom.enumerations.application.EventTypes;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.event.controller.utilities.EventResourceAssembler;
import com.mes.modules.manufacturingEngine.event.services.ArtifactEventService;
import com.mes.modules.manufacturingEngine.event.services.EventRepository;
import com.mes.modules.manufacturingEngine.event.services.WorkDirectiveEventService;
import com.mes.modules.manufacturingEngine.event.services.WorkflowNodeEventService;

@RepositoryRestController
public class EventController {
	@Autowired
	private EntityLinks entityLinks;
	@Autowired
	private EventResourceAssembler eventResourceAssembler;	
	@Autowired
	private WorkflowNodeEventService workflowNodeEventService;
	@Autowired
	private WorkDirectiveEventService workDirectiveEventService;
	@Autowired
	private ArtifactEventService artifactEventService;	
	@Autowired
	private EventRepository eventRepository;

	@RequestMapping(value = "/events", method = RequestMethod.POST)
	public ResponseEntity<Resource<ExecutionEvent>> createEvent(@RequestBody ExecutionEvent event) {
		ExecutionEvent eventSaved = new ExecutionEvent();
		event.setEventType(EventTypes.USER);
		if (event.getEntityType() == EventEntities.WORK_DIRECTIVE) {
			eventSaved = workDirectiveEventService.throwEvent(event,EventTypes.USER);
		} else if (event.getEntityType() == EventEntities.WORKFLOW_NODE) {
			eventSaved = workflowNodeEventService.throwEvent(event,EventTypes.USER);
		} else {
			eventSaved =artifactEventService.throwEvent(event,EventTypes.USER);
		}
		return ResponseEntity.ok(eventResourceAssembler.toResource(eventSaved));
	}

	@RequestMapping(value = "/events", method = RequestMethod.PUT)
	public ResponseEntity<Resources<ExecutionEvent>> createBulkEvent(@RequestBody List<ExecutionEvent> events) {
		List<ExecutionEvent> saved = eventRepository.save(events);
		Resources<ExecutionEvent> resources = new Resources<ExecutionEvent>(saved);
		resources.add(entityLinks.linkToCollectionResource(ExecutionEvent.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/events/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<ExecutionEvent>> updateEvent(@PathVariable long id,
			@RequestBody ExecutionEvent event) {
		ExecutionEvent saved = eventRepository.save(event);
		return ResponseEntity.ok(eventResourceAssembler.toResource(saved));
	}
}
