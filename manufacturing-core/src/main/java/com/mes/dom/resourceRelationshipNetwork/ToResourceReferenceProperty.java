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
@EqualsAndHashCode(exclude={"toResourceReference", "values"})
@Table(name="resource_to_reference_property")
public class ToResourceReferenceProperty  implements Serializable{
	private static final long serialVersionUID = 8106679618720322313L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=false,length=70)
	private String code;
	@Column(columnDefinition = "mediumtext")
	private String description;
	@ManyToOne
	@JoinColumn(name="toResourceReference", referencedColumnName="id")
	@JsonBackReference("toResourceReference")	
	private ToResourceReference toResourceReference;
	/*To associate properties*/
	@OneToMany(mappedBy="toResourceReferenceProperty", cascade = CascadeType.ALL,orphanRemoval=true)
	@JsonManagedReference("toResourceReferenceProperty") 
	private Collection<ToResourceReferencePropertyValue> values = new HashSet<ToResourceReferencePropertyValue>();

	public ToResourceReferenceProperty() { }

	public ToResourceReferenceProperty(Long id ){
		this.id = id;
	}

	public ToResourceReferencePropertyValue getValue(String code) {
		for (ToResourceReferencePropertyValue toResourceReferencePropertyValue: this.values) {
			if (toResourceReferencePropertyValue.getCode().equals(code)) {
				return toResourceReferencePropertyValue;
			}
		}
		return null;
	}

	public Collection<ToResourceReferencePropertyValue> getValues(String code) {
		Collection<ToResourceReferencePropertyValue> values = Collections.emptyList();
		for (ToResourceReferencePropertyValue toResourceReferencePropertyValue: this.values) {
			if (toResourceReferencePropertyValue.getCode().equals(code)) {
				values.add(toResourceReferencePropertyValue);
			}
		}
		return values;
	}
}
