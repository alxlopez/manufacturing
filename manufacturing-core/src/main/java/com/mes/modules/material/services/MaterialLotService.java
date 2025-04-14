package com.mes.modules.material.services;

import com.mes.dom.material.MaterialLot;

public interface MaterialLotService {
	public MaterialLot  updateStorageLocation(Long materialLotId, long storageLocationId);
	public MaterialLot  updateCode(Long materialLotId, String code);
}
