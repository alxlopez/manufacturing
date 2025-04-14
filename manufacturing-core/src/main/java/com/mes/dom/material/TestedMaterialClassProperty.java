package com.mes.dom.material;

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
@EqualsAndHashCode(callSuper=true)
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
@Table(name="material_tested_class_property")
public class TestedMaterialClassProperty extends TestedProperty implements Serializable{
	private static final long serialVersionUID = 4634731939170298663L;
	@OneToOne
	@JoinColumn(name="materialClass", referencedColumnName="id")	
	private MaterialClass materialClass;	
	@OneToOne
	@JoinColumn(name="materialClassProperty", referencedColumnName="id")	
	private MaterialClassProperty materialClassProperty;	
	@ManyToOne
	@JsonBackReference("testedMaterialClassProperties")	
	@JoinColumn(name="materialTestSpecification", referencedColumnName="id")
	private MaterialTestSpecification materialTestSpecification;

	public TestedMaterialClassProperty() { }
}
