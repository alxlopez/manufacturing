package com.mes.modules.milanes.controller;

import java.sql.Timestamp;
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

import com.mes.dom.enumerations.application.DispatchStatus;
import com.mes.dom.personnel.QPerson;
import com.mes.dom.workSchedule.JobOrder;
import com.mes.dom.workSchedule.QJobOrder;
import com.mes.dom.workSchedule.QJobOrderParameter;
import com.mes.modules.workSchedule.controller.utilities.JobOrderResourceAssembler;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;

@RestController
@RequestMapping(path = "/milanes")
public class DentalLabJobOrderController {
	@PersistenceContext
    private EntityManager em;
	@Autowired
	private RepositoryEntityLinks entityLinks;
	@Autowired
	private JobOrderResourceAssembler jobOrderResourceAssembler;
	@Autowired
	private PagedResourcesAssembler<JobOrder>  pagedResourcesAssembler;

	@RequestMapping(value = "query/jobOrders", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> jobOrderFindByParams(
			Pageable pageable,
			@RequestParam(value = "workRequest", required = false) Long workRequest,
			@RequestParam(value = "hierarchyScope", required = false) String hierarchyScope,
			@RequestParam(value = "customer", required = false) String customer,
			@RequestParam(value = "patient", required = false) String patient,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "workType", required = false) String workType,
			@RequestParam(value = "processingType", required = false) String processingType
	) {
		JPAQuery<JobOrder> query = this.search(workRequest, hierarchyScope, startTime, endTime, status, customer, patient, workType, processingType);
		long size = query.fetchCount();
		if (size == 0) {
			Page<JobOrder> pages = new PageImpl<JobOrder>(new ArrayList<JobOrder>(), pageable, 0);
			return ResponseEntity.ok(pagedResourcesAssembler.
					toEmptyResource(pages, JobOrder.class, entityLinks.linkFor(JobOrder.class).withSelfRel())
			);
		}
		long start = pageable.getOffset();
		long end = (start + pageable.getPageSize()) > size ? size : (start + pageable.getPageSize());
		List<JobOrder> jobOrders = query.offset(start).limit(end).fetch();
		Page<JobOrder> pages = new PageImpl<JobOrder>(jobOrders, pageable, size);
		PagedResources<?> resources = pagedResourcesAssembler.toResource(pages, jobOrderResourceAssembler);
		return ResponseEntity.ok(resources);
	}

	@InitBinder
	public void initBinder(final WebDataBinder binder){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	public JPAQuery<JobOrder> search(Long workRequest, String hierarchyScope, Date startTime, Date endTime, String status, String customer, String patient, String workType, String processingType) {
		QJobOrder qJobOrder = QJobOrder.jobOrder;
		JPAQuery<JobOrder> query = new JPAQuery<JobOrder>(em);
		query.from(qJobOrder);
		if (hierarchyScope != null) {
			query.where(qJobOrder.hierarchyScope.eq(hierarchyScope));
		}
		if (workRequest != null) {
			query.where(qJobOrder.workRequest.id.eq(workRequest));
		}
		if (startTime != null) {
			query.where(qJobOrder.startTime.after(new Timestamp(startTime.getTime())));
		}
		if (endTime != null) {
			query.where(qJobOrder.endTime.before(new Timestamp(endTime.getTime())));
		}
		if (status != null) {
			query.where(qJobOrder.dispatchStatus.eq(DispatchStatus.valueOf(status)));
		}
		if (customer != null || patient != null || workType != null || processingType != null) {
			query.where(this.searchParams(customer, patient, workType, processingType, qJobOrder));
		}
		query.orderBy(qJobOrder.id.desc());
		return query;
	}

	private BooleanExpression searchParams(String customer, String patient, String workType, String processingType, QJobOrder qJobOrder) {
		QJobOrderParameter qJobOrderParameter = QJobOrderParameter.jobOrderParameter;
		BooleanExpression where = qJobOrderParameter.jobOrder.id.eq(qJobOrder.id);
		long cantidad = 1;
		if (patient != null) {
			BooleanExpression whereGroup = qJobOrderParameter.code.eq("patientCustomer")
				.and(qJobOrderParameter.value.upper().like("%" + patient.toUpperCase() + "%"));
			if (customer != null) {
				whereGroup = whereGroup.or(this.searchCustomer(customer, qJobOrderParameter));
				cantidad++;
			}
			where = where.andAnyOf(whereGroup);
		} else if (customer != null) {
			where = where.and(this.searchCustomer(customer, qJobOrderParameter));
		}
		if (workType != null) {
			where = where.and(this.searchWorkType(workType, qJobOrderParameter));
		}
		if (processingType != null) {
			where = where.and(this.searchProcessingType(processingType, qJobOrderParameter));
		}
		JPQLQuery<Long> subQuery = JPAExpressions.selectFrom(qJobOrderParameter)
		.where(where).select(qJobOrderParameter.id.count());
		return subQuery.eq(cantidad);
	}

	private BooleanExpression searchCustomer(String customer, QJobOrderParameter qJobOrderParameter) {
		QPerson qPerson = QPerson.person;
		JPQLQuery<String> subQuery = JPAExpressions.selectFrom(qPerson)
				.where(qPerson.description.upper().like("%"+customer.toUpperCase()+"%"))
				.select(qPerson.id.stringValue());
		return qJobOrderParameter.code.eq("idCustomer").and(qJobOrderParameter.value.in(subQuery));
	}

	private BooleanExpression searchWorkType(String workType, QJobOrderParameter qJobOrderParameter) {
		/*QPerson qPerson = QPerson.person;
		JPQLQuery<String> subQuery = JPAExpressions.selectFrom(qPerson)
				.where(qPerson.description.upper().like("%"+workType.toUpperCase()+"%"))
				.select(qPerson.id.stringValue());*/
		/*
		qJobOrderParameter.code.eq("patientCustomer")
		.and(qJobOrderParameter.value.upper().like("%" + patient.toUpperCase() + "%")); */
		
		return qJobOrderParameter.code.eq("workType").and(qJobOrderParameter.value.eq(workType));
	}

	private BooleanExpression searchProcessingType(String processingType, QJobOrderParameter qJobOrderParameter) {
		/*QPerson qPerson = QPerson.person;
		JPQLQuery<String> subQuery = JPAExpressions.selectFrom(qPerson)
				.where(qPerson.description.upper().like("%"+processingType.toUpperCase()+"%"))
				.select(qPerson.id.stringValue());*/
		return qJobOrderParameter.code.eq("processingType").and(qJobOrderParameter.value.eq(processingType));
	}
}
