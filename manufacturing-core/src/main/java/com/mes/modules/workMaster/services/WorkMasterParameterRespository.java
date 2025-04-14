package com.mes.modules.workMaster.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.workDirective.WorkMasterParameter;

@RepositoryRestResource(collectionResourceRel = "workMasterParameters", path = "workMasterParameters")
@RestResource(exported = true)
@Repository 
public interface WorkMasterParameterRespository extends JpaRepository<WorkMasterParameter, Long>{ }
