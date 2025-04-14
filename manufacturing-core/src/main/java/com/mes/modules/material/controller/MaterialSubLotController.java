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
import com.mes.dom.enumerations.application.UnitOfMeasure;
import com.mes.dom.equipment.Equipment;
import com.mes.dom.material.MaterialLot;
import com.mes.dom.material.MaterialSubLot;
import com.mes.modules.material.services.MaterialSubLotRepository;

@RepositoryRestController
public class MaterialSubLotController {
	@Autowired
	private MaterialSubLotRepository repository;
	@Autowired
	private EntityLinks entityLinks;
	
	@RequestMapping(value = "materialSubLots/search", method = RequestMethod.GET)
	public @ResponseBody List<MaterialSubLot> equipmentFindByParams(
			@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "assemblyRelationship", required = false) AssemblyRelationShip assemblyRelationship,
			@RequestParam(value = "assemblyType", required = false) AssemblyType assemblyType,
			@RequestParam(value = "quantity", required = false) Float quantity,
			@RequestParam(value = "status", required = false) MaterialLotStatus status,
			@RequestParam(value = "unitOfMeasure", required = false) UnitOfMeasure unitOfMeasure,
			@RequestParam(value = "materialLot", required = false) Long materialLot,
			@RequestParam(value = "storageLocation", required = false) Long storageLocation,
			@RequestParam(value = "hierarchyScope", required = false) String hierarchyScope,
			@RequestParam(value = "location", required = false, defaultValue = "0") Long location
	) {
		MaterialSubLot materialToSearch = new MaterialSubLot();
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
		if (materialLot > 0) {
			MaterialLot materialLotParam = new MaterialLot();
			materialLotParam.setId(materialLot);
			materialToSearch.setMaterialLot(materialLotParam);
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

	@RequestMapping(value = "/materialSubLots", method = RequestMethod.POST)
	public ResponseEntity<Resource<MaterialSubLot>> createMaterialLot(@RequestBody MaterialSubLot materialSubLot) {
		MaterialSubLot saved = repository.save(materialSubLot);
		Resource<MaterialSubLot>	resource = new Resource<MaterialSubLot>(saved);
		resource.add(entityLinks.linkToSingleResource(MaterialSubLot.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/materialSubLots", method = RequestMethod.PUT)
	public ResponseEntity<Resources<MaterialSubLot>> updateBulkMaterialLot(
			@RequestBody List<MaterialSubLot> materialSubLots
	) {
		List<MaterialSubLot> saved = repository.save(materialSubLots);
		Resources<MaterialSubLot> resources = new Resources<MaterialSubLot>(saved);
		resources.add(entityLinks.linkToCollectionResource(MaterialSubLot.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/materialSubLots/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<MaterialSubLot>> updateMaterialLot(@PathVariable long id,@RequestBody MaterialSubLot materialSubLot) {
		MaterialSubLot saved = repository.save(materialSubLot);
		Resource<MaterialSubLot>	resource = new Resource<MaterialSubLot>(saved);
		resource.add(entityLinks.linkToSingleResource(MaterialSubLot.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}
}
