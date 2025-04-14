package com.mes.modules.enumerationSet.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.enumerations.custom.EnumerationSet;

@RepositoryRestResource(collectionResourceRel= "enumerationSets", path ="enumerationSets")
@RestResource(exported = true)
@Repository
public interface EnumerationSetRepository extends JpaRepository<EnumerationSet, Long> { }
