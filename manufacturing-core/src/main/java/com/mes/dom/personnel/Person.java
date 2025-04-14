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
@EqualsAndHashCode(exclude={"personnelClasses","properties"})
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
public class Person implements Serializable{
	private static final long serialVersionUID = -6874313283356166956L;
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
	@OneToMany(mappedBy="person", cascade = CascadeType.ALL,orphanRemoval=true)
	@JsonManagedReference("person") 
	private Collection<PersonProperty> properties = new HashSet<PersonProperty>();
	//New :: Bi-Directional Relation
    @ManyToMany
    private Collection<PersonnelClass> personnelClasses = new HashSet<PersonnelClass>();

	public Person() { }

	public Person(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public PersonProperty addProperty(String code, String value) {
		PersonProperty property = new PersonProperty(code, value, this);
		this.properties.add(property);
		return property;
	}

	public PersonProperty getProperty(String code) {
		for (PersonProperty property: this.properties) {
			if (property.getCode().equals(code)) {
				return property;
			}
		}
		return null;
	}

	public Collection<PersonProperty> getProperties(String code) {
		Collection<PersonProperty> properties = Collections.emptyList();
		for (PersonProperty property: this.properties) {
			if (property.getCode().equals(code)) {
				properties.add(property);
			}
		}
		return properties;
	}
}
