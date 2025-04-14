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
public class PersonnelClassProperty extends Property implements Serializable {
	private static final long serialVersionUID = -1005240576832311737L;
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=false,updatable=true) 
	@JsonBackReference("personnelClassProperty-parent-child")
	private PersonnelClassProperty parent;
	@OneToMany(cascade={CascadeType.ALL},orphanRemoval=true) 
	@JoinColumn(name = "parent_id") 
	@JsonManagedReference("personnelClassProperty-parent-child")
	private Set<PersonnelClassProperty> properties = new HashSet<PersonnelClassProperty>();
	/*----------------------------------------------------------------------------*/	
	@ManyToOne
	@JoinColumn(name="personnelClass", referencedColumnName="id")	
	@JsonBackReference("personnelClass")	
	private PersonnelClass personnelClass;

	public PersonnelClassProperty() { }

	public PersonnelClassProperty(Long id) {
		this.setId(id);
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
