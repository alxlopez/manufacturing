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
@Table(name="resource_from_reference")
public class FromResourceReference  implements Serializable{
	private static final long serialVersionUID = -370671558964307204L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=false,length=70)
	private String resourceCode;
	@Enumerated (EnumType.STRING)
	private ResourceType resourceType;
	/*To associate properties*/
	@OneToMany(mappedBy="fromResourceReference", cascade = CascadeType.ALL,orphanRemoval=true)
	@JsonManagedReference("fromResourceReference") 
	private Collection<FromResourceReferenceProperty> resourceProperties = new HashSet<FromResourceReferenceProperty>() ;

	public FromResourceReference() { }
	
	public FromResourceReference(Long id) {
		this.id=id;
	}

	public FromResourceReferenceProperty getResourceProperty(String resourceCode) {
		for (FromResourceReferenceProperty fromResourceReferenceProperty: this.resourceProperties) {
			if (fromResourceReferenceProperty.getCode().equals(resourceCode)) {
				return fromResourceReferenceProperty;
			}
		}
		return null;
	}

	public Collection<FromResourceReferenceProperty> getResourceProperties(String resourceCode) {
		Collection<FromResourceReferenceProperty> resourceProperties = Collections.emptyList();
		for (FromResourceReferenceProperty fromResourceReferenceProperty: this.resourceProperties) {
			if (fromResourceReferenceProperty.getCode().equals(resourceCode)) {
				resourceProperties.add(fromResourceReferenceProperty);
			}
		}
		return resourceProperties;
	}
}
