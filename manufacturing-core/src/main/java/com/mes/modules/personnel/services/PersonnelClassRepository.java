package com.mes.modules.personnel.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mes.dom.personnel.*;

@RepositoryRestResource(collectionResourceRel = "personnelClasses", path = "personnelClasses")
@RestResource(exported = false)
@Repository 
public interface PersonnelClassRepository extends JpaRepository<PersonnelClass, Long> {
	public PersonnelClass findById(Long id);
}
