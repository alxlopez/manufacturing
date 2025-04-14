package com.mes.modules.material.controller;

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
import com.mes.dom.material.MaterialTestSpecification;
import com.mes.modules.material.services.MaterialTestSpecificationRepository;

@RepositoryRestController
public class MaterialTestSpecificationController {
	@Autowired
	private MaterialTestSpecificationRepository repository;
	@Autowired
	private EntityLinks entityLinks;	

	@RequestMapping(value = "materialTestSpecifications/search", method = RequestMethod.GET)
	public @ResponseBody List<MaterialTestSpecification> materialTestSpecificationFindByParams(
			@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "hierarchyScope", required = false) String hierarchyScope,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "location", required = false, defaultValue = "0") Long location,
			@RequestParam(value = "version", required = false) String version
	) {
		MaterialTestSpecification materialTestSpecificationToSearch = new MaterialTestSpecification();
		if (location > 0) {
			Equipment locationParam = new Equipment();
			locationParam.setId(location);
			materialTestSpecificationToSearch.setLocation(locationParam);
		}
		if (id > 0) {
			materialTestSpecificationToSearch.setId(id);
		}
		materialTestSpecificationToSearch.setCode(code);
		materialTestSpecificationToSearch.setDescription(description);
		materialTestSpecificationToSearch.setHierarchyScope(hierarchyScope);
		materialTestSpecificationToSearch.setName(name);
		materialTestSpecificationToSearch.setVersion(version);
		return repository.findAll(Example.of(materialTestSpecificationToSearch));
	}

	@RequestMapping(value = "/materialTestSpecifications", method = RequestMethod.POST)
	public ResponseEntity<Resource<MaterialTestSpecification>> createMaterialTestSpecification(
			@RequestBody MaterialTestSpecification materialTestSpecification
	) {
		MaterialTestSpecification saved = repository.save(materialTestSpecification);
		Resource<MaterialTestSpecification>	resource = new Resource<MaterialTestSpecification>(saved);
		resource.add(entityLinks.linkToSingleResource(MaterialTestSpecification.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/materialTestSpecifications", method = RequestMethod.PUT)
	public ResponseEntity<Resources<MaterialTestSpecification>> createBulkMaterialTestSpecification(
			@RequestBody List<MaterialTestSpecification> materialTestSpecifications) {
		List<MaterialTestSpecification> saved = repository.save(materialTestSpecifications);
		Resources<MaterialTestSpecification> resources = new Resources<MaterialTestSpecification>(saved);
		resources.add(entityLinks.linkToCollectionResource(MaterialTestSpecification.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/materialTestSpecifications/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<MaterialTestSpecification>> updateMaterialTestSpecification(
			@PathVariable long id, @RequestBody MaterialTestSpecification materialTestSpecification) {
		MaterialTestSpecification saved = repository.save(materialTestSpecification);
		Resource<MaterialTestSpecification>	resource = new Resource<MaterialTestSpecification>(saved);
		resource.add(entityLinks.linkToSingleResource(MaterialTestSpecification.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}
}
