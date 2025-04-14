package com.mes.modules.workSchedule.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.workSchedule.JobOrder;
import com.mes.dom.workSchedule.QJobOrder;

@RepositoryRestResource(collectionResourceRel = "jobOrders", path = "jobOrders")
@Repository
public interface JobOrderRepository extends JpaRepository<JobOrder, Long> ,
QueryDslPredicateExecutor<JobOrder> ,
QuerydslBinderCustomizer<QJobOrder> {
	public JobOrder findById(Long id);

	default void customize(QuerydslBindings bindings, QJobOrder jobOrder) {
		bindings.bind(jobOrder.startTime).first((path, value) -> path.after(value));
		bindings.bind(jobOrder.endTime).first((path, value) -> path.before(value));
		bindings.bind(jobOrder.description).first((path, value) -> path.contains(value));
	}
}
