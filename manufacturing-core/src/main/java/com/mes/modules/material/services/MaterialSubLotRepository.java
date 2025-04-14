package com.mes.modules.material.services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import com.mes.dom.material.MaterialSubLot;
import com.mes.dom.material.QMaterialSubLot;

@RepositoryRestResource(collectionResourceRel = "materialSubLots", path = "materialSubLots")
@RestResource(exported = true)
@Repository 
public interface MaterialSubLotRepository extends
JpaRepository<MaterialSubLot, Long>,
QueryDslPredicateExecutor<MaterialSubLot>,
QuerydslBinderCustomizer<QMaterialSubLot>{	
	default void customize(QuerydslBindings bindings, QMaterialSubLot materialSubLot) {
		bindings.bind(materialSubLot.description).first((path, value) -> path.contains(value));
	}
}
