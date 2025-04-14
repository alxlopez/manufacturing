package com.mes.modules.manufacturingEngine.event.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.event.EventDefinitionNotificationRecipient;
import com.mes.dom.event.QEventDefinitionNotificationRecipient;

@RepositoryRestResource(collectionResourceRel = "EventDefinitionNotificationRecipients", path = "EventDefinitionNotificationRecipients")
@Repository
public interface EventDefinitionNotificationRecipientRepository extends JpaRepository<EventDefinitionNotificationRecipient, Long> ,
QueryDslPredicateExecutor<EventDefinitionNotificationRecipient>,
QuerydslBinderCustomizer<QEventDefinitionNotificationRecipient> {
	public EventDefinitionNotificationRecipient findById(Long id);	

	default void customize(QuerydslBindings bindings, QEventDefinitionNotificationRecipient eventDefinitionNotificationRecipient) {
		bindings.bind(eventDefinitionNotificationRecipient.name).first((path, value) -> path.contains(value));
		bindings.bind(eventDefinitionNotificationRecipient.email).first((path, value) -> path.contains(value));
	}
}
