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
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.EqualsAndHashCode; 

@Entity
@Data
@EqualsAndHashCode(exclude={"resourceRelationshipNetwork", "connectionProperties"})
public class ResourceNetworkConnection  implements Serializable{
	private static final long serialVersionUID = -4529613969704032994L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=true,length=70)
	private String code;
	@Column(columnDefinition = "mediumtext")
	private String description;
	@ManyToOne
	@JoinColumn(name="resourceRelationshipNetwork", referencedColumnName="id")
	@JsonBackReference("resourceRelationshipNetwork")	
	private ResourceRelationshipNetwork resourceRelationshipNetwork; 	
	/*To associate properties*/
	@OneToMany(mappedBy = "resourceNetworkConnection", cascade = CascadeType.ALL, orphanRemoval=true)
	@JsonManagedReference("resourceNetworkConnection")	
	private Collection<ConnectionProperty> connectionProperties = new HashSet<ConnectionProperty>();
	/*To associate properties*/
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fromResourceReference", referencedColumnName="id")	
	private FromResourceReference fromResourceReference;
	/*To associate properties*/
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="toResourceReference", referencedColumnName="id")	
	private ToResourceReference toResourceReference;

	public ResourceNetworkConnection() { }

	public ResourceNetworkConnection(Long id) {
		this.id = id;
	}

	public ConnectionProperty getConnectionProperty(String code) {
		for (ConnectionProperty connectionProperty: this.connectionProperties) {
			if (connectionProperty.getCode().equals(code)) {
				return connectionProperty;
			}
		}
		return null;
	}

	public Collection<ConnectionProperty> getConnectionProperties(String code) {
		Collection<ConnectionProperty> connectionProperties = Collections.emptyList();
		for (ConnectionProperty connectionProperty: this.connectionProperties) {
			if (connectionProperty.getCode().equals(code)) {
				connectionProperties.add(connectionProperty);
			}
		}
		return connectionProperties;
	}
}
