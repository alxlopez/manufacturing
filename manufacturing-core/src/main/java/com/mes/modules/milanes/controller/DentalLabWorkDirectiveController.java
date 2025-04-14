package com.mes.modules.milanes.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mes.dom.enumerations.application.WorkTypes;
import com.mes.dom.personnel.QPerson;
import com.mes.dom.workDirective.QWorkDirective;
import com.mes.dom.workDirective.QWorkDirectiveParameter;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.modules.workDirective.controller.utilities.WorkDirectiveResourceAssembler;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;

@RestController 
@RequestMapping(path = "/milanes")
public class DentalLabWorkDirectiveController {
	@PersistenceContext
    private EntityManager em;	
	@Autowired
	private RepositoryEntityLinks entityLinks;
	@Autowired
	private WorkDirectiveResourceAssembler workDirectiveResourceAssembler;
	@Autowired
	private PagedResourcesAssembler<WorkDirective>  pagedResourcesAssembler;

	@RequestMapping(value = "query/workDirectives", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> jobOrderFindByParams(
			Pageable pageable,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "jobOrder", required = false) Long jobOrder,
			@RequestParam(value = "workRequest", required = false) Long workRequest,
			@RequestParam(value = "hierarchyScope", required = false) String hierarchyScope,
			@RequestParam(value = "customer", required = false) String customer,
			@RequestParam(value = "patient", required = false) String patient,
			@RequestParam(value = "workType", required = false) String workType,
			@RequestParam(value = "hasWorkflow", required = false) boolean hasWorkflow
	) {
		JPAQuery<WorkDirective> query = this.search(id,workRequest,jobOrder, hierarchyScope, customer, patient, workType, hasWorkflow);
		long size = query.fetchCount();
		if (size == 0) {
			Page<WorkDirective> pages = new PageImpl<WorkDirective>(new ArrayList<WorkDirective>(), pageable, 0);
			return ResponseEntity.ok(pagedResourcesAssembler.
					toEmptyResource(pages, WorkDirective.class, entityLinks.linkFor(WorkDirective.class).withSelfRel())
			);
		}
		long start = pageable.getOffset();
		long end = (start + pageable.getPageSize()) > size ? size : (start + pageable.getPageSize());
		List<WorkDirective> jobOrders = query.offset(start).limit(end).fetch();
		Page<WorkDirective> pages = new PageImpl<WorkDirective>(jobOrders, pageable, size);
		PagedResources<?> resources = pagedResourcesAssembler.toResource(pages, workDirectiveResourceAssembler);
		return ResponseEntity.ok(resources);
	}
	
	@InitBinder
	public void initBinder(final WebDataBinder binder){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	
	public JPAQuery<WorkDirective> search(Long id,Long workRequest,Long jobOrder, String hierarchyScope, String customer, String patient, String workType, Boolean hasWorkflow) {
		QWorkDirective qWorkDirective = QWorkDirective.workDirective;
		JPAQuery<WorkDirective> query = new JPAQuery<WorkDirective>(em);
		query.from(qWorkDirective);
		if (id != null) {
			query.where(qWorkDirective.id.eq(id));
		}
		if (workRequest != null) {
			query.where(qWorkDirective.jobOrder.workRequest.id.eq(workRequest));
		}
		if (id != null) {
			query.where(qWorkDirective.id.eq(id));
		}
		if (workType != null) {
			query.where(qWorkDirective.workType.eq(WorkTypes.valueOf(workType)));
		}
		if (hasWorkflow != null) {
			if (hasWorkflow) {
				query.where(qWorkDirective.workflowSpecificationId.isNotNull());
			} else {
				query.where(qWorkDirective.workflowSpecificationId.isNull());
			}
		}
		if (hierarchyScope != null) {
			query.where(qWorkDirective.hierarchyScope.code.eq(hierarchyScope));
		}
		if (jobOrder != null) {
			query.where(qWorkDirective.jobOrder.id.eq(jobOrder));
		}
		
		if (customer != null || patient != null) {
			query.where(this.searchParams(customer, patient, qWorkDirective));
		}
		query.orderBy(qWorkDirective.id.desc());
		return query;
	}

	private BooleanExpression searchParams(String customer, String patient, QWorkDirective qWorkDirective) {
		QWorkDirectiveParameter qWorkDirectiveParameter = QWorkDirectiveParameter.workDirectiveParameter;
		BooleanExpression where = qWorkDirectiveParameter.workDirective.id.eq(qWorkDirective.id);
		long cantidad = 1;
		if (patient != null) {
			BooleanExpression whereGroup = qWorkDirectiveParameter.code.eq("patientCustomer")
				.and(qWorkDirectiveParameter.value.upper().like("%" + patient.toUpperCase() + "%"));
			if (customer != null) {
				whereGroup = whereGroup.or(this.searchCustomer(customer, qWorkDirectiveParameter));
				cantidad++;
			}
			where = where.andAnyOf(whereGroup);
		} else if (customer != null) {
			where = where.and(this.searchCustomer(customer, qWorkDirectiveParameter));
		}
		JPQLQuery<Long> subQuery = JPAExpressions.selectFrom(qWorkDirectiveParameter)
		.where(where).select(qWorkDirectiveParameter.id.count());
		System.out.println(cantidad);
		return subQuery.eq(cantidad);
	}

	private BooleanExpression searchCustomer(String customer, QWorkDirectiveParameter qWorkDirectiveParameter) {
		QPerson qPerson = QPerson.person;
		JPQLQuery<String> subQuery = JPAExpressions.selectFrom(qPerson)
				.where(qPerson.description.upper().like("%"+customer.toUpperCase()+"%"))
				.select(qPerson.id.stringValue());
		return qWorkDirectiveParameter.code.eq("idCustomer").and(qWorkDirectiveParameter.value.in(subQuery));
	}
}
