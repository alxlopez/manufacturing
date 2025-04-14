package com.mes.dom.resourceRelationshipNetwork;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mes.dom.enumerations.application.UnitOfMeasure;
import com.mes.dom.enumerations.application.ValueType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(exclude={"fromResourceReferenceProperty"})
@Table(name="resource_from_reference_property_value")
public class FromResourceReferencePropertyValue  implements Serializable{
	private static final long serialVersionUID = -4395023146244283361L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=false,length=70)
	private String code;
	@Enumerated (EnumType.STRING)	
	private ValueType valueType;
	@Enumerated (EnumType.STRING)	
	private UnitOfMeasure unitOfMeasure;
	private String valueString;
	@ManyToOne
	@JoinColumn(name="fromResourceReferenceProperty", referencedColumnName="id")
	@JsonBackReference("fromResourceReferenceProperty")	
	private FromResourceReferenceProperty fromResourceReferenceProperty; 

	public FromResourceReferencePropertyValue() { }
}
