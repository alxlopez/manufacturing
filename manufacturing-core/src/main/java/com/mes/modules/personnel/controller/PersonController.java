package com.mes.modules.personnel.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.mes.dom.personnel.Person;
import com.mes.dom.personnel.PersonnelClass;
import com.mes.dom.personnel.QPerson;
import com.mes.dom.personnel.QPersonnelClass;
import com.mes.modules.personnel.controller.utilities.PersonnelResourceAssembler;
import com.mes.modules.personnel.services.PersonRepository;
import com.mes.query.translators.QueryPerson;
import com.mes.query.wrappers.Criteria;
import com.mes.query.wrappers.Link;
import com.mes.query.wrappers.QueryBody;
import com.querydsl.jpa.impl.JPAQuery;

@RepositoryRestController
public class PersonController {
	@PersistenceContext
    private EntityManager em;
	@Autowired
	private PersonRepository repository;
	@Autowired
	private PersonnelResourceAssembler personnelResourceAssembler;
	@Autowired
	private PagedResourcesAssembler<Person> pagedResourcesAssembler;
	@Autowired
	private EntityLinks entityLinks;
	

	@RequestMapping(value = "/personnel", method = RequestMethod.POST)
	public ResponseEntity<Resource<Person>> createPerson(@RequestBody Person person) {
		Person saved = repository.save(person);
		Resource<Person>	resource = new Resource<Person>(saved);
		resource.add(entityLinks.linkToSingleResource(Person.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}
	
	@RequestMapping(value = "/personnel/query", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> test(Pageable pageable, @RequestBody QueryBody query) {
		QueryPerson qp = new QueryPerson(em);
		for (Link join: query.getJoins()) {
			qp.join(join.getParameter(), join.getEntity());
		}
		for (Criteria criteria: query.getFilters()) {
			qp.filter(criteria.getKey(), criteria.getOperation(), criteria.getValue());
		}
		long size = qp.size();
		qp.page(pageable);
		List<Person> personnel = qp.run();
		Page<Person> pages = new PageImpl<Person>(personnel, pageable, size);
		PagedResources<?> resources = (size == 0 || (pageable.getPageSize() * pages.getNumber()) > size)?
				pagedResourcesAssembler.
				toEmptyResource(pages, Person.class, entityLinks.linkFor(Person.class).withSelfRel()):
					pagedResourcesAssembler.
					toResource(pages, personnelResourceAssembler);
		return ResponseEntity.ok(resources);
	}
	

	@RequestMapping(value = "/personnel/search", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> gerPerson(
			Pageable pageable,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "personnelClass", required = false) String personnelClass
	) {
		QPerson qPerson = QPerson.person;
		JPAQuery<Person> query = new JPAQuery<Person>(em);
		query.from(qPerson);
		if (code != null) {
			query.where(qPerson.code.eq(code));
		}
		if (description != null) {
			query.where(qPerson.description.upper().like("%" + description.toUpperCase() + "%"));
		}
		if (personnelClass != null) {
			QPersonnelClass qPersonnelClass = QPersonnelClass.personnelClass;
			query.innerJoin(qPerson.personnelClasses, qPersonnelClass);
			query.where(qPersonnelClass.code.eq(personnelClass));
		}
		long size = query.fetchCount();
		if (size == 0) {
			Page<Person> pages = new PageImpl<Person>(new ArrayList<Person>(), pageable, 0);
			return ResponseEntity.ok(pagedResourcesAssembler.
					toEmptyResource(pages, Person.class, entityLinks.linkFor(Person.class).withSelfRel())
			);
		}
		long start = pageable.getOffset();
		long end = (start + pageable.getPageSize()) > size ? size : (start + pageable.getPageSize());
		List<Person> persons = query.offset(start).limit(end).orderBy(qPerson.id.desc()).fetch();
		Page<Person> pages = new PageImpl<Person>(persons, pageable, size);
		PagedResources<?> resources = pagedResourcesAssembler.toResource(pages, personnelResourceAssembler);
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/personnel", method = RequestMethod.PUT)
	public ResponseEntity<Resources<Person>> createBulkPerson(@RequestBody List<Person> personnel) {
		List<Person> saved = repository.save(personnel);
		Resources<Person> resources = new Resources<Person>(saved);
		resources.add(entityLinks.linkToCollectionResource(Person.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@RequestMapping(value = "/personnel/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<Person>> updateJobOrder(@PathVariable long id, @RequestBody Person person) {
		Person saved = repository.save(person);
		Resource<Person> resource = new Resource<Person>(saved);
		resource.add(entityLinks.linkToSingleResource(Person.class, saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/personnel/{id}/personnelClasses/{idClass}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> unlinkPersonnelClass(@PathVariable long id, @PathVariable long idClass) {
		Person person = repository.findById(id);
		Collection<PersonnelClass> classes = person.getPersonnelClasses();
		for (PersonnelClass personClass: classes) {
			if (personClass.getId().equals(idClass)) {
				classes.remove(personClass);
				repository.save(person);
				return ResponseEntity.noContent().build();
			}
		}
		return ResponseEntity.notFound().build();
	}
}
