package com.mes.dom.common;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

//import com.mes.dom.enumerations.application.UnitOfMeasure;
import com.mes.dom.enumerations.application.ValueType;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class TestedProperty {
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")	
	private Long id;	
	@Enumerated(EnumType.STRING)
	private ValueType valueType;
	private String enumeration;
	private String value;	
	private String minValue;	
	private String maxValue;	
	private Double tolerance;	
	private String defaultValue;	
	private String unitOfMeasure;
}
