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

import com.mes.dom.event.EventDefinition;
import com.mes.modules.manufacturingEngine.event.services.EventDefinitionRepository;

@RepositoryRestController
public class EventDefinitionController {
	@Autowired
	private EventDefinitionRepository repository;	
	@Autowired
	private EntityLinks entityLinks;

	@RequestMapping(value = "/eventDefinitions", method = RequestMethod.POST)
	public ResponseEntity<Resource<EventDefinition>> createEventDefinition(@RequestBody EventDefinition eventDefinition) {
		EventDefinition saved = repository.save(eventDefinition);
		Resource<EventDefinition>	resource = new Resource<EventDefinition>(saved);
		resource.add(entityLinks.linkToSingleResource(EventDefinition.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/eventDefinitions", method = RequestMethod.PUT)
	public ResponseEntity<Resources<EventDefinition>> createBulkEventDefinition(
			@RequestBody List<EventDefinition> eventDefinitions
	) {
		List<EventDefinition> saved = repository.save(eventDefinitions);
		Resources<EventDefinition> resources = new Resources<EventDefinition>(saved);
		resources.add(entityLinks.linkToCollectionResource(EventDefinition.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/eventDefinitions/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<EventDefinition>> updateEventDefinition(@PathVariable long id, @RequestBody EventDefinition eventDefinition) {
		EventDefinition saved = repository.save(eventDefinition);
		Resource<EventDefinition>	resource = new Resource<EventDefinition>(saved);
		resource.add(entityLinks.linkToSingleResource(EventDefinition.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}
}
