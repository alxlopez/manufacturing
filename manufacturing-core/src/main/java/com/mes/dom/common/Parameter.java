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
public abstract class Parameter {
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	private Long id;
	@Column(nullable=true,length=70)	
	private String code;
	@Column(columnDefinition = "mediumtext")	
	private String description;
	@Enumerated (EnumType.STRING)
	private ValueType valueType;
	private String enumeration;
	@Column(columnDefinition = "mediumtext")
	private String value;
	private String unitOfMeasure;
}
