package com.mes.dom.personnel;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
@EqualsAndHashCode(exclude={"properties", "personnel"})
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
public class PersonnelClass implements Serializable{
	private static final long serialVersionUID = 5893641639009466618L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=false,length=70)
	private String code;
	@Column(columnDefinition = "mediumtext")
	private String description;
	@OneToOne
	@JoinColumn(name="location", referencedColumnName="id")
	private Equipment location; 
	private String hierarchyScope;
	/*To associate properties*/
	@OneToMany(mappedBy="personnelClass", cascade = CascadeType.ALL,orphanRemoval=true)
	@JsonManagedReference("personnelClass") 
	private Collection<PersonnelClassProperty> properties = new HashSet<PersonnelClassProperty>();
	//New :: Bi-Directional Relation (Use only personnel created previously)
	@ManyToMany (mappedBy ="personnelClasses")
	private Collection<Person> personnel = new HashSet<Person>();

	public PersonnelClass() { }

	public PersonnelClass(Long id) {
		this.id = id; 
	}

	public PersonnelClassProperty getProperty(String code) {
		for (PersonnelClassProperty personnelClassProperty: this.properties) {
			if (personnelClassProperty.getCode().equals(code)) {
				return personnelClassProperty;
			}
		}
		return null;
	}

	public Collection<PersonnelClassProperty> getProperties(String code) {
		Collection<PersonnelClassProperty> properties = Collections.emptyList();
		for (PersonnelClassProperty personnelClassProperty: this.properties) {
			if (personnelClassProperty.getCode().equals(code)) {
				properties.add(personnelClassProperty);
			}
		}
		return properties;
	}
}
