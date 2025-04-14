package com.mes.modules.milanes.services;

import com.mes.dom.workDirective.WorkDirective;

public interface MilanesDirectiveService {
	public Long clone(Long workDirectiveId);
	public WorkDirective reentry(WorkDirective wd);
}
