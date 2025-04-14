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

import com.mes.dom.equipment.Equipment;
import com.mes.dom.equipment.EquipmentCapabilityTestSpecification;
import com.mes.modules.equipment.services.EquipmentCapabilityTestRepository;


@RepositoryRestController
public class EquipmentCapabilityTestController {
	@Autowired
	private EquipmentCapabilityTestRepository repository;
	@Autowired
	private EntityLinks entityLinks;
	
	@RequestMapping(value = "equipmentCapabilityTests/search", method = RequestMethod.GET)
	public @ResponseBody List<EquipmentCapabilityTestSpecification> equipmentCapabilityTestSpecificationFindByParams(
			@RequestParam(value = "id", required=false, defaultValue="0") Long id,
			@RequestParam(value = "code", required=false) String code,
			@RequestParam(value = "description", required=false) String description,
			@RequestParam(value = "hierarchyScope", required=false) String hierarchyScope,
			@RequestParam(value = "name", required=false) String name,
			@RequestParam(value = "location", required=false, defaultValue="0") Long location,
			@RequestParam(value = "version", required=false) String version	
	) {
		EquipmentCapabilityTestSpecification equipmentCapabilityTestSpecificationToSearch = new EquipmentCapabilityTestSpecification();
		if (location > 0) {
			Equipment locationParam = new Equipment();
			locationParam.setId(location);
			equipmentCapabilityTestSpecificationToSearch.setLocation(locationParam);
		}
		if(id > 0) {
			equipmentCapabilityTestSpecificationToSearch.setId(id);	
		}
		equipmentCapabilityTestSpecificationToSearch.setCode(code);
		equipmentCapabilityTestSpecificationToSearch.setDescription(description);
		equipmentCapabilityTestSpecificationToSearch.setHierarchyScope(hierarchyScope);
		equipmentCapabilityTestSpecificationToSearch.setName(name);
		equipmentCapabilityTestSpecificationToSearch.setVersion(version);
		return repository.findAll(Example.of(equipmentCapabilityTestSpecificationToSearch));
	}

	@RequestMapping(value = "/equipmentCapabilityTests", method = RequestMethod.POST)
	public ResponseEntity<Resource<EquipmentCapabilityTestSpecification>> createEquipmentCapabilityTest(@RequestBody EquipmentCapabilityTestSpecification equipmentCapabilityTest) {
		EquipmentCapabilityTestSpecification saved = repository.save(equipmentCapabilityTest);
		Resource<EquipmentCapabilityTestSpecification>	resource = new Resource<EquipmentCapabilityTestSpecification>(saved);
		resource.add(entityLinks.linkToSingleResource(EquipmentCapabilityTestSpecification.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/equipmentCapabilityTests", method = RequestMethod.PUT)
	public ResponseEntity<Resources<EquipmentCapabilityTestSpecification>> createBulkEquipmentCapabilityTest(@RequestBody List<EquipmentCapabilityTestSpecification> equipmentCapabilityTests) {
		List<EquipmentCapabilityTestSpecification> saved = repository.save(equipmentCapabilityTests);
		Resources<EquipmentCapabilityTestSpecification> resources = new Resources<EquipmentCapabilityTestSpecification>(saved);
		resources.add(entityLinks.linkToCollectionResource(EquipmentCapabilityTestSpecification.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/equipmentCapabilityTests/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<EquipmentCapabilityTestSpecification>> updateEquipmentCapabilityTest(@PathVariable long id,@RequestBody EquipmentCapabilityTestSpecification equipmentCapabilityTest) {
		EquipmentCapabilityTestSpecification saved = repository.save(equipmentCapabilityTest);
		Resource<EquipmentCapabilityTestSpecification>	resource = new Resource<EquipmentCapabilityTestSpecification>(saved);
		resource.add(entityLinks.linkToSingleResource(EquipmentCapabilityTestSpecification.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}	
}
