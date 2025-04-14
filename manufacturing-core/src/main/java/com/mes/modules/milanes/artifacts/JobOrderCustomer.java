package com.mes.modules.milanes.artifacts;

import java.io.Serializable;
import java.util.List;

import com.mes.dom.workSchedule.JobOrder;
import com.mes.dom.workSchedule.WorkRequest;

import lombok.Data;

@Data
public class JobOrderCustomer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7597319499895139259L;
	Long JobOrderId;
	JobOrder jobOrder;
	WorkRequest workRequest;
	String workMaster;
	//List<Task> tasks;
	List<TaskCustomer> tasks;
}
