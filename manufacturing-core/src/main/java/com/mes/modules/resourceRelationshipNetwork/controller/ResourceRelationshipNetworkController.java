
package com.mes.modules.resourceRelationshipNetwork.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
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
import com.mes.dom.enumerations.application.RelationshipForm;
import com.mes.dom.enumerations.application.RelationshipType;
import com.mes.dom.equipment.Equipment;
import com.mes.dom.resourceRelationshipNetwork.ResourceNetworkConnection;
import com.mes.dom.resourceRelationshipNetwork.ResourceRelationshipNetwork;
import com.mes.modules.resourceRelationshipNetwork.services.ResourceNetworkConnectionRepository;
import com.mes.modules.resourceRelationshipNetwork.services.ResourceRelationshipNetworkRepository;

@RepositoryRestController
public class ResourceRelationshipNetworkController {
	@Autowired
	private EntityLinks entityLinks;
	@Autowired
	private ResourceRelationshipNetworkRepository repository;
	@Autowired
	private ResourceNetworkConnectionRepository repositoryConnection;

	@RequestMapping(value = "resourceRelationshipNetworks/{id}/hierarchy", method = RequestMethod.GET)
	public ResponseEntity<ResourceRelationshipNetwork> getHierarchy(@PathVariable Long id) {
		return ResponseEntity.ok(repository.findOne(id));
	}

	@RequestMapping(value = "resourceRelationshipNetworks/search", method = RequestMethod.GET)
	public @ResponseBody List<ResourceRelationshipNetwork> resourceRelationshipNetworkFindByParams(
			@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "hierarchyScope", required = false) Long hierarchyScope,
			@RequestParam(value = "published_date", required = false) Date publishedDate,
			@RequestParam(value = "relationshipForm", required = false) RelationshipForm relationshipForm,
			@RequestParam(value = "relationshipType", required = false) RelationshipType relationshipType
	) {
		ResourceRelationshipNetwork resourceRelationshipNetworkToSearch = new ResourceRelationshipNetwork();
		if (hierarchyScope != null) {// Used to validate a default value obtained  in queryString.
			if (hierarchyScope > 0)	{
				Equipment locationParam = new Equipment();
				locationParam.setId(hierarchyScope);
				resourceRelationshipNetworkToSearch.setHierarchyScope(locationParam);
			}
		}
		if (id != null) {
			if (id > 0) {
				resourceRelationshipNetworkToSearch.setId(id);
			}
		}
		resourceRelationshipNetworkToSearch.setCode(code);
		resourceRelationshipNetworkToSearch.setDescription(description);
		resourceRelationshipNetworkToSearch.setPublishedDate(publishedDate);
		resourceRelationshipNetworkToSearch.setRelationshipForm(relationshipForm);
		resourceRelationshipNetworkToSearch.setRelationshipType(relationshipType);
		return repository.findAll(Example.of(resourceRelationshipNetworkToSearch));
	}

	@RequestMapping(value = "resourceRelationshipNetworks/search/from", method = RequestMethod.GET)
	public @ResponseBody Collection<ResourceNetworkConnection> resourceNetworkConnectionFindByFromParams(
			@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "hierarchyScope", required = false) Long hierarchyScope,
			@RequestParam(value = "published_date", required = false) Date publishedDate,
			@RequestParam(value = "relationshipForm", required = false) RelationshipForm relationshipForm,
			@RequestParam(value = "relationshipType", required = false) RelationshipType relationshipType,
			@RequestParam(value = "fromResourceCode", required = true) String fromResourceCode
	) {
		ResourceRelationshipNetwork resourceRelationshipNetworkToSearch = new ResourceRelationshipNetwork();
		List<ResourceRelationshipNetwork> resourceRelationshipNetworkFound = null;
		Collection<ResourceNetworkConnection> resourceNetworkConnectionFound = null;
		Collection<ResourceNetworkConnection> resourceNetworkConnectionResulset = null;
		if (hierarchyScope != null) {
			if (hierarchyScope > 0) {
				Equipment locationParam = new Equipment();
				locationParam.setId(hierarchyScope);
				resourceRelationshipNetworkToSearch.setHierarchyScope(locationParam);
			}
		}
		if (id != null) {
			if (id > 0) {
				resourceRelationshipNetworkToSearch.setId(id);
			}
		}
		resourceRelationshipNetworkToSearch.setCode(code);
		resourceRelationshipNetworkToSearch.setDescription(description);
		resourceRelationshipNetworkToSearch.setPublishedDate(publishedDate);
		resourceRelationshipNetworkToSearch.setRelationshipForm(relationshipForm);
		resourceRelationshipNetworkToSearch.setRelationshipType(relationshipType);
		resourceRelationshipNetworkFound = repository.findAll(Example.of(resourceRelationshipNetworkToSearch));
		if (resourceRelationshipNetworkFound != null) {
			resourceNetworkConnectionFound = new ArrayList<ResourceNetworkConnection>();
			for (ResourceRelationshipNetwork resourceRelationshipNetworktoDive : resourceRelationshipNetworkFound) {
				resourceNetworkConnectionFound
						.addAll(resourceRelationshipNetworktoDive.getResourceNetworkConnections());
			}
			resourceNetworkConnectionResulset = repositoryConnection
					.findByNetworkConnectionsAndFromResourceReference(resourceNetworkConnectionFound, fromResourceCode);
		}
		return resourceNetworkConnectionResulset;
	}

	@RequestMapping(value = "resourceRelationshipNetworks/search/fromTo", method = RequestMethod.GET)
	public @ResponseBody Collection<ResourceNetworkConnection> resourceNetworkConnectionFindByFromToParams(
			@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "hierarchyScope", required = false) Long hierarchyScope,
			@RequestParam(value = "published_date", required = false) Date publishedDate,
			@RequestParam(value = "relationshipForm", required = false) RelationshipForm relationshipForm,
			@RequestParam(value = "relationshipType", required = false) RelationshipType relationshipType,
			@RequestParam(value = "fromResourceCode", required = true) String fromResourceCode,
			@RequestParam(value = "toResourceCode", required = true) String toResourceCode
	) {
		ResourceRelationshipNetwork resourceRelationshipNetworkToSearch = new ResourceRelationshipNetwork();
		List<ResourceRelationshipNetwork> resourceRelationshipNetworkFound = null;
		Collection<ResourceNetworkConnection> resourceNetworkConnectionFound = null;
		Collection<ResourceNetworkConnection> resourceNetworkConnectionResulset = null;
		if (hierarchyScope != null) {
			if (hierarchyScope > 0)	{
				Equipment locationParam = new Equipment();
				locationParam.setId(hierarchyScope);

				resourceRelationshipNetworkToSearch.setHierarchyScope(locationParam);
			}
		}
		if (id != null) {
			if (id > 0) {
				resourceRelationshipNetworkToSearch.setId(id);
			}
		}
		resourceRelationshipNetworkToSearch.setCode(code);
		resourceRelationshipNetworkToSearch.setDescription(description);
		resourceRelationshipNetworkToSearch.setPublishedDate(publishedDate);
		resourceRelationshipNetworkToSearch.setRelationshipForm(relationshipForm);
		resourceRelationshipNetworkToSearch.setRelationshipType(relationshipType);
		resourceRelationshipNetworkFound = repository.findAll(Example.of(resourceRelationshipNetworkToSearch));
		if (resourceRelationshipNetworkFound != null) {
			resourceNetworkConnectionFound = new ArrayList<ResourceNetworkConnection>();
			for (ResourceRelationshipNetwork resourceRelationshipNetworktoDive : resourceRelationshipNetworkFound) {
				resourceNetworkConnectionFound
						.addAll(resourceRelationshipNetworktoDive.getResourceNetworkConnections());
			}
			resourceNetworkConnectionResulset = repositoryConnection.findByNetworkConnectionsAndFromToResourceReference(
					resourceNetworkConnectionFound, fromResourceCode, toResourceCode);
		}
		return resourceNetworkConnectionResulset;
	}

	@RequestMapping(value = "/resourceRelationshipNetworks", method = RequestMethod.POST)
	public ResponseEntity<Resource<ResourceRelationshipNetwork>> createResourceRelationshipNetwork(
			@RequestBody ResourceRelationshipNetwork resourceRelationshipNetwork) {
		ResourceRelationshipNetwork saved = repository.save(resourceRelationshipNetwork);
		Resource<ResourceRelationshipNetwork> resource = new Resource<ResourceRelationshipNetwork>(saved);
		resource.add(entityLinks.linkToSingleResource(ResourceRelationshipNetwork.class, saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/resourceRelationshipNetworks/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<ResourceRelationshipNetwork>> updateResourceRelationshipNetwork(
			@PathVariable long id, @RequestBody ResourceRelationshipNetwork resourceRelationshipNetwork) {
		ResourceRelationshipNetwork saved = repository.save(resourceRelationshipNetwork);
		Resource<ResourceRelationshipNetwork> resource = new Resource<ResourceRelationshipNetwork>(saved);
		resource.add(entityLinks.linkToSingleResource(Equipment.class, saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/resourceRelationshipNetworks", method = RequestMethod.PUT)
	public ResponseEntity<List<ResourceRelationshipNetwork>> createBulkResourceRelationshipNetwork(
			@RequestBody List<ResourceRelationshipNetwork> resourceRelationshipNetworks) {
		return ResponseEntity.ok(repository.save(resourceRelationshipNetworks));
	}
}
