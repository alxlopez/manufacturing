package com.mes.modules.material.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
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
import com.mes.dom.material.*;
import com.mes.modules.material.controller.utilities.MaterialDefinitionResourceAssembler;
import com.mes.modules.material.services.MaterialDefinitionRepository;
import com.mes.query.translators.QueryMaterialDefinition;
import com.mes.query.wrappers.Criteria;
import com.mes.query.wrappers.Link;
import com.mes.query.wrappers.QueryBody;

@RepositoryRestController
public class MaterialDefinitionController {
	
	@Autowired
	private MaterialDefinitionRepository repository;
	@Autowired
	private EntityLinks entityLinks;
	@PersistenceContext
    private EntityManager em;
	@Autowired
	private MaterialDefinitionResourceAssembler materialDefinitionResourceAssembler;
	@Autowired
	private PagedResourcesAssembler<MaterialDefinition>  pagedResourcesAssembler;

	@RequestMapping(value = "materialDefinitions/model", method = RequestMethod.GET)
	public ResponseEntity<List<MaterialDefinition>> getModel() {
		return ResponseEntity.ok(repository.findAll());
	}
	
	@RequestMapping(value = "/materialDefinitions/query", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> test(Pageable pageable, @RequestBody QueryBody query) {
		QueryMaterialDefinition qmd = new QueryMaterialDefinition(em);
		for (Link join: query.getJoins()) {
			qmd.join(join.getParameter(), join.getEntity());
		}
		for (Criteria criteria: query.getFilters()) {
			qmd.filter(criteria.getKey(), criteria.getOperation(), criteria.getValue());
		}
		long size = qmd.size();
		qmd.page(pageable);
		List<MaterialDefinition> materialDefinitions = qmd.run();
		Page<MaterialDefinition> pages = new PageImpl<MaterialDefinition>(materialDefinitions, pageable, size);
		PagedResources<?> resources = (size == 0 || (pageable.getPageSize() * pages.getNumber()) > size)?
				pagedResourcesAssembler.
				toEmptyResource(pages, MaterialDefinition.class, entityLinks.linkFor(MaterialDefinition.class).withSelfRel()):
					pagedResourcesAssembler.
					toResource(pages, materialDefinitionResourceAssembler);
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "materialDefinitions/search", method = RequestMethod.GET)
	public @ResponseBody List<MaterialDefinition> materialFindByParams(
			@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "assemblyRelationship", required = false) AssemblyRelationShip assemblyRelationship,
			@RequestParam(value = "assemblyType", required = false) AssemblyType assemblyType,
			@RequestParam(value = "hierarchyScope", required = false) String hierarchyScope,
			@RequestParam(value = "location", required = false, defaultValue = "0") Long location
	) {
		MaterialDefinition materialToSearch = new MaterialDefinition();
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
		return repository.findAll(Example.of(materialToSearch));
	}

	@RequestMapping(value = "/materialDefinitions", method = RequestMethod.POST)
	public ResponseEntity<Resource<MaterialDefinition>> createMaterialDefinition(
			@RequestBody MaterialDefinition materialDefinition
	) {
		MaterialDefinition saved = repository.save(materialDefinition);
		Resource<MaterialDefinition>	resource = new Resource<MaterialDefinition>(saved);
		resource.add(entityLinks.linkToSingleResource(MaterialDefinition.class,saved.getId()).withSelfRel());		
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/materialDefinitions", method = RequestMethod.PUT)
	public ResponseEntity<Resources<MaterialDefinition>> createBulkMaterialDefinition(
			@RequestBody List<MaterialDefinition> materialDefinitions
	) {
		List<MaterialDefinition> saved = repository.save(materialDefinitions);
		Resources<MaterialDefinition> resources = new Resources<MaterialDefinition>(saved);
		resources.add(entityLinks.linkToCollectionResource(MaterialDefinition.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/materialDefinitions/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<MaterialDefinition>> updateMaterialDefinition(
			@PathVariable long id, @RequestBody MaterialDefinition materialDefinition
	) {
		MaterialDefinition saved = repository.save(materialDefinition);
		Resource<MaterialDefinition>	resource = new Resource<MaterialDefinition>(saved);
		resource.add(entityLinks.linkToSingleResource(MaterialDefinition.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}
}
