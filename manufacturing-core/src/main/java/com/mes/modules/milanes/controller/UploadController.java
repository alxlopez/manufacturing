package com.mes.modules.milanes.controller;

import java.io.IOException;
import java.util.List;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.enumerations.application.EventEntities;
import com.mes.dom.equipment.Equipment;
import com.mes.dom.event.ExecutionEvent;
import com.mes.dom.material.MaterialDefinition;
import com.mes.dom.personnel.Person;
import com.mes.dom.personnel.PersonProperty;
import com.mes.dom.personnel.PersonnelClass;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.dom.workDirective.WorkDirectiveParameter;
import com.mes.modules.equipment.services.EquipmentRepository;
import com.mes.modules.manufacturingEngine.event.controller.EventController;
import com.mes.modules.manufacturingEngine.event.services.EventDefinitionRepository;
import com.mes.modules.material.services.MaterialDefinitionRepository;
import com.mes.modules.personnel.services.PersonPropertyRepository;
import com.mes.modules.personnel.services.PersonRepository;
import com.mes.modules.personnel.services.PersonnelClassRepository;
import com.mes.modules.workDirective.services.WorkDirectiveParameterRespository;
import com.mes.modules.workDirective.services.WorkDirectiveRepository;

@Controller
@RequestMapping(value = "milanes/upload")
public class UploadController {
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private PersonnelClassRepository personnelClassRepository;
	@Autowired
	private MaterialDefinitionRepository meterialDefinitionRepository;
	@Autowired
	private EquipmentRepository equipmentRepository;
	@Autowired
	private WorkDirectiveRepository workDirectiveRepository;
	@Autowired
	private WorkDirectiveParameterRespository workDirectiveParameterRespository;
	@Autowired
	private PersonPropertyRepository personPropertyRepository;
	@Autowired
	private TaskService taskService;
	@Autowired
	private EventController eventController;
	@Autowired
	private EventDefinitionRepository eventDefinitionRepository;

	@RequestMapping(value = "/equipments", method = RequestMethod.GET)
	public String loadEquipments() {
		List<Equipment> equipments = equipmentRepository.findAll();
		for (Equipment equipment: equipments) {
			String hierarchy = this.getHierarchy(equipment);
			equipment.setHierarchyScope(hierarchy.substring(1, hierarchy.length()));
			equipmentRepository.save(equipment);
		}
		equipmentRepository.flush();
		return null;
	}

	@RequestMapping(value = "/dispatchCity", method = RequestMethod.GET)
	public String createDispatchCity() {
		List<WorkDirective> directives = workDirectiveRepository.findAll();
		for (WorkDirective wd: directives) {
			String addressId = wd.getParameter("idAddress").getValue();
			System.out.println(addressId);
			PersonProperty address = personPropertyRepository.findOne(Long.parseLong(addressId));
			String city = address.getProperty("city").getValue();
			wd.addParameter("dispatchCity", city);
			workDirectiveRepository.save(wd);
		}
		workDirectiveRepository.flush();
		return null;
	}

	@RequestMapping(value = "/material/properties", method = RequestMethod.POST)
	public String loadMaterialProperties(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
	        try {
	            String completeData = new String(file.getBytes());
	            completeData = completeData.replace("\r", "");
	            String[] rows = completeData.split("\n");
	            for (int i=1; i < rows.length; i++) {
	            	String[] columns = rows[i].split(";");
	            	MaterialDefinition materialDefinition = meterialDefinitionRepository.findByCode(columns[0]);
	            	if (materialDefinition == null) {
	            		continue;
	            	}
	            	int size = columns.length;
	            	if (size > 1 && !columns[1].isEmpty()) {
	            		materialDefinition.addProperty("productType", columns[1]);
	            	}
	            	if (size > 2 && !columns[2].isEmpty()) {
	            		materialDefinition.addProperty("productSubType", columns[2]);
	            	}
	            	if (size > 3 && !columns[3].isEmpty()) {
	            		materialDefinition.addProperty("materialType", columns[3]);
	            	}
	            	if (size > 4 && !columns[4].isEmpty()) {
	            		materialDefinition.addProperty("materialSubType", columns[4]);
	            	}
	            	meterialDefinitionRepository.save(materialDefinition);
	            }
	            meterialDefinitionRepository.flush();
	            System.out.println("CARGA FINALIZADA");
	        } catch (IOException e) {
	        	System.out.println(e);
	        }
		}
		return null;
	}

	/* Replace not existing idAddress with custom id address */
	@RequestMapping(value = "/fixIdAddress", method = RequestMethod.POST)
	public String fixIdAddress(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
	        try {
	        	String completeData = new String(file.getBytes());
	            completeData = completeData.replace("\r", "");
	            String[] rows = completeData.split("\n");
	            for (int i=1; i < rows.length; i++) {
	            	System.out.println("WorkDirective: "+rows[i]);	            	
	            	WorkDirectiveParameter workDirectiveParameter = workDirectiveParameterRespository.findOne(Long.parseLong(rows[i].trim()));
					if(workDirectiveParameter.getCode().equals("idAddress")) {
						String idAddress = workDirectiveParameter.getValue();
						WorkDirective workDirective = workDirectiveParameter.getWorkDirective();
						String idCustomerString = workDirective.getParameter("idCustomer").getValue();
						Long idCustomer = Long.parseLong(idCustomerString);
						Person person = personRepository.findOne(idCustomer);
						Long customIdAddress = person.getProperty("address").getId();
						System.out.println("WorkDirective Out: "+workDirective.getId().toString());
						if(!idAddress.equals(customIdAddress.toString())) {
							System.out.println("WorkDirective: "+workDirective.getId().toString());
							System.out.println("previo idAddress: "+ idAddress);
							System.out.println("nuevo idAddress: "+ customIdAddress.toString());
							workDirectiveParameter.setValue(customIdAddress.toString());
							workDirectiveParameter.getWorkDirective().getJobOrder().getParameter("idAddress").setValue(customIdAddress.toString());
							workDirectiveParameterRespository.save(workDirectiveParameter);
						}
					}
				}
	        }catch (IOException e) {
	        	System.out.println(e);
	        }
		}
		return "Ok";
	}
	
	@RequestMapping(value = "/dispatch", method = RequestMethod.GET)
	public void closeDispatch() {
		List<Task> tasks = taskService.createTaskQuery()
			    .processVariableValueEquals("directiveWorkType", "INVENTORY_OUTBOUND")
			    .list();
				for (Task task: tasks) {
					String state = taskService.getVariableLocal(task.getId(), "state").toString();
					Object localDelivery = taskService.getVariable(task.getId(), "localDelivery");
					System.out.println("assignee: " + task.getAssignee());
					System.out.println("TASK: " + task.getId());
					System.out.println("PROCESS: " + task.getProcessInstanceId());
					System.out.println(state);
					if (localDelivery == null) {
						taskService.setVariable(task.getId(), "localDelivery", "INCLUDED");
					}
					if (state.equals("READY") && task.getAssignee() != null) {
						taskService.setVariableLocal(task.getId(), "state", "RUNNING");
						continue;
					}
					if (state.equals("COMPLETE")) {
						taskService.setVariableLocal(task.getId(), "state", "RUNNING");
						continue;
					}
					ExecutionEvent event = new ExecutionEvent();
					event.setEntityId(Long.parseLong(task.getId()));
					event.setEntityType(EventEntities.USER_TASK);
					event.setEventDefinition(this.eventDefinitionRepository.findByCode(state.equals("READY")? Commands.START: Commands.COMPLETE));
					event.setReporterId("admin@labmilanes.com");
					eventController.createEvent(event);
				}
				System.out.println("FINISH");
	}

	@RequestMapping(value = "/endProcess", method = RequestMethod.POST)
	public String finalizeProcess(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
	        try {
	            String completeData = new String(file.getBytes());
	            completeData = completeData.replace("\r", "");
	            String[] rows = completeData.split("\n");
	            boolean aborted;
	            //boolean removed;
	            for (int i=1; i < rows.length; i++) {
		            aborted = false;
		            //removed = false;
	            	String[] columns = rows[i].split(";");
	            	System.out.println("Work_Request: "+columns[0]);
	            	aborted = this.abortProcess(columns[0].trim());
	            	//removed = this.removeProcess(columns[5].trim());
	            	if(aborted)
	            		System.out.println("Work_Request: "+columns[0]+" abortado");
	            	/*if(removed)
	            		System.out.println("Work_Request: "+columns[5]+" removido");*/
	            }
	            //System.out.println("PROCESOS ABORTADOS y REMOVIDOS");
	            System.out.println("PROCESOS ABORTADOS");
	        } catch (IOException | IndexOutOfBoundsException e) {
	        	System.out.println(e);
	        }
		}
		return null;
	}
	
	private boolean abortProcess(String workRequestId) {
		List<Task> tasks = taskService.createTaskQuery()
	    .processVariableValueEquals("workRequestId", workRequestId)
	    .processVariableValueEquals("directiveWorkType", "PRODUCTION")
	    .list();
		if(tasks.size() > 0)
		{
			String processInstanceId = tasks.get(0).getProcessInstanceId();
			System.out.println("PROCESS: " + processInstanceId);
			System.out.println("WORKREQUEST: " + workRequestId);
			ExecutionEvent event = new ExecutionEvent();
			event.setEntityId(Long.parseLong(processInstanceId));
			event.setEntityType(EventEntities.PROCESS);
			event.setEventDefinition(this.eventDefinitionRepository.findByCode(Commands.ABORT));
			event.setReporterId("admin@labmilanes.com");
			eventController.createEvent(event);
			return true;
		}
		return false;
	}
	
	private boolean removeProcess(String workRequestId) {
		List<Task> tasks = taskService.createTaskQuery()
	    .processVariableValueEquals("workRequestId", workRequestId)
	    .processVariableValueEquals("directiveWorkType", "PRODUCTION")
	    .list();
		if(tasks.size() > 0) {
			String processInstanceId = tasks.get(0).getProcessInstanceId();
			System.out.println("PROCESS: " + processInstanceId);
			System.out.println("WORKREQUEST: " + workRequestId);
			ExecutionEvent event = new ExecutionEvent();
			event.setEntityId(Long.parseLong(processInstanceId));
			event.setEntityType(EventEntities.PROCESS);
			event.setEventDefinition(this.eventDefinitionRepository.findByCode(Commands.REMOVE));
			event.setReporterId("admin@labmilanes.com");
			eventController.createEvent(event);
			return true;
		}
		return false;
	}
	
	@RequestMapping(value = "/customers", method = RequestMethod.POST)
	public String loadCustomers(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
	        try {
	            String completeData = new String(file.getBytes());
	            String[] rows = completeData.split(System.getProperty("line.separator"));
	            PersonnelClass personnelClass = personnelClassRepository.findById((long) 1);
	            for (int i=1; i < rows.length; i++) {
	            	String[] columns = rows[i].split(";");
	            	if (columns.length < 5) {
	            		continue;
	            	}
	            	if (personRepository.findByCode(columns[0]) != null) {
	            		System.out.println("Fallo la creación del cliente " + columns[0] + " ya existe");
	            		continue;
	            	}
	            	Person person = new Person(columns[0], columns[2]);
	            	String telephone = "";
	            	if (columns.length > 5 && !columns[5].isEmpty()) {
	            		telephone = columns[5];
	            	} else if (columns.length > 6 && !columns[6].isEmpty()) {
	            		telephone = columns[6];
	            	} else if (columns.length > 7 && !columns[7].isEmpty()) {
	            		telephone = columns[7];
	            	}
	            	PersonProperty address = person.addProperty("address", columns[3]);
	            	address.addProperty("city", columns[4]);
	            	address.addProperty("phone", telephone);
	            	person.addProperty("documentType", "NIT");
	            	person.getPersonnelClasses().add(personnelClass);
	            	personnelClass.getPersonnel().add(person);
	            	personRepository.save(person);
	            }
	            personRepository.flush();
	            System.out.println("CARGA FINALIZADA");
	        } catch (IOException e) {
	        	System.out.println(e);
	        }
		}
		return null;
	}

	private String getHierarchy(Equipment equipment) {
		String hierarchy = "";
		Equipment parent = equipment.getParent();
		if (parent != null) {
			hierarchy += this.getHierarchy(parent);
		}
		return hierarchy + "/" + equipment.getDescription();
	}
}
