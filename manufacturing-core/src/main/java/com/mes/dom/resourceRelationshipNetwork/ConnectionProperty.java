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
@EqualsAndHashCode(exclude={"resourceNetworkConnection", "values"})
@Table(name="resource_connection_property")
public class ConnectionProperty implements Serializable{
	private static final long serialVersionUID = 5163855263285835289L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=false,length=70)
	private String code;
	@Column(columnDefinition = "mediumtext")
	private String description;
	@ManyToOne
	@JoinColumn(name="resourceNetworkConnection", referencedColumnName="id")	
	@JsonBackReference("resourceNetworkConnection")
	private ResourceNetworkConnection resourceNetworkConnection;
	/*To associate properties*/
	@OneToMany(mappedBy="connectionProperty", cascade = CascadeType.ALL,orphanRemoval=true)
	@JsonManagedReference("connectionProperty") 
	private Collection<ConnectionPropertyValue> values = new HashSet<ConnectionPropertyValue>();

	public ConnectionProperty() { }

	public ConnectionProperty(Long id) {
		this.id = id;
	}

	public ConnectionPropertyValue getValue(String code) {
		for (ConnectionPropertyValue connectionPropertyValue: this.values) {
			if (connectionPropertyValue.getCode().equals(code)) {
				return connectionPropertyValue;
			}
		}
		return null;
	}

	public Collection<ConnectionPropertyValue> getValues(String code) {
		Collection<ConnectionPropertyValue> values = Collections.emptyList();
		for (ConnectionPropertyValue connectionPropertyValue: this.values) {
			if (connectionPropertyValue.getCode().equals(code)) {
				values.add(connectionPropertyValue);
			}
		}
		return values;
	}
}
