package com.mes.modules.workSchedule.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.dom.enumerations.application.DispatchStatus;
import com.mes.dom.workSchedule.JobOrder;

@Service("jobOrderService")
public class JobOrderServiceImpl implements JobOrderService {
	@Autowired
	private JobOrderRepository jobOrderRepository;

	@Override
	public void setDispatchStatus(JobOrder jobOrder, DispatchStatus status) {
		jobOrder.setDispatchStatus(status);
		jobOrderRepository.save(jobOrder);
	}
}
