package com.mes.dom.resourceRelationshipNetwork;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.EqualsAndHashCode; 

@Entity
@Data
@EqualsAndHashCode(exclude={"fromResourceReference", "values"})
@Table(name="resource_from_reference_property")
public class FromResourceReferenceProperty  implements Serializable{
	private static final long serialVersionUID = -2918562298165695105L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=false,length=70)
	private String code;
	@Column(columnDefinition = "mediumtext")
	private String description;
	@ManyToOne
	@JoinColumn(name="fromResourceReference", referencedColumnName="id")
	@JsonBackReference("fromResourceReference")	
	private FromResourceReference fromResourceReference;
	/*To associate properties*/
	@OneToMany(mappedBy="fromResourceReferenceProperty", cascade = CascadeType.ALL,orphanRemoval=true)
	@JsonManagedReference("fromResourceReferenceProperty") 
	private Collection<FromResourceReferencePropertyValue> values = new HashSet<FromResourceReferencePropertyValue>();

	public FromResourceReferenceProperty() { }
	
	public FromResourceReferenceProperty(Long id) {
		this.id = id;
	}

	public FromResourceReferencePropertyValue getValue(String code) {
		for (FromResourceReferencePropertyValue fromResourceReferencePropertyValue: this.values) {
			if (fromResourceReferencePropertyValue.getCode().equals(code)) {
				return fromResourceReferencePropertyValue;
			}
		}
		return null;
	}

	public Collection<FromResourceReferencePropertyValue> getValues(String code) {
		Collection<FromResourceReferencePropertyValue> values = Collections.emptyList();
		for (FromResourceReferencePropertyValue fromResourceReferencePropertyValue: this.values) {
			if (fromResourceReferencePropertyValue.getCode().equals(code)) {
				values.add(fromResourceReferencePropertyValue);
			}
		}
		return values;
	}
}
