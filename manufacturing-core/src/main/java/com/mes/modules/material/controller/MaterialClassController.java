package com.mes.modules.material.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.mes.dom.enumerations.application.AssemblyRelationShip;
import com.mes.dom.enumerations.application.AssemblyType;
import com.mes.dom.equipment.Equipment;
import com.mes.dom.material.MaterialClass;
import com.mes.modules.material.services.MaterialClassRepository;



@RepositoryRestController
public class MaterialClassController {
	@Autowired
	private MaterialClassRepository repository;
	@Autowired
	private EntityLinks entityLinks;

	@RequestMapping(value = "materialClasses/search", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> getMaterialClasses(
			@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "assemblyRelationship", required = false) AssemblyRelationShip assemblyRelationship,
			@RequestParam(value = "assemblyType", required = false) AssemblyType assemblyType,
			@RequestParam(value = "hierarchyScope", required = false) String hierarchyScope,
			@RequestParam(value = "location", required = false, defaultValue = "0") Long location
	) {
		if (
				(id == 0) && (code == null) && (description == null) && (assemblyRelationship == null)
				&& (assemblyType == null) && (hierarchyScope == null) && (location == 0)
		) {
			return ResponseEntity.ok(repository.findAll());
		}
		MaterialClass materialToSearch = new MaterialClass();
		if (location > 0) {
			Equipment locationParam = new Equipment();
			locationParam.setId(location);
			materialToSearch.setLocation(locationParam);
		}
		if (id > 0) {
			materialToSearch.setId(id);
		}
		materialToSearch.setCode(code);
		materialToSearch.setDescription(description);
		materialToSearch.setAssemblyRelationship(assemblyRelationship);
		materialToSearch.setAssemblyType(assemblyType);
		materialToSearch.setHierarchyScope(hierarchyScope);
		List<MaterialClass> ObjectsFound = repository.findAll(Example.of(materialToSearch));
		return ResponseEntity.ok(ObjectsFound);
	}

	@RequestMapping(value = "/materialClasses", method = RequestMethod.POST)
	public ResponseEntity<Resource<MaterialClass>> createMaterialClass(@RequestBody MaterialClass materialClass) {
		MaterialClass saved = repository.save(materialClass);
		Resource<MaterialClass> resource = new Resource<MaterialClass>(saved);
		resource.add(entityLinks.linkToSingleResource(MaterialClass.class, saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/materialClasses", method = RequestMethod.PUT)
	public ResponseEntity<List<MaterialClass>> createBulkMaterialClass(
			@RequestBody List<MaterialClass> materialClasses
	) {
		return ResponseEntity.ok(repository.save(materialClasses));
	}

	@RequestMapping(value = "/materialClasses/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<MaterialClass>> updateMaterialClass(
			@PathVariable long id,
			@RequestBody MaterialClass materialClass
		) {
		MaterialClass saved = repository.save(materialClass);
		Resource<MaterialClass> resource = new Resource<MaterialClass>(saved);
		resource.add(entityLinks.linkToSingleResource(MaterialClass.class, saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "materialClasses/{id}/hierarchy", method = RequestMethod.GET)
	public ResponseEntity<MaterialClass> getHierarchy(@PathVariable long id) {
		return ResponseEntity.ok(repository.findOne(id));
	}
}
