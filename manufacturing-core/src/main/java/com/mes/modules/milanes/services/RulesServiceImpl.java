package com.mes.modules.milanes.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.dom.enumerations.application.MaterialUse;
import com.mes.dom.enumerations.application.WorkTypes;
import com.mes.dom.equipment.Equipment;
import com.mes.dom.equipment.EquipmentProperty;
import com.mes.dom.material.MaterialDefinition;
import com.mes.dom.material.MaterialDefinitionProperty;
import com.mes.dom.personnel.Person;
import com.mes.dom.personnel.PersonProperty;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.dom.workSchedule.JobOrder;
import com.mes.dom.workSchedule.JobOrderMaterialRequirement;
import com.mes.modules.material.services.MaterialDefinitionPropertyRepository;
import com.mes.modules.material.services.MaterialDefinitionRepository;
import com.mes.modules.milanes.fact.MaterialDefinitionWorkMaster;
import com.mes.modules.milanes.fact.StimatedScheduledTime;
import com.mes.modules.milanes.fact.ToProcess;
import com.mes.modules.milanes.utilities.DateUtility;
import com.mes.modules.personnel.services.PersonPropertyRepository;
import com.mes.modules.personnel.services.PersonRepository;
import com.mes.modules.workDirective.services.WorkDirectiveRepository;
import com.mes.modules.workSchedule.services.JobOrderRepository;

@Service("rulesService")
public class RulesServiceImpl implements RulesService {
	@Autowired
	private MaterialDefinitionRepository definitionRepository;
	@Autowired
	private MaterialDefinitionPropertyRepository definitionPropertyRepository;
	@Autowired
	private JobOrderRepository jobOrderRepository;
	@Autowired
	private PersonPropertyRepository personPropertyRepository;
	@Autowired
	private KieContainer kieContainer;

	public void setProcessingDays(ToProcess toProcess) {
		KieSession kSession = kieContainer.newKieSession("ksession-rulesDays");
		kSession.insert(toProcess);
		kSession.fireAllRules();
	}

	private MaterialDefinitionWorkMaster getMaterialDefinition(
			MaterialDefinitionWorkMaster materialDefinitionWorkMaster
	) {
		KieSession kSession = kieContainer.newKieSession("ksession-workDefinitionWorkMaster");
		kSession.insert(materialDefinitionWorkMaster);
		kSession.fireAllRules();
		return materialDefinitionWorkMaster;
	}

	public ToProcess getScheduledFinishedDate(String materialCode, String workType, boolean hasflash) {
		ToProcess toProcess = new ToProcess();
		Date initDate = getInitialDate();
		Date configuredInidDate = startDateTimeConfiguration(initDate);
		toProcess.setScheduledStartDate(configuredInidDate);
		toProcess.setWorkType(workType);
		toProcess.setHasflash(hasflash);
		setEndDate(toProcess, initDate, workType, hasflash);
		if (toProcess.getScheduledEndDate() == null) {
			if (materialCode != null) {
				MaterialDefinition materialDefinition = definitionRepository.findByCode(materialCode);
				MaterialDefinitionProperty productTypeProperty = definitionPropertyRepository
						.findByCodeAndMaterialDefinition(materialDefinition, "productType");
				MaterialDefinitionProperty materialTypeProperty = definitionPropertyRepository
						.findByCodeAndMaterialDefinition(materialDefinition, "materialType");
				// a material definition may not have those attributes defined, so that we check that case
				if (productTypeProperty != null) {
					toProcess.setProductType(productTypeProperty.getValue());
				}
				if (materialTypeProperty != null) {
					toProcess.setMaterialType(materialTypeProperty.getValue());
				}
				setEndDate(toProcess, initDate, workType, hasflash);
			}
		}
		if (toProcess.getNormalDays() == 0) {
			toProcess.setScheduledEndDate(null);
		}
		return toProcess;
	}

	public Date getInitialDate() {
		Calendar now = Calendar.getInstance();
		return (now.get(Calendar.HOUR_OF_DAY) >= 13)?
				DateUtility.getNextDayEnabled(now.getTime(), 1):
				DateUtility.getEnableDay(now.getTime());
	}
	
	public Date getFinishDate(Date initDate, int daysNumber){
		return DateUtility.getNextDayEnabled(initDate, daysNumber - 1);
	}

	public void setEndDate(ToProcess toProcess, Date initDate, String workType, Boolean hasflash) {
		// Running service that run rules
		// ToProcess toProcessUpdated = scheduledDateService.getProcessingDays(toProcess);
		setProcessingDays(toProcess);
		// Setting ScheduledFinishedDate Base on hasFlash Parameter, if a
		// material does not have flashDay normaldays are set as default
		if ((toProcess.getNormalDays() == null) && (toProcess.getFlashDays() == null)
				&& (toProcess.getTestDays() == null)) {
			System.out.println("regla de fecha no especificada para el valor de hechos provisto");
		} else {
			if ((toProcess.getNormalDays() == null) && (toProcess.getFlashDays() == null)
					&& (toProcess.getTestDays() == null)) {
				System.out.println("regla de fecha no especificada para el valor de hechos provisto");
			} else {
				toProcess.setScheduledProcessingDays(toProcess.getNormalDays());
				Double testDays = toProcess.getTestDays();
				Double flashDays = toProcess.getFlashDays();
				if ((workType.equals("PR") || workType.equals("IR")) && testDays != null) {
					toProcess.setScheduledProcessingDays(testDays);
				} else if (hasflash && flashDays != null) {
					toProcess.setScheduledProcessingDays(flashDays);
				}
			}
			Date finishDate = this.getFinishDate(initDate, toProcess.getScheduledProcessingDays().intValue());
			toProcess.setScheduledEndDate(finishDate);
		}
	}

	public MaterialDefinitionWorkMaster getMaterialDefinitionWorkMaster(String materialCode) {
		MaterialDefinitionWorkMaster materialDefinitionWorkMaster = new MaterialDefinitionWorkMaster();
		if (materialCode != null) {
			MaterialDefinition materialDefinition = definitionRepository.findByCode(materialCode);
			MaterialDefinitionProperty productTypeProperty = definitionPropertyRepository
					.findByCodeAndMaterialDefinition(materialDefinition, "productType");
			MaterialDefinitionProperty productSubTypeProperty = definitionPropertyRepository
					.findByCodeAndMaterialDefinition(materialDefinition, "productSubType");
			MaterialDefinitionProperty materialTypeProperty = definitionPropertyRepository
					.findByCodeAndMaterialDefinition(materialDefinition, "materialType");
			MaterialDefinitionProperty materialSubTypeProperty = definitionPropertyRepository
					.findByCodeAndMaterialDefinition(materialDefinition, "materialSubType");
			// a material definition may not have those attributes defined, so that we check that case
			if (productTypeProperty != null) {
				materialDefinitionWorkMaster.setProductType(productTypeProperty.getValue());
			}
			if (productSubTypeProperty != null) {
				materialDefinitionWorkMaster.setProductSubType(productSubTypeProperty.getValue());
			}
			if (materialTypeProperty != null) {
				materialDefinitionWorkMaster.setMaterialType(materialTypeProperty.getValue());
			}
			if (materialSubTypeProperty != null) {
				materialDefinitionWorkMaster.setMaterialSubType(materialSubTypeProperty.getValue());
			}
		}
		// Running service that run rules
		MaterialDefinitionWorkMaster materialDefinitionWorkMasterUpdated = this
				.getMaterialDefinition(materialDefinitionWorkMaster);
		// Setting ScheduledFinishedDate Base on hasFlash Parameter, if a
		// material does not have flashDay normaldays are set as default
		if (materialDefinitionWorkMasterUpdated.getReceta() == null) {
			System.out.println("Receta no especificada para los hechos provisto");
		}
		return materialDefinitionWorkMasterUpdated;
	}

	public StimatedScheduledTime getScheduledTotalFinishedDate(Long jobOrderId) {
		StimatedScheduledTime stimatedScheduledTime = new StimatedScheduledTime();
		JobOrder jobOrder = jobOrderRepository.findById(jobOrderId);
		String workType = jobOrder.getParameter("workType").getValue();
		Boolean hasFlash = this.hasFlash(jobOrder, workType);
		for (JobOrderMaterialRequirement materialRequirement : jobOrder.getMaterialRequirements()) {
			MaterialDefinition materialDefinition = materialRequirement.getMaterialDefinition();
			if (materialRequirement.getMaterialUse().equals(MaterialUse.PRODUCED)) {
				ToProcess toProcess = getScheduledFinishedDate(materialDefinition.getCode(), workType, hasFlash);
				this.maxStimatedScheduledTime(stimatedScheduledTime, toProcess);
				//this.addStimatedScheduledTime(stimatedScheduledTime, toProcess);
			}
		}
		this.setStimatedScheduledTimeEndDate(stimatedScheduledTime);
		Date endDate = stimatedScheduledTime.getScheduledEndDate();		
		stimatedScheduledTime.setScheduledEndDate(this.setTimeDateParameters(endDate,12,0,0));
		stimatedScheduledTime.setDispatchCity(this.getDispatchCity(jobOrderId));
		return stimatedScheduledTime;
	}
	
	public Date setTimeDateParameters(Date date, int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();		
		calendar.setTime(date);
		calendar.setTimeZone(TimeZone.getDefault());
		calendar.set(Calendar.HOUR_OF_DAY,hour);
		calendar.set(Calendar.MINUTE,minute);
		calendar.set(Calendar.SECOND,second);
	    Date dateCalendar =calendar.getTime();
	    return dateCalendar;
	}

	private Boolean hasFlash(JobOrder jobOrder, String workType) {
		Boolean result = false;
		if (!(workType.equals("PR") || workType.equals("IR"))) {
			for (JobOrderMaterialRequirement materialRequirement : jobOrder.getMaterialRequirements()) {
				MaterialDefinition materialDefinition = materialRequirement.getMaterialDefinition();
				result = result || materialDefinition.getCode().equals("FLASH");
			}
		}
		return result;
	}
	
	public Date startDateTimeConfiguration(Date date) {
		Calendar now = Calendar.getInstance();
		Calendar paramDate = Calendar.getInstance();	
		Date scheduledEndTime = null;
		paramDate.setTime(date);
		if(now.get(Calendar.DATE) <  paramDate.get(Calendar.DATE)) {
			scheduledEndTime = this.setTimeDateParameters(paramDate.getTime(), 7, 0, 0);
		}
		else{
			scheduledEndTime = this.setTimeDateParameters(paramDate.getTime(),paramDate.get(Calendar.HOUR_OF_DAY),
								paramDate.get(Calendar.MINUTE),paramDate.get(Calendar.SECOND));
		}
		return scheduledEndTime;
	}

//	private void addStimatedScheduledTime(StimatedScheduledTime stimatedScheduledTime, ToProcess toProcess) {
//		if (stimatedScheduledTime.getScheduledStartDate() == null) {
//			stimatedScheduledTime.setScheduledStartDate(toProcess.getScheduledStartDate());
//		}
//		if(toProcess.getWorkType().equals("AT")) {
//			if (stimatedScheduledTime.getScheduledProcessingDays() == null) {
//				stimatedScheduledTime.setScheduledProcessingDays(toProcess.getScheduledProcessingDays());
//			}
//		}
//		else {
//			if (stimatedScheduledTime.getScheduledProcessingDays() == null) {
//				stimatedScheduledTime.setScheduledProcessingDays(toProcess.getScheduledProcessingDays());
//			} else {
//				Double countScheduledProcessingDays = stimatedScheduledTime.getScheduledProcessingDays()
//						+ toProcess.getScheduledProcessingDays();
//				stimatedScheduledTime.setScheduledProcessingDays(countScheduledProcessingDays);
//			}
//		}
//	}
	
	private void maxStimatedScheduledTime(StimatedScheduledTime stimatedScheduledTime, ToProcess toProcess) {
		if (stimatedScheduledTime.getScheduledStartDate() == null) {
			stimatedScheduledTime.setScheduledStartDate(toProcess.getScheduledStartDate());
		}
		if(toProcess.getWorkType().equals("AT") || toProcess.getWorkType().equals("IA")) {
			if (stimatedScheduledTime.getScheduledProcessingDays() == null) {
				stimatedScheduledTime.setScheduledProcessingDays(toProcess.getScheduledProcessingDays());
			}
		}
		else {
			if (stimatedScheduledTime.getScheduledProcessingDays() == null) {
				stimatedScheduledTime.setScheduledProcessingDays(toProcess.getScheduledProcessingDays());
			} else {
				Double countScheduledProcessingDays = Math.max(stimatedScheduledTime.getScheduledProcessingDays(),
						toProcess.getScheduledProcessingDays());
				stimatedScheduledTime.setScheduledProcessingDays(countScheduledProcessingDays);
			}
		}
	}

	public void setStimatedScheduledTimeEndDate(StimatedScheduledTime stimatedScheduledTime) {
		Date initDate = stimatedScheduledTime.getScheduledStartDate();
		if (stimatedScheduledTime.getScheduledProcessingDays() == null) {
			System.out.println("regla de fecha no especificada para el valor de hechos provisto en cálculo estimado de Tiempo ");
		} else {
			Date finishDate = this.getFinishDate(initDate, stimatedScheduledTime.getScheduledProcessingDays().intValue());
			stimatedScheduledTime.setScheduledEndDate(finishDate);
		}
	}
	
	public String getDispatchCity(Long jobOrderId){
		JobOrder jobOrder = jobOrderRepository.findById(jobOrderId);
		List<WorkDirective> WorkDirectives = jobOrder.getWorkDirectivesByWorkType(WorkTypes.INVENTORY_OUTBOUND);
		WorkDirective lastDispatch = null;
		for(WorkDirective workDirective : WorkDirectives){
			Equipment centroLogistico = workDirective.getHierarchyScope();
			if(!centroLogistico.getCode().equals("logisticoCali")){
				if(lastDispatch == null || lastDispatch.getId() < workDirective.getId()){
					lastDispatch = workDirective;
				}
			}
		}
		if(lastDispatch != null) {
			EquipmentProperty equipmentCity = lastDispatch.getHierarchyScope().getProperty("city");
			if(equipmentCity != null) {
				return equipmentCity.getValue();
			}
		}
		Long idAddress = Long.parseLong(jobOrder.getParameter("idAddress").getValue());
		PersonProperty address = personPropertyRepository.getOne(idAddress);
		return address.getProperty("city").getValue();
	}
}
