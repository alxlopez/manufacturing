package com.mes.modules.workDirective.services;

import com.mes.dom.workDirective.WorkDirectiveParameter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(collectionResourceRel = "workDirectiveParameters", path = "workDirectiveParameters")
@RestResource(exported = true)
@Repository 
public interface WorkDirectiveParameterRespository extends JpaRepository<WorkDirectiveParameter, Long> { }
