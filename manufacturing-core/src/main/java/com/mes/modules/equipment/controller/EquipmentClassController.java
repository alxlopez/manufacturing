package com.mes.modules.equipment.controller;

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

import com.mes.dom.enumerations.application.EquipmentLevel;
import com.mes.dom.equipment.*;
import com.mes.modules.equipment.services.EquipmentClassRepository;

@RepositoryRestController
public class EquipmentClassController {
	@Autowired
	private EquipmentClassRepository repository;
	@Autowired
	private EntityLinks entityLinks;

	@RequestMapping(value = "equipmentClasses/search", method = RequestMethod.GET)
	public @ResponseBody List<EquipmentClass> equipmentFindByParams(
			@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "equipmentLevel", required = false) EquipmentLevel equipmentLevel,
			@RequestParam(value = "hierarchyScope", required = false) String hierarchyScope,
			@RequestParam(value = "location", required = false, defaultValue = "0") Long location
	) {
		EquipmentClass equipmentClassToSearch = new EquipmentClass();
		if (location > 0) {
			Equipment locationParam = new Equipment();
			locationParam.setId(location);
			equipmentClassToSearch.setLocation(locationParam);
		}
		if (id > 0) {
			equipmentClassToSearch.setId(id);
		}
		equipmentClassToSearch.setCode(code);
		equipmentClassToSearch.setDescription(description);
		equipmentClassToSearch.setEquipmentLevel(equipmentLevel);
		equipmentClassToSearch.setHierarchyScope(hierarchyScope);
		return repository.findAll(Example.of(equipmentClassToSearch));
	}

	@RequestMapping(value = "/equipmentClasses", method = RequestMethod.POST)
	public ResponseEntity<Resource<EquipmentClass>> saveEquipmentClass(@RequestBody EquipmentClass equipmentClass) {
		EquipmentClass saved = repository.save(equipmentClass);
		Resource<EquipmentClass>	resource = new Resource<EquipmentClass>(saved);
		resource.add(entityLinks.linkToSingleResource(EquipmentClass.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "equipmentClasses", method = RequestMethod.PUT)
	public ResponseEntity<Resources<EquipmentClass>> createBulkEquipmentClass(
			@RequestBody List<EquipmentClass> equipmentClasses) {
		List<EquipmentClass> saved = repository.save(equipmentClasses);
		Resources<EquipmentClass> resources = new Resources<EquipmentClass>(saved);
		resources.add(entityLinks.linkToCollectionResource(EquipmentClass.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/equipmentClasses/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<EquipmentClass>> updateEquipmentClass(@PathVariable long id,@RequestBody EquipmentClass equipmentClass) {
		EquipmentClass saved = repository.save(equipmentClass);
		Resource<EquipmentClass>	resource = new Resource<EquipmentClass>(saved);
		resource.add(entityLinks.linkToSingleResource(EquipmentClass.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}
}
