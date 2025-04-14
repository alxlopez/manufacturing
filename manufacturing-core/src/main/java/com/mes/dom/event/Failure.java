package com.mes.dom.event;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.mes.dom.enumerations.application.FailureType;
import com.mes.dom.enumerations.application.Severity;

import lombok.Data;

@Entity
@Data
public class Failure implements Serializable {
	private static final long serialVersionUID = -3151793893258002302L;
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column(name="id")	
	private Long id;
	@Column(nullable=false,length=70)
	private String code;
	@Enumerated (EnumType.STRING)
	private FailureType type;
	@Enumerated(EnumType.STRING)
	private Severity severity;
	@Column(columnDefinition = "mediumtext")
	private String description;
	private String eventMasterId;
	private String candidateGroup;

	public Failure() { }

	public Failure(Long id) {
		this.id=id;
	}
}
