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
@EqualsAndHashCode(exclude={"connectionProperty"})
@Table(name="resource_connection_property_value")
public class ConnectionPropertyValue implements Serializable{
	private static final long serialVersionUID = 7094648670902110202L;
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
	@JoinColumn(name="connectionProperty", referencedColumnName="id")
	@JsonBackReference("connectionProperty")	
	private ConnectionProperty connectionProperty; 

	public ConnectionPropertyValue() { }
	
	public ConnectionPropertyValue(Long id) {
		this.id = id;
	}
}
