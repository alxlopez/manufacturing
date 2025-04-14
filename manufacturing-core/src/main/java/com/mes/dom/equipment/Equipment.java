package com.mes.dom.equipment;

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
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mes.dom.enumerations.application.EquipmentLevel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"equipmentClasses", "properties", "equipments"})
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"equipmentLevel", "code"}))
public class Equipment implements Serializable {
	private static final long serialVersionUID = 1477553091764926844L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=false,length=70)
	private String code;
	@Column(columnDefinition = "mediumtext")
	private String description;
	@Enumerated (EnumType.STRING)
	private EquipmentLevel equipmentLevel;
	@OneToOne
	@JoinColumn(name="location", referencedColumnName="id")
	private Equipment location; 
	private String hierarchyScope;
	//New :: Bi-Directional Relation
    @ManyToMany(mappedBy="equipments")
    private Collection<EquipmentClass> equipmentClasses;	
	@OneToMany(mappedBy="equipment", cascade={CascadeType.ALL})
	@JsonManagedReference("equipment")
	private Collection<EquipmentProperty> properties;
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=true,updatable=true) 
	@JsonBackReference("equipment-parent-child")
	private Equipment parent;
	@OneToMany(cascade={CascadeType.PERSIST} ) 
	@JoinColumn(name = "parent_id", insertable = true, updatable = false)
	@JsonManagedReference("equipment-parent-child")
	private Set<Equipment> equipments = new HashSet<Equipment>();
	/*----------------------------------------------------------------------------*/	
	
	public Equipment() { }

	public Equipment(Long id) {
		super();
		this.id = id; 
	}

	public EquipmentProperty getProperty(String code) {
		for (EquipmentProperty equipmentProperty: this.properties) {
			if (equipmentProperty.getCode().equals(code)) {
				return equipmentProperty;
			}
		}
		return null;
	}

	public Collection<EquipmentProperty> getProperties(String code) {
		Collection<EquipmentProperty> properties = Collections.emptyList();
		for (EquipmentProperty equipmentProperty: this.properties) {
			if (equipmentProperty.getCode().equals(code)) {
				properties.add(equipmentProperty);
			}
		}
		return properties;
	}
}
