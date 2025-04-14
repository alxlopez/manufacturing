package com.mes.modules.manufacturingEngine.event.controller.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mes.dom.event.ExecutionEvent;

@SuppressWarnings("rawtypes")
@Component
public class EventResourceAssembler extends ResourceAssemblerSupport<ExecutionEvent, Resource> {
	@Autowired
	private EntityLinks entityLinks;

	public EventResourceAssembler() {
		super(ExecutionEvent.class, Resource.class);
	}

	@Override
	public Resource<ExecutionEvent> toResource(ExecutionEvent event) {
		Resource<ExecutionEvent> resource = new Resource<ExecutionEvent>(event);
		resource.add(entityLinks.linkForSingleResource(ExecutionEvent.class, event.getId()).slash("properties").withRel("properties"));
		resource.add(entityLinks.linkForSingleResource(ExecutionEvent.class, event.getId()).slash("eventDefinition").withRel("eventDefinition"));
		resource.add(entityLinks.linkForSingleResource(ExecutionEvent.class, event.getId()).slash("cause").withRel("cause"));
		return resource;
	}
}
