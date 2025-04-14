package com.mes.modules.milanes.fact;

import java.util.Date;


import lombok.Data;

@Data
public class ToProcess {	
	private String productType;	
	private String materialType;
	private String workType;
	private Boolean hasflash;
	private Double normalDays;
	private Double flashDays;
	private Double testDays;
	private Double scheduledProcessingDays;
	private Date scheduledStartDate;
	private Date scheduledEndDate;

	public ToProcess() { }
}
