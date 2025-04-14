package com.mes.dom.resourceRelationshipNetwork;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mes.dom.enumerations.application.ResourceType;

import lombok.Data;
import lombok.EqualsAndHashCode; 

@Entity
@Data
@EqualsAndHashCode(exclude={"resourceProperties"})
@Table(name="resource_to_reference")
public class ToResourceReference  implements Serializable{
	private static final long serialVersionUID = -7987291898010417879L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=false,length=70)
	private String resourceCode;
	@Enumerated (EnumType.STRING)
	private ResourceType resourceType;
	/*To associate properties*/
	@OneToMany(mappedBy="toResourceReference", cascade = CascadeType.ALL,orphanRemoval=true)
	@JsonManagedReference("toResourceReference") 
	private Collection<ToResourceReferenceProperty> resourceProperties = new HashSet<ToResourceReferenceProperty>();

	public ToResourceReference() { }

	public ToResourceReference(Long id){
		this.id=id;
	}

	public ToResourceReferenceProperty getResourceProperty(String resourceCode) {
		for (ToResourceReferenceProperty toResourceReferenceProperty: this.resourceProperties) {
			if (toResourceReferenceProperty.getCode().equals(resourceCode)) {
				return toResourceReferenceProperty;
			}
		}
		return null;
	}

	public Collection<ToResourceReferenceProperty> getResourceProperties(String resourceCode) {
		Collection<ToResourceReferenceProperty> resourceProperties = Collections.emptyList();
		for (ToResourceReferenceProperty toResourceReferenceProperty: this.resourceProperties) {
			if (toResourceReferenceProperty.getCode().equals(resourceCode)) {
				resourceProperties.add(toResourceReferenceProperty);
			}
		}
		return resourceProperties;
	}
}
