package com.mes.modules.personnel.services;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;


import com.mes.dom.personnel.*;

@RepositoryRestResource(collectionResourceRel = "personProperties", path = "personProperties")
@RestResource(exported = true)
@Repository 
public interface PersonPropertyRepository  extends JpaRepository<PersonProperty, Long> { }
