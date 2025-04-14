package com.mes.dom.material;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mes.dom.enumerations.application.AssemblyRelationShip;
import com.mes.dom.enumerations.application.AssemblyType;
import com.mes.dom.enumerations.application.MaterialLotStatus;
import com.mes.dom.equipment.Equipment;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"subLots", "properties", "lots"})
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
public class MaterialLot implements Serializable{
	private static final long serialVersionUID = -7627804094400214894L;
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable=true,length=70)	
	private String code;
	@Column(columnDefinition = "mediumtext")
	private String description;
	@OneToOne
	@JoinColumn(name="location",referencedColumnName="id")
	private Equipment location;
	private String hierarchyScope;
	@OneToOne
	@JoinColumn(name="materialDefinition",referencedColumnName="id")
	private MaterialDefinition materialDefinition;	
	@Enumerated(EnumType.STRING)
	private MaterialLotStatus status;		
	@OneToOne
	@JoinColumn(name="storageLocation",referencedColumnName="id")
	private Equipment storageLocation;
	private Float quantity;
	private String unitOfMeasure;
	@Enumerated(EnumType.STRING)
	private AssemblyType assemblyType;
	@Enumerated(EnumType.STRING)
	private AssemblyRelationShip assemblyRelationship;
	/*----------------------------------------------------------------------------*/
    // material lot - material lot properties composition relationShip
	/*----------------------------------------------------------------------------*/
	@OneToMany(mappedBy = "materialLot", cascade = CascadeType.ALL, orphanRemoval=true)
	@JsonManagedReference("materialLotProperties")
	private Collection<MaterialLotProperty> properties = new HashSet<MaterialLotProperty>();	
	/*----------------------------------------------------------------------------*/
    // material lot - material lot aggregation relationShip
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=true,updatable=true)
	@JsonBackReference("materialLot-parent-child")
	private MaterialLot parent;
	@OneToMany(cascade={CascadeType.PERSIST} ) 
	@JoinColumn(name = "parent_id", insertable = true, updatable = false) 
	@JsonManagedReference("materialLot-parent-child")
	private Set<MaterialLot> lots = new HashSet<MaterialLot>();
	/*----------------------------------------------------------------------------*/
    // material lot - material subLot aggregation relationShip
	/*----------------------------------------------------------------------------*/
	/*To associate subLot*/
	@OneToMany(mappedBy = "materialLot", cascade = CascadeType.PERSIST)
	@JsonManagedReference("materialLotSubLot")
	private Collection<MaterialSubLot>  subLots = new HashSet<MaterialSubLot>();	

	public MaterialLot() { }

	public MaterialLot(Long id) {
		super();
		this.id =id; 
	}

	public MaterialLotProperty getProperty(String code) {
		for (MaterialLotProperty materialLotProperty: this.properties) {
			if (materialLotProperty.getCode().equals(code)) {
				return materialLotProperty;
			}
		}
		return null;
	}

	public Collection<MaterialLotProperty> getProperties(String code) {
		Collection<MaterialLotProperty> properties = Collections.emptyList();
		for (MaterialLotProperty materialLotProperty: this.properties) {
			if (materialLotProperty.getCode().equals(code)) {
				properties.add(materialLotProperty);
			}
		}
		return properties;
	}
}
