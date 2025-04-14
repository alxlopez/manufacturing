package com.mes.modules.milanes.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mes.dom.workSchedule.JobOrder;
import com.mes.dom.workSchedule.WorkRequest;
import com.mes.modules.milanes.artifacts.JobOrderCustomer;
import com.mes.modules.milanes.artifacts.TaskCustomer;
import com.mes.query.translators.QueryJobOrder;


@Controller
@RequestMapping(value = "milanes")
public class JobOrderCustomerController {
	@PersistenceContext
    private EntityManager em;
//	@Autowired
//	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	
	@RequestMapping(value = "/portal/client", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> portal(Pageable pageable, @RequestParam(value="document") String document) {
		QueryJobOrder qj = new QueryJobOrder(em);
		List<JobOrder> jobOrders = qj.join("idCustomer", "com.mes.dom.personnel.Person")
		.filter("parameter.idCustomer.code", "equals", document)
		.filter("hierarchyScope", "equals", "PRODUCTION")
		.page(pageable)
		.run();
		ArrayList<JobOrderCustomer> jobOrderCustomers = new ArrayList<JobOrderCustomer>();
		for (JobOrder jobOrder : jobOrders) {
			TaskQuery taskQuery = taskService.createTaskQuery();
			JobOrderCustomer jobOrderCustomer = new JobOrderCustomer();
			taskQuery.processVariableValueEquals("jobOrderId", jobOrder.getId());			
			List<Task> tasks  = taskQuery.list();
			List<TaskCustomer> taskList = new ArrayList<TaskCustomer>();
			for(Task task : tasks) {
				TaskCustomer taskCustomer = new TaskCustomer();
				taskCustomer.setId(task.getId());
				taskCustomer.setName(task.getName());
				taskCustomer.setCreateTime(task.getCreateTime());
				taskCustomer.setPriority(task.getPriority());
				taskCustomer.setSuspended(task.isSuspended());
				taskCustomer.setTaskDefinitionKey(task.getTaskDefinitionKey());
				taskCustomer.setCategory(task.getCategory());
				taskCustomer.setExecutionId(task.getExecutionId());
				taskCustomer.setProcessInstanceId(task.getProcessInstanceId());
				taskCustomer.setProcessDefinitionId(task.getProcessDefinitionId());
				//Quitar if al estar actualizados procesos de despacho con Mensajeros: category='operation' en producción
				if(taskCustomer.getCategory() != null){
					taskList.add(taskCustomer);
				}
			}
			WorkRequest workRequest = jobOrder.getWorkRequest();
			jobOrderCustomer.setJobOrderId(jobOrder.getId());
			jobOrderCustomer.setJobOrder(jobOrder);
			jobOrderCustomer.setWorkRequest(workRequest);
			jobOrderCustomer.setWorkMaster(jobOrder.getWorkMaster().getDescription());
			jobOrderCustomer.setTasks(taskList);
			jobOrderCustomers.add(jobOrderCustomer);
		}
		return ResponseEntity.ok(jobOrderCustomers);
	}

}
