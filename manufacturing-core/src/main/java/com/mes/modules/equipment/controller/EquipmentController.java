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
import com.mes.modules.equipment.services.EquipmentRepository;
import com.mes.modules.equipment.services.EquipmentService;

@RepositoryRestController
public class EquipmentController {
	@Autowired
	private EquipmentRepository repository;
	@Autowired
	private EntityLinks entityLinks;
	@Autowired
	private EquipmentService equipmentService;

	@RequestMapping(value = "equipments/{id}/hierarchy", method = RequestMethod.GET)
	public ResponseEntity<Equipment> getHierarchy(@PathVariable long id) {
		return ResponseEntity.ok(repository.findOne(id));
	}

	@RequestMapping(value = "equipments/search", method = RequestMethod.GET)
	public @ResponseBody List<Equipment> equipmentFindByParams(
			@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "equipmentLevel", required = false) EquipmentLevel equipmentLevel,
			@RequestParam(value = "hierarchyScope", required = false) String hierarchyScope,
			@RequestParam(value = "location", required = false, defaultValue = "0") Long location
	) {
		Equipment equipmentToSearch = new Equipment();
		if (location > 0) {
			Equipment locationParam = new Equipment();
			locationParam.setId(location);
			equipmentToSearch.setLocation(locationParam);
		}
		if (id > 0) {
			equipmentToSearch.setId(id);
		}
		equipmentToSearch.setCode(code);
		equipmentToSearch.setDescription(description);
		equipmentToSearch.setEquipmentLevel(equipmentLevel);
		equipmentToSearch.setHierarchyScope(hierarchyScope);
		return repository.findAll(Example.of(equipmentToSearch));
	}

	@RequestMapping(value = "/equipments", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Resource<Equipment>> saveEquipment(@RequestBody Equipment equipment) {
		Equipment saved = repository.save(equipment);
		Resource<Equipment> resource = new Resource<Equipment>(saved);
		resource.add(entityLinks.linkToSingleResource(Equipment.class, saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/equipments", method = RequestMethod.PUT)
	public ResponseEntity<Resources<Equipment>> createBulkEquipment(@RequestBody List<Equipment> equipments) {
		List<Equipment> saved = repository.save(equipments);
		Resources<Equipment> resources = new Resources<Equipment>(saved);
		resources.add(entityLinks.linkToCollectionResource(Equipment.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/equipments/{id}", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity<Resource<Equipment>> updateEquipment(@PathVariable long id,@RequestBody Equipment equipment) {
		Equipment saved = repository.save(equipment);
		Resource<Equipment> resource = new Resource<Equipment>(saved);
		resource.add(entityLinks.linkToSingleResource(Equipment.class, saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}
	
	@RequestMapping(value = "/equipments/relationShip", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Equipment> relationShip(
				@RequestParam(value = "codeBaseEquipment", required = true) String codeBaseEquipment,
				@RequestParam(value = "codeSearchedEquipment", required = true) String codeSearchedEquipment
	) {
		Equipment equipment = equipmentService.relationShip(codeBaseEquipment,codeSearchedEquipment);
		return ResponseEntity.ok(equipment);
	}
}
