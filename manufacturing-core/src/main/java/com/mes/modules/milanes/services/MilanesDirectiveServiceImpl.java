package com.mes.modules.milanes.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.dom.material.MaterialLot;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.dom.workDirective.WorkDirectiveParameter;
import com.mes.modules.material.services.MaterialLotRepository;
import com.mes.modules.workDirective.services.WorkDirectiveRepository;

@Service("milanesDirectiveService")
public class MilanesDirectiveServiceImpl implements MilanesDirectiveService {
	@Autowired
	private WorkDirectiveRepository repository;
	@Autowired
	private MaterialLotRepository  materialLotRepository;

	public Long clone(Long workDirectiveId){
		Set<WorkDirective> workDirectivesList = new HashSet<WorkDirective>();
		WorkDirective baseWorkDirective = repository.findById(workDirectiveId);
		WorkDirective newWorDirective = baseWorkDirective.clone();
		WorkDirectiveParameter reentry = newWorDirective.getParameter("reentry");
		String strWorkDirectiveId = workDirectiveId.toString();
		newWorDirective.setParent(baseWorkDirective);
		newWorDirective.getParameter("processingType").setValue("V");
		if (reentry == null) {
			newWorDirective.addParameter("reentry", strWorkDirectiveId);
		}
		else {
			reentry.setValue(strWorkDirectiveId);
			newWorDirective.removeParameter("executeTest");
			newWorDirective.removeParameter("executeTestType");
		}		
		newWorDirective.setWorkDirectives(workDirectivesList);
		repository.save(newWorDirective);
		MaterialLot materialLot = materialLotRepository.findByCode(workDirectiveId.toString());
		materialLot.setCode(newWorDirective.getId().toString());
		materialLotRepository.save(materialLot);
		materialLotRepository.flush();
		return newWorDirective.getId();
	}

	public WorkDirective reentry(WorkDirective wd) {
		WorkDirective savedWorkDirective = repository.findById(wd.getId());
		WorkDirectiveParameter executeTest = savedWorkDirective.getParameter("executeTest");
		WorkDirectiveParameter executeTestType = savedWorkDirective.getParameter("executeTestType");
		if (executeTest == null) {
			savedWorkDirective.addParameter("executeTest", wd.getParameter("executeTest").getValue());
			savedWorkDirective.addParameter("executeTestType", wd.getParameter("executeTestType").getValue());
		} else {
			executeTest.setValue(wd.getParameter("executeTest").getValue());
			executeTestType.setValue(wd.getParameter("executeTestType").getValue());
		}
		repository.save(savedWorkDirective);
		return savedWorkDirective;
	}
}
