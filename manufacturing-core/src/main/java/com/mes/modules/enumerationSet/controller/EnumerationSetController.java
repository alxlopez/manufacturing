package com.mes.modules.enumerationSet.controller;

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

import com.mes.dom.enumerations.custom.EnumerationSet;
import com.mes.modules.enumerationSet.services.EnumerationSetRepository;

@RepositoryRestController
public class EnumerationSetController {
	@Autowired
	private EnumerationSetRepository repository;
	@Autowired
	private EntityLinks entityLinks;

	@RequestMapping(value = "enumerationSets/search", method = RequestMethod.GET)
	public @ResponseBody List<EnumerationSet> findByParams(
			@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "description", required = false) String description
	) {
		EnumerationSet objectToSearch = new EnumerationSet();
		if (id > 0) {
			objectToSearch.setId(id);
		}
		objectToSearch.setCode(code);
		objectToSearch.setDescription(description);
		return repository.findAll(Example.of(objectToSearch));
	}

	@RequestMapping(value = "/enumerationSets", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Resource<EnumerationSet>> saveEquipment(@RequestBody EnumerationSet enumerationSet) {
		EnumerationSet saved = repository.save(enumerationSet);
		Resource<EnumerationSet> resource = new Resource<EnumerationSet>(saved);
		resource.add(entityLinks.linkToSingleResource(EnumerationSet.class, saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/enumerationSets", method = RequestMethod.PUT)
	public ResponseEntity<Resources<EnumerationSet>> createBulkEquipment(@RequestBody List<EnumerationSet> enumerationSets) {
		List<EnumerationSet> saved = repository.save(enumerationSets);
		Resources<EnumerationSet> resources = new Resources<EnumerationSet>(saved);
		resources.add(entityLinks.linkToCollectionResource(EnumerationSet.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/enumerationSets/{id}", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity<Resource<EnumerationSet>> updateEquipment(@PathVariable long id,@RequestBody EnumerationSet enumerationSet) {
		EnumerationSet saved = repository.save(enumerationSet);
		Resource<EnumerationSet> resource = new Resource<EnumerationSet>(saved);
		resource.add(entityLinks.linkToSingleResource(EnumerationSet.class, saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}
}
