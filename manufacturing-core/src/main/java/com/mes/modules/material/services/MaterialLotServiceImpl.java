package com.mes.modules.material.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.dom.equipment.Equipment;
import com.mes.dom.material.MaterialLot;
import com.mes.modules.equipment.services.EquipmentRepository;
@Service ("materialLotService")
public class MaterialLotServiceImpl implements MaterialLotService {
	@Autowired
	private MaterialLotRepository  materialLotRepository;
	@Autowired
	private EquipmentRepository  equipmentRepository;

	@Override
	public MaterialLot updateStorageLocation(Long materialLotId, long storageLocationId) {		
		MaterialLot materialLot = materialLotRepository.findById(materialLotId);
		Equipment storageLocation = equipmentRepository.findById(storageLocationId);
		materialLot.setStorageLocation(storageLocation);		
		return materialLotRepository.save(materialLot);
	}
	
	public MaterialLot  updateCode(Long materialLotId, String code){
		MaterialLot materialLot = materialLotRepository.findById(materialLotId);
		materialLot.setCode(code);
		return materialLotRepository.save(materialLot);
	}
}
