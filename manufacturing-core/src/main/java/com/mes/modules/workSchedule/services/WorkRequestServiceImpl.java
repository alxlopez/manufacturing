package com.mes.modules.workSchedule.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.dom.enumerations.application.DispatchStatus;
import com.mes.dom.workSchedule.JobOrder;
import com.mes.dom.workSchedule.WorkRequest;

@Service("workRequestService")
public class WorkRequestServiceImpl implements WorkRequestService {
	@Autowired
	private WorkRequestRepository repository;

	@Override
	public WorkRequest create(WorkRequest workRequest) {
		Collection<JobOrder> jobOrders = workRequest.getJobOrders();
		for (JobOrder jobOrder : jobOrders) {
			jobOrder.setDispatchStatus(DispatchStatus.CREATED);
		}
		WorkRequest saved = repository.save(workRequest);
		return saved;
	}

	@Override
	public WorkRequest schedule(Long id) {
		return null;
	}

	@Override
	public WorkRequest dispatch(Long id) {
		return null;
	}

	@Override
	public WorkRequest close(Long id) {
		return null;
	}
}
