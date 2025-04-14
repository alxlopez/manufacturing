package com.mes.modules.equipment.services;

import com.mes.dom.equipment.Equipment;

public interface EquipmentService {
	public Equipment relationShip(String codeBaseEquipment, String codeSearchedEquipment);
	public Equipment validateParent(Equipment baseEquipment, Equipment searchedEquipment);
	public Equipment validateChildren(Equipment baseEquipment, Equipment searchedEquipment);
}
