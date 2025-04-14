package com.mes.modules.resourceRelationshipNetwork.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.resourceRelationshipNetwork.ConnectionProperty;

import com.mes.dom.resourceRelationshipNetwork.QConnectionProperty;

@RepositoryRestResource(collectionResourceRel = "connectionProperties", path = "connectionProperties")
@RestResource(exported = false)
@Repository 
public interface ConnectionPropertyRepository extends JpaRepository<ConnectionProperty, Long>,
QueryDslPredicateExecutor<ConnectionProperty>,
QuerydslBinderCustomizer<QConnectionProperty>{
	default void customize(QuerydslBindings bindings, QConnectionProperty connectionProperty) {	}
}
