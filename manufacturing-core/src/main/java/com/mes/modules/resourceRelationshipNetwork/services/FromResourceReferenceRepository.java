package com.mes.modules.resourceRelationshipNetwork.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;


import com.mes.dom.resourceRelationshipNetwork.FromResourceReference;
import com.mes.dom.resourceRelationshipNetwork.QFromResourceReference;

@RepositoryRestResource(collectionResourceRel = "fromResourceReferences", path = "fromResourceReferences")
@RestResource(exported = false)
@Repository 
public interface FromResourceReferenceRepository extends JpaRepository<FromResourceReference, Long>,
QueryDslPredicateExecutor<FromResourceReference>,
QuerydslBinderCustomizer<QFromResourceReference>{
	default void customize(QuerydslBindings bindings, QFromResourceReference fromResourceReference) { }
}
