package com.mes.modules.milanes.fact;

import java.util.Date;

import lombok.Data;

@Data
public class StimatedScheduledTime {
	private Long workRequestId;
	private Double scheduledProcessingDays;
	private Date scheduledStartDate;
	private Date scheduledEndDate;
	private String dispatchCity;

	public StimatedScheduledTime() { }
}
