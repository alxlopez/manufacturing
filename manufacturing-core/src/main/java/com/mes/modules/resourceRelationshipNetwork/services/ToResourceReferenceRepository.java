package com.mes.modules.resourceRelationshipNetwork.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;


import com.mes.dom.resourceRelationshipNetwork.QToResourceReference;
import com.mes.dom.resourceRelationshipNetwork.ToResourceReference;

@RepositoryRestResource(collectionResourceRel = "toResourceReferences", path = "toResourceReferences")
@RestResource(exported = false)
@Repository
public interface ToResourceReferenceRepository extends JpaRepository<ToResourceReference, Long>,
QueryDslPredicateExecutor<ToResourceReference>,
QuerydslBinderCustomizer<QToResourceReference>{
	default void customize(QuerydslBindings bindings, QToResourceReference toResourceReference) { }
}
