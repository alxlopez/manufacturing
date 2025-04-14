package com.mes.modules.resourceRelationshipNetwork.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.resourceRelationshipNetwork.*; 

@RepositoryRestResource(collectionResourceRel = "resourceRelationshipNetworks", path = "resourceRelationshipNetworks")
@RestResource(exported = false)
@Repository 
public interface ResourceRelationshipNetworkRepository extends JpaRepository<ResourceRelationshipNetwork, Long>,
QueryDslPredicateExecutor<ResourceRelationshipNetwork>,
QuerydslBinderCustomizer<QResourceRelationshipNetwork>{
	public ResourceRelationshipNetwork findById(Long id);

	default void customize(QuerydslBindings bindings, QResourceRelationshipNetwork resourceRelationshipNetwork) {
		bindings.bind(resourceRelationshipNetwork.description).first((path, value) -> path.contains(value));
	}
}
