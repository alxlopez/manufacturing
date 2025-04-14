package com.mes.dom.personnel;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mes.dom.common.Property;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"properties"}, callSuper=true)
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
public class PersonProperty extends Property implements Serializable {
	private static final long serialVersionUID = 1068184282656943443L;
	@ManyToOne
	@JoinColumn(name="person", referencedColumnName="id")
	@JsonBackReference("person")	
	private Person person; 
 	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=false,updatable=true) 
	@JsonBackReference("personProperty-parent-child")
	private PersonProperty parent;
	@OneToMany(cascade={CascadeType.ALL},orphanRemoval=true) 
	@JoinColumn(name = "parent_id") 
	@JsonManagedReference("personProperty-parent-child")
	private Set<PersonProperty> properties = new HashSet<PersonProperty>();
	/*----------------------------------------------------------------------------*/	
	@OneToOne
	@JoinColumn(name="personnelClassProperty", referencedColumnName="id")	
	private PersonnelClassProperty personnelClassProperty;

	public PersonProperty() { }

	public PersonProperty(String code) {
		//this.code = code;
		this.setCode(code);
	}

	public PersonProperty(String code, String value, Person person) {
		this.setCode(code);
		this.setValue(value);
		this.person = person;
	}

	public PersonProperty(String code, String value, PersonProperty personProperty) {
		this.setCode(code);
		this.setValue(value);
		this.parent = personProperty;
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
