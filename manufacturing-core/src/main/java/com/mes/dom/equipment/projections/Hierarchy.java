package com.mes.dom.equipment.projections;

import java.util.Collection;
import java.util.Set;
import com.mes.dom.enumerations.application.EquipmentLevel;
import com.mes.dom.equipment.Equipment;
import com.mes.dom.equipment.EquipmentClass;
import com.mes.dom.equipment.EquipmentProperty;

// *******************
// disabled , it is not be used
//*****************
//@Projection(name="hierarchy", types= {Equipment.class})
public interface Hierarchy {
	public Long getId();
	public String getCode();
	public String getDescription();
	public EquipmentLevel getEquipmentLevel();
	public Equipment getLocation();
	public String getHierarchyScope();
	public Collection<EquipmentClass> getEquipmentClasses();
	public Collection<EquipmentProperty> getProperties();
	public Equipment getParent();
	public Set<Equipment> getEquipments();
}
