package com.mes.modules.equipment.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.dom.equipment.Equipment;

@Service("equipmentService")
public class EquipmentServiceImpl implements EquipmentService {
	@Autowired
	private EquipmentRepository equipmentRepository;

	public Equipment relationShip(String codeBaseEquipment, String codeSearchedEquipment){
		Equipment baseEquipment = equipmentRepository.findByCode(codeBaseEquipment);
		Equipment searchedEquipment = equipmentRepository.findByCode(codeSearchedEquipment);
		Equipment equipmentResult;
		if(baseEquipment != null && searchedEquipment != null){
			if(baseEquipment.getCode() == searchedEquipment.getCode()){
				return null;
			}
			equipmentResult = this.validateParent(baseEquipment, searchedEquipment);
			if(equipmentResult != null) {
				return null;
			}
			equipmentResult = this.validateChildren(baseEquipment, searchedEquipment);
			if(equipmentResult != null){
				return null;
			}
		}
		return searchedEquipment;
	}

	public Equipment validateParent(Equipment baseEquipment, Equipment searchedEquipment) {	
		Equipment parent = baseEquipment.getParent();
		if(parent != null) {
			if (parent.getCode() == searchedEquipment.getCode()) {
				return parent;
			}
			return this.validateParent(parent, searchedEquipment);
		}
		return null;
	}

	public Equipment validateChildren(Equipment baseEquipment, Equipment searchedEquipment) {
		ArrayList<Equipment> baseEquipmentChildren = equipmentRepository.findByParentId(baseEquipment.getId());
		if(!baseEquipmentChildren.isEmpty()) {
			Equipment equipmentResult;
			for(Equipment equipment: baseEquipmentChildren) {
				if(equipment.getCode() == searchedEquipment.getCode()) {
					return equipment;
				}
				equipmentResult = this.validateChildren(equipment, searchedEquipment);
				if(equipmentResult != null) return equipmentResult;
			}
		}
		return null;
	}
}
