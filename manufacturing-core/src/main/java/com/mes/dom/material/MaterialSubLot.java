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
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mes.dom.enumerations.application.AssemblyRelationShip;
import com.mes.dom.enumerations.application.AssemblyType;
import com.mes.dom.enumerations.application.MaterialLotStatus;
import com.mes.dom.enumerations.application.UnitOfMeasure;
import com.mes.dom.equipment.Equipment;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name="material_sublot")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"properties", "sublots"})
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
public class MaterialSubLot implements Serializable {
	private static final long serialVersionUID = 1222430010216451439L;
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
	@Enumerated(EnumType.STRING)
	private MaterialLotStatus status;	
	@OneToOne
	@JoinColumn(name="storageLocation",referencedColumnName="id")
	private Equipment storageLocation;
	private Float quantity;
	@Enumerated(EnumType.STRING)
	private UnitOfMeasure unitOfMeasure;
	@Enumerated(EnumType.STRING)
	private AssemblyType assemblyType;
	@Enumerated(EnumType.STRING)
	private AssemblyRelationShip assemblyRelationship;
	@ManyToOne
	@JoinColumn(name="materialLot", referencedColumnName="id")	
	@JsonBackReference("materialLotSubLot")
	private MaterialLot materialLot;
	/*To associate properties*/
	@OneToMany(mappedBy = "materialSublot", cascade = CascadeType.ALL, orphanRemoval=true)
	@JsonManagedReference("materialSublot")
	private Collection<MaterialSubLotProperty> properties;	
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=true,updatable=true) 
	@JsonBackReference("materialSubLot-parent-child")
	private MaterialSubLot parent;
	@OneToMany(cascade={CascadeType.PERSIST} ) 
	@JoinColumn(name = "parent_id", insertable = true, updatable = false) 
	@JsonManagedReference("materialSubLot-parent-child")
	private Set<MaterialSubLot> sublots = new HashSet<MaterialSubLot>();
	/*----------------------------------------------------------------------------*/	

	public MaterialSubLot() { }

	public MaterialSubLot(Long id) {
		this.id = id;
	}	

	public MaterialSubLotProperty getProperty(String code) {
		for (MaterialSubLotProperty materialSubLotProperty: this.properties) {
			if (materialSubLotProperty.getCode().equals(code)) {
				return materialSubLotProperty;
			}
		}
		return null;
	}

	public Collection<MaterialSubLotProperty> getProperties(String code) {
		Collection<MaterialSubLotProperty> properties = Collections.emptyList();
		for (MaterialSubLotProperty materialSubLotProperty: this.properties) {
			if (materialSubLotProperty.getCode().equals(code)) {
				properties.add(materialSubLotProperty);
			}
		}
		return properties;
	}
}
