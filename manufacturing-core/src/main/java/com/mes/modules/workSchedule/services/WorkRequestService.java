package com.mes.modules.workSchedule.services;

import com.mes.dom.workSchedule.WorkRequest;

public interface WorkRequestService {
	public WorkRequest create(WorkRequest workRequest);
	public WorkRequest schedule(Long id);
    public WorkRequest dispatch(Long id);
    public WorkRequest close(Long id);
}
