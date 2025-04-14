package com.mes.modules.resourceRelationshipNetwork.services;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.resourceRelationshipNetwork.QResourceNetworkConnection;
import com.mes.dom.resourceRelationshipNetwork.ResourceNetworkConnection;

@RepositoryRestResource(collectionResourceRel = "resourceNetworkConnections", path = "resourceNetworkConnections")
@RestResource(exported = false)
@Repository 
public interface ResourceNetworkConnectionRepository extends JpaRepository<ResourceNetworkConnection, Long>,
QueryDslPredicateExecutor<ResourceNetworkConnection>,
QuerydslBinderCustomizer<QResourceNetworkConnection>{
	public ResourceNetworkConnection findById(Long id);
	@Query("Select resNet from ResourceNetworkConnection resNet "
		      +"INNER JOIN resNet.fromResourceReference fr "
			  +"INNER JOIN resNet.toResourceReference to "
		      +"Where resNet in ?1 and fr.resourceCode=?2  ")
	public List<ResourceNetworkConnection> findByNetworkConnectionsAndFromResourceReference(Collection<ResourceNetworkConnection> resourceNetworkConnection,String fromResourceCode);
	@Query("Select resNet from ResourceNetworkConnection resNet "
		      +"INNER JOIN resNet.fromResourceReference fr "
			  +"INNER JOIN resNet.toResourceReference to "
		      +"Where resNet in ?1 and fr.resourceCode=?2 and to.resourceCode=?3 ")
	public List<ResourceNetworkConnection> findByNetworkConnectionsAndFromToResourceReference(Collection<ResourceNetworkConnection> resourceNetworkConnection,String fromResourceCode,String toResourceCode);
	
	default void customize(QuerydslBindings bindings, QResourceNetworkConnection resourceNetworkConnection) {
		bindings.bind(resourceNetworkConnection.description).first((path, value) -> path.contains(value));		
	}
}
