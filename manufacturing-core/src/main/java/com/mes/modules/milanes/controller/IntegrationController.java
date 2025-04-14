package com.mes.modules.milanes.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mes.dom.enumerations.application.MaterialUse;
import com.mes.dom.workSchedule.JobOrder;
import com.mes.dom.workSchedule.JobOrderMaterialRequirement;
import com.mes.modules.personnel.services.PersonRepository;
import com.querydsl.jpa.impl.JPAQuery;

@Controller
@RequestMapping(value = "milanes/integration")
public class IntegrationController {
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private DentalLabJobOrderController dentalLabJobOrderController;

	@RequestMapping(value = "/siesa", method = RequestMethod.GET)
	public ResponseEntity<String> integrateToSiesa(
			@RequestParam(value = "workRequest", required = false) Long workRequest,
			@RequestParam(value = "hierarchyScope", required = false) String hierarchyScope,
			@RequestParam(value = "customer", required = false) String customer,
			@RequestParam(value = "patient", required = false) String patient,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "workType", required = false) String workType,
			@RequestParam(value = "processingType", required = false) String processingType
	) {
		JPAQuery<JobOrder> query = dentalLabJobOrderController.search(workRequest, hierarchyScope, startTime, endTime, status, customer, patient, workType, processingType);
		List<JobOrder> jobOrders = query.fetch();
		String content = "";
		for(JobOrder jobOrder: jobOrders) {
			Collection<JobOrderMaterialRequirement> materials = jobOrder.getMaterialRequirements();
			for (JobOrderMaterialRequirement material: materials) {
				if (!material.getMaterialUse().equals(MaterialUse.CONSUMED)) {
					content += this.getLine(material);
				}
			}
		}
		String name = "IBS" + this.formatNumber(ThreadLocalRandom.current().nextInt(0, 100000), 5) + ".cpi";
        return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\";")
				.body(content);
	}

	private String getLine(JobOrderMaterialRequirement material) {
		JobOrder jobOrder = material.getJobOrder();
		Long idCustomer = Long.parseLong(jobOrder.getParameter("idCustomer").getValue());
		//return this.formatString(jobOrder.getId().toString(), 10) +
		return this.formatString(jobOrder.getWorkRequest().getId().toString(), 10) +
				this.formatNumber(2, 1) +
				this.formatString("", 20) +
				this.formatString(personRepository.findById(idCustomer).getCode(), 13) +
				this.formatNumber(0, 2) +
				this.formatDate(jobOrder.getStartTime()) +
				this.formatNumber(1, 3) +
				this.formatNumber(30, 2) +
				"R" +
				this.formatString("", 15) +
				this.formatString(material.getMaterialDefinition().getCode(), 15) +
				this.formatString("", 3) +
				this.formatDate(jobOrder.getEndTime()) +
				this.formatString("UND", 3) +
				this.formatValue((double) (material.getQuantity() == null? 0: material.getQuantity()), 3) +
				this.formatValue((double) 0, 3) +
				this.formatNumber(1, 1) +
				this.formatNumber(1, 3) +
				this.formatNumber(2, 2) +
				this.formatValue((double) 0, 2) +
				"0000" +
				"0000" +
				this.formatString("Integración IBISA", 20) +
				this.formatString("", 40) +
				this.formatString("", 4) +
				this.formatString(jobOrder.getDescription().replace("\n", " "), 60) +
				this.formatString("", 60) +
				this.formatString("", 40) +
				this.formatString("", 40) +
				this.formatString("", 40) +
				this.formatString("", 40) +
				this.formatString("", 13) +
				this.formatString("", 2) +
				this.formatString("", 8) +
				this.formatString("", 10) +
				System.getProperty("line.separator");
	}

	private String formatNumber(int num, int length) {
		String number = String.valueOf(num);
		int strLength = number.length();
		if (strLength > length) {
			return number.substring(0, length);
		}
		return String.format("%"+ length +"s", number).replace(' ', '0');
	}

	private String formatString(String str, int length) {
		if (str == null) {
			return String.format("%"+ length +"s", "");
		}
		int strLength = str.length();
		if (strLength > length) {
			return str.substring(0, length);
		}
		return String.format("%-"+ length +"s", str);
	}

	private String formatDate(Date date) {
		if (date != null) {
			SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd");
			return dt.format(date);
		}
		return "        ";
	}
/*
	private String formatValue(Double value) {
		DecimalFormat df = new DecimalFormat("000000000.000");
		return df.format(value).replace(",", "").replace(".", "")+"+";
	}
*/
	private String formatValue(Double value, int length) {
		String decimalString = "000000000.";
		while(length > 0){
			decimalString+="0";
			length--;
		}
		DecimalFormat df = new DecimalFormat(decimalString);
		return df.format(value).replace(",", "").replace(".", "")+"+";
	}

	@InitBinder
	public void initBinder(final WebDataBinder binder){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}
