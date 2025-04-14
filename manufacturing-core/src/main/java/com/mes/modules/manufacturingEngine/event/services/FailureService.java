package com.mes.modules.manufacturingEngine.event.services;

import com.mes.dom.event.Failure;

public interface FailureService {
	public String getFailureType(Long failureId);
	public Failure getFailure(Long failureId);
}
