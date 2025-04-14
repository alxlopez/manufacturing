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

import com.mes.dom.event.EventType;
import com.mes.modules.manufacturingEngine.event.services.EventTypeRepository;

@RepositoryRestController
public class EventTypeController {
	@Autowired
	private EventTypeRepository repository;
	@Autowired
	private EntityLinks entityLinks;

	@RequestMapping(value = "/eventTypes", method = RequestMethod.POST)
	public ResponseEntity<Resource<EventType>> createEventType(@RequestBody EventType EventType) {
		EventType saved = repository.save(EventType);
		Resource<EventType>	resource = new Resource<EventType>(saved);
		resource.add(entityLinks.linkToSingleResource(EventType.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/eventTypes", method = RequestMethod.PUT)
	public ResponseEntity<Resources<EventType>> createBulkEventType(
			@RequestBody List<EventType> EventTypes
	) {
		List<EventType> saved = repository.save(EventTypes);
		Resources<EventType> resources = new Resources<EventType>(saved);
		resources.add(entityLinks.linkToCollectionResource(EventType.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/eventTypes/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<EventType>> updateEventType(@PathVariable long id, @RequestBody EventType EventType) {
		EventType saved = repository.save(EventType);
		Resource<EventType>	resource = new Resource<EventType>(saved);
		resource.add(entityLinks.linkToSingleResource(EventType.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}
}
