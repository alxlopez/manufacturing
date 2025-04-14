package com.mes.modules.personnel.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import com.mes.dom.personnel.Person;
import com.mes.dom.personnel.QPerson;

@RepositoryRestResource(collectionResourceRel = "personnel", path = "personnel")
@RestResource(exported = false)
@Repository 
public interface PersonRepository  extends JpaRepository<Person, Long>,
QueryDslPredicateExecutor<Person>,
QuerydslBinderCustomizer<QPerson> {
	public Person findById(Long id);
	public Person findByCode(String code);
	
	default void customize(QuerydslBindings bindings, QPerson person) {
		bindings.bind(person.description).first((path, value) -> path.contains(value));
	}
}
