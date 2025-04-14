package com.mes.dom.enumerations.custom;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(exclude={"enumerationSet"})
public class Enumeration implements Serializable {
	private static final long serialVersionUID = -3553632040865666922L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=true,length=70)
	private String code;
	@Column(nullable=false,length=70)
	private String value;
	@ManyToOne
	@JoinColumn(name="enumerationSet", referencedColumnName="id")
	@JsonBackReference("enumerationSet")	
	private EnumerationSet enumerationSet; 	

	public Enumeration() { }

	public Enumeration(Long id) {
		this.id = id; 
	}
}
