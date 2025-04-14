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
@EqualsAndHashCode(exclude={"qualificationTestSpecification"}, callSuper=true)
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
@Table(name="personnel_tested_person_property")
public class TestedPersonProperty extends TestedProperty implements Serializable{
	private static final long serialVersionUID = -6727174826968306026L;
	@OneToOne
	@JoinColumn(name="person", referencedColumnName="id")
	private Person person; 	
	@OneToOne
	@JoinColumn(name="personProperty", referencedColumnName="id")
	private PersonProperty personProperty; 
	@ManyToOne
	@JsonBackReference("testedPersonProperties")	
	@JoinColumn(name="qualificationTestSpecification", referencedColumnName="id")
	private QualificationTestSpecification qualificationTestSpecification;	

	public TestedPersonProperty() { }

	public TestedPersonProperty(Long id) {
		this.setId(id);
	}
}
