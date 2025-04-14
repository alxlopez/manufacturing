package com.mes.dom.resourceRelationshipNetwork;

import java.io.Serializable;
import java.sql.Date;
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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mes.dom.enumerations.application.RelationshipForm;
import com.mes.dom.enumerations.application.RelationshipType;
import com.mes.dom.equipment.Equipment;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@EqualsAndHashCode(exclude={"resourceNetworkConnections"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"code"}))
public class ResourceRelationshipNetwork  implements Serializable{
	private static final long serialVersionUID = 515925349431597691L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=false,length=70)
	private String code;
	@Column(columnDefinition = "mediumtext")
	private String description;
	@OneToOne
	@JoinColumn(name="hierarchyScope", referencedColumnName="id")
	private Equipment hierarchyScope; 
	private Date publishedDate;
	@Enumerated (EnumType.STRING)
	private RelationshipForm relationshipForm;
	@Enumerated (EnumType.STRING)
	private RelationshipType relationshipType;
	/*To associate properties*/
	@OneToMany(mappedBy="resourceRelationshipNetwork", cascade = CascadeType.ALL,orphanRemoval=true)
	@JsonManagedReference("resourceRelationshipNetwork") 
	private Collection<ResourceNetworkConnection> resourceNetworkConnections = new HashSet<ResourceNetworkConnection>();

	public ResourceRelationshipNetwork() { }
	
	public ResourceRelationshipNetwork(Long id) {
		this.id = id;
	}
	
	public ResourceNetworkConnection getResourceNetworkConnection(String code) {
		for (ResourceNetworkConnection resourceNetworkConnection: this.resourceNetworkConnections) {
			if (resourceNetworkConnection.getCode().equals(code)) {
				return resourceNetworkConnection;
			}
		}
		return null;
	}

	public Collection<ResourceNetworkConnection> getResourceNetworkConnections(String code) {
		Collection<ResourceNetworkConnection> resourceNetworkConnections = Collections.emptyList();
		for (ResourceNetworkConnection resourceNetworkConnection: this.resourceNetworkConnections) {
			if (resourceNetworkConnection.getCode().equals(code)) {
				resourceNetworkConnections.add(resourceNetworkConnection);
			}
		}
		return resourceNetworkConnections;
	}
}
