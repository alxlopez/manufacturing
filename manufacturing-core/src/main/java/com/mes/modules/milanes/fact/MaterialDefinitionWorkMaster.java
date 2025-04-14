package com.mes.modules.milanes.fact;

import lombok.Data;

@Data
public class MaterialDefinitionWorkMaster {
	private String workType;
	private String productType;
	private String productSubType;
	private String materialType;
	private String materialSubType;
	private String receta;

	public MaterialDefinitionWorkMaster() { }
}
