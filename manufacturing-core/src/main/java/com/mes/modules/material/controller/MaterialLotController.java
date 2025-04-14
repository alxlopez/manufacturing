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

import com.mes.dom.enumerations.application.AssemblyRelationShip;
import com.mes.dom.enumerations.application.AssemblyType;
import com.mes.dom.enumerations.application.MaterialLotStatus;
import com.mes.dom.equipment.Equipment;
import com.mes.dom.material.*;
import com.mes.modules.material.controller.utilities.MaterialLotResourceAssembler;
import com.mes.modules.material.services.MaterialLotRepository;
import com.mes.modules.material.services.MaterialLotService;

@RepositoryRestController
public class MaterialLotController {
	@Autowired
	private MaterialLotRepository repository;
	@Autowired
	private EntityLinks entityLinks;
	@Autowired
	private MaterialLotService materialLotService;
	@Autowired
	private MaterialLotResourceAssembler materialLotResourceAssembler;
	
	@RequestMapping(value = "materialLots/search", method = RequestMethod.GET)
	public @ResponseBody List<MaterialLot> equipmentFindByParams(
			@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "assemblyRelationship", required = false) AssemblyRelationShip assemblyRelationship,
			@RequestParam(value = "assemblyType", required = false) AssemblyType assemblyType,
			@RequestParam(value = "quantity", required = false, defaultValue = "0") Float quantity,
			@RequestParam(value = "status", required = false) MaterialLotStatus status,
			@RequestParam(value = "unitOfMeasure", required = false) String unitOfMeasure,
			@RequestParam(value = "materialDefinition", required = false, defaultValue = "0") Long materialDefinition,
			@RequestParam(value = "storageLocation", required = false, defaultValue = "0") Long storageLocation,
			@RequestParam(value = "hierarchyScope", required = false) String hierarchyScope,
			@RequestParam(value = "location", required = false, defaultValue = "0") Long location
	) {
		MaterialLot materialToSearch = new MaterialLot();
		if (location > 0) {
			Equipment locationParam = new Equipment();
			locationParam.setId(location);
			materialToSearch.setLocation(locationParam);
		}
		if (storageLocation > 0) {
			Equipment storageLocationParam = new Equipment();
			storageLocationParam.setId(storageLocation);
			materialToSearch.setLocation(storageLocationParam);
		}
		if (id > 0) {
			materialToSearch.setId(id);
		}
		if (quantity != null) {
			if (quantity > 0) {
				materialToSearch.setQuantity(quantity);
			}
		}
		if (materialDefinition > 0) {
			MaterialDefinition materialDefinitionParam = new MaterialDefinition();
			materialDefinitionParam.setId(materialDefinition);
			materialToSearch.setMaterialDefinition(materialDefinitionParam);
		}
		materialToSearch.setCode(code);
		materialToSearch.setDescription(description);
		materialToSearch.setAssemblyRelationship(assemblyRelationship);
		materialToSearch.setAssemblyType(assemblyType);
		materialToSearch.setHierarchyScope(hierarchyScope);
		materialToSearch.setStatus(status);
		materialToSearch.setUnitOfMeasure(unitOfMeasure);
		return repository.findAll(Example.of(materialToSearch));
	}

	@RequestMapping(value = "/materialLots", method = RequestMethod.POST)
	public ResponseEntity<Resource<MaterialLot>> createMaterialLot(@RequestBody MaterialLot materialLot) {
		MaterialLot saved = repository.save(materialLot);		
		return ResponseEntity.ok(materialLotResourceAssembler.toResource(saved));		
	}

	@RequestMapping(value = "/materialLots", method = RequestMethod.PUT)
	public ResponseEntity<Resources<MaterialLot>> createBulkMaterialLot(@RequestBody List<MaterialLot> materialLots) {
		List<MaterialLot> saved = repository.save(materialLots);
		Resources<MaterialLot> resources = new Resources<MaterialLot>(saved);
		resources.add(entityLinks.linkToCollectionResource(MaterialLot.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/materialLots/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<MaterialLot>> updateMaterialLot(@PathVariable long id, @RequestBody MaterialLot materialLot) {
		MaterialLot saved = repository.save(materialLot);
		return ResponseEntity.ok(materialLotResourceAssembler.toResource(saved));
	}

	@RequestMapping(value = "/materialLots/{id}/storagelocation", method = RequestMethod.PATCH)
	public ResponseEntity<Resource<MaterialLot>> patchMaterialLot(
			@PathVariable(value = "id") long materialLotId,
			@RequestBody MaterialLot materialLot
	) {
		Equipment storageLocation = materialLot.getStorageLocation();
		MaterialLot saved = materialLotService.updateStorageLocation(materialLotId, storageLocation.getId());		
		return ResponseEntity.ok(materialLotResourceAssembler.toResource(saved));
	}
}
