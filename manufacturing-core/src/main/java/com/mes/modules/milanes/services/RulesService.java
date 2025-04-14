package com.mes.modules.milanes.services;

import java.util.Date;
import com.mes.modules.milanes.fact.MaterialDefinitionWorkMaster;
import com.mes.modules.milanes.fact.StimatedScheduledTime;
import com.mes.modules.milanes.fact.ToProcess;

public interface RulesService {
	public void setProcessingDays(ToProcess toProcess);
	public ToProcess getScheduledFinishedDate(String materialCode, String workType, boolean hasflash);
	public MaterialDefinitionWorkMaster getMaterialDefinitionWorkMaster(String materialCode);
	public StimatedScheduledTime getScheduledTotalFinishedDate(Long jobOrderId);
	public Date setTimeDateParameters(Date date, int hour, int minute, int second);
	public Date getInitialDate();
	public Date getFinishDate(Date initDate, int daysNumber);
	public String getDispatchCity(Long jobOrderId);
}
