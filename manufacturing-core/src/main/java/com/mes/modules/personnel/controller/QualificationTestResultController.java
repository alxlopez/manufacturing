package com.mes.modules.personnel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod; 
import com.mes.dom.personnel.QualificationTestResult;
import com.mes.modules.personnel.services.QualificationTestResultRepository;

@RepositoryRestController
public class QualificationTestResultController {
	@Autowired
	private QualificationTestResultRepository repository;
	@Autowired
	private EntityLinks entityLinks;

	@RequestMapping(value = "/qualificationTestResults", method = RequestMethod.POST)
	public ResponseEntity<Resource<QualificationTestResult>> createQualificationTestResult(@RequestBody QualificationTestResult qualificationTestResult) {
		QualificationTestResult saved = repository.save(qualificationTestResult);
		Resource<QualificationTestResult>	resource = new Resource<QualificationTestResult>(saved);
		resource.add(entityLinks.linkToSingleResource(QualificationTestResult.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/qualificationTestResults", method = RequestMethod.PUT)
	public ResponseEntity<Resources<QualificationTestResult>> createBulkQualificationTestResult(@RequestBody List<QualificationTestResult> qualificationTestResults) {
		List<QualificationTestResult> saved = repository.save(qualificationTestResults);
		Resources<QualificationTestResult> resources = new Resources<QualificationTestResult>(saved);
		resources.add(entityLinks.linkToCollectionResource(QualificationTestResult.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}
}
