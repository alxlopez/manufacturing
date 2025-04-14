package com.mes.dom.personnel;

import java.io.Serializable;
import java.sql.Timestamp;

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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mes.dom.enumerations.application.UnitOfMeasure;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"qualificationTestSpecification"})
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
@Table(name="personnel_qualification_test_result")
public class QualificationTestResult implements Serializable{
	private static final long serialVersionUID = 8984458059838253793L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(columnDefinition = "mediumtext")
	private String description;
	public Timestamp date;
	private String result;
	@Enumerated (EnumType.STRING)
	private UnitOfMeasure unitOfMeasure;
	private Timestamp expiration;
	@ManyToOne
	@JoinColumn(name="qualificationTestSpecification", referencedColumnName="id")	
	@JsonBackReference("qualificationTestSpecification")	
	private QualificationTestSpecification qualificationTestSpecification;
	@Transient
	private Long qualificationTestSpecificationId;

	public QualificationTestResult() { }

	public QualificationTestResult(Long id) {
		this.id=id;
	}
}
