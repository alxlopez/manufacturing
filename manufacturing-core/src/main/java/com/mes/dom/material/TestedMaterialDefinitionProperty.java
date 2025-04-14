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
@Table(name="material_tested_definition_property")
public class TestedMaterialDefinitionProperty extends TestedProperty implements Serializable{
	private static final long serialVersionUID = 6598067697500445749L;
	@OneToOne
	@JoinColumn(name="materialDefinition", referencedColumnName="id")	
	private MaterialDefinition materialDefinition;	
	@OneToOne
	@JoinColumn(name="materialDefinitionProperty", referencedColumnName="id")	
	private MaterialDefinitionProperty materialDefinitionProperty;
	@ManyToOne
	@JsonBackReference("testedMaterialDefinitionProperties")	
	@JoinColumn(name="materialTestSpecification", referencedColumnName="id")
	private MaterialTestSpecification materialTestSpecification;

	public TestedMaterialDefinitionProperty() { }
}
