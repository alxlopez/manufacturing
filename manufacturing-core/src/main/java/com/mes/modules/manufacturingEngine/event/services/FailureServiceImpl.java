package com.mes.modules.manufacturingEngine.event.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.config.exception.ManufacturingIllegalArgumentException;
import com.mes.dom.event.Failure;

@Service("failureService")
public class FailureServiceImpl implements FailureService {
	@Autowired
	private FailureRepository failureRepository;

	@Override
	public String getFailureType(Long failureId) {
		Failure failure = failureRepository.findById(failureId);
		return failure.getType().toString();
	}

	@Override
	public Failure getFailure(Long failureId) {
		Failure failure = failureRepository.findById(failureId);
		if (failure==null){
			throw new ManufacturingIllegalArgumentException(" failureId "+failureId+" unknown for the system");
		}
		return failure;
	}
}
