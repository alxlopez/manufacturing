package com.mes.dom.personnel;

import java.io.Serializable; 
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn; 
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mes.dom.equipment.Equipment;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"testedPersonnelClassProperties", "testedPersonProperties", "qualificationTestResults"})
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
@Table(name="personnel_qualification_test_specification")
public class QualificationTestSpecification implements Serializable{
	private static final long serialVersionUID = 3595504411919624197L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=false,length=70)
	private String code;	
	@Column(columnDefinition = "mediumtext")
	private String description;
	private String version;
	@OneToOne
	@JoinColumn(name="location", referencedColumnName="id")
	private Equipment location;
	private String hierarchyScope;
	@OneToMany(mappedBy="qualificationTestSpecification", cascade = CascadeType.ALL)
	@JsonManagedReference("testedPersonnelClassProperties")	
	private Collection<TestedPersonnelClassProperty> testedPersonnelClassProperties; 
	@OneToMany(mappedBy="qualificationTestSpecification", cascade = CascadeType.ALL)
	@JsonManagedReference("testedPersonProperties")
	private Collection<TestedPersonProperty> testedPersonProperties; 
	/*To associate test results*/
	@OneToMany(mappedBy="qualificationTestSpecification", cascade = CascadeType.ALL,orphanRemoval=true)
	@JsonManagedReference("qualificationTestSpecification") 
	private Collection<QualificationTestResult> qualificationTestResults;

	public QualificationTestSpecification() { }
	
	public QualificationTestSpecification(Long id) {
		this.id=id;
	}
}
