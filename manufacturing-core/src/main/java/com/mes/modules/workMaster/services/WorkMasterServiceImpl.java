package com.mes.modules.workMaster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.dom.workDirective.WorkMaster;

@Service("workMasterService")
public class WorkMasterServiceImpl implements WorkMasterService {
	@Autowired
	private WorkMasterRepository repository;

	@Override
	public WorkMaster create(WorkMaster workMaster) {
		workMaster = repository.save(workMaster);
		return workMaster;
	}
}
