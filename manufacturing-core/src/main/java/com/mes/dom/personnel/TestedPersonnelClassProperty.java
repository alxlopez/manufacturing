package com.mes.dom.personnel;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mes.dom.common.TestedProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(callSuper=false)
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
@Table(name="personnel_tested_class_property")
public class TestedPersonnelClassProperty extends TestedProperty implements Serializable{
	private static final long serialVersionUID = -5661314024662627011L;
	@OneToOne
	@JoinColumn(name="personnelClass", referencedColumnName="id")	
	private PersonnelClass personnelClass;	
	@OneToOne
	@JoinColumn(name="personnelClassProperty", referencedColumnName="id")	
	private PersonnelClassProperty personnelClassProperty;	
	@ManyToOne
	@JsonBackReference("testedPersonnelClassProperties")
	@JoinColumn(name="qualificationTestSpecification", referencedColumnName="id")
	private QualificationTestSpecification qualificationTestSpecification;

	public TestedPersonnelClassProperty() { }

	public TestedPersonnelClassProperty(Long id) {
		this.setId(id);
	}
}
