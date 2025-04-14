package com.mes.modules.workSchedule.services;

import com.mes.dom.enumerations.application.DispatchStatus;
import com.mes.dom.workSchedule.JobOrder;

public interface JobOrderService {	
	public void setDispatchStatus(JobOrder JobOrder,DispatchStatus status);
}
