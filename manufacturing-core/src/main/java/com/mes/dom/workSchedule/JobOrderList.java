package com.mes.dom.workSchedule;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mes.dom.enumerations.application.WorkTypes;
import com.mes.dom.equipment.Equipment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"jobOrders"})
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
@Table(name="schedule_job_order_list")
public class JobOrderList implements Serializable{
	private static final long serialVersionUID = -6390403787267745116L;
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column(name="id")	
	private Long id;
	@Column(nullable=true,length=70)
	private String code;
	@Column(columnDefinition = "mediumtext")
	private String description;
	@OneToOne
	@JoinColumn(name="location",referencedColumnName="id")
	private Equipment location;
	private String hierarchyScope;
	@Enumerated(EnumType.STRING)
	private WorkTypes workType;
	private Timestamp startTime;
	private Timestamp endTime;
	@OneToMany(mappedBy = "jobOrderList",fetch = FetchType.LAZY)
	@JsonBackReference("jobOrderList")
	private Collection<JobOrder> jobOrders = new HashSet<JobOrder>();
	
	public JobOrderList() { }
	
	public JobOrderList(Long id) {
		this.id = id;
	}
	
	public JobOrder getJobOrder(String code) {
		for (JobOrder jobOrder: this.jobOrders) {
			if (jobOrder.getCode().equals(code)) {
				return jobOrder;
			}
		}
		return null;
	}

	public List<JobOrder> getJobOrders(String code) {
		ArrayList<JobOrder> jobOrders = new ArrayList<JobOrder>();
		for (JobOrder jobOrder: this.jobOrders) {
			if (jobOrder.getCode().equals(code)) {
				jobOrders.add(jobOrder);
			}
		}
		return jobOrders;
	}

	public void addJobOrder(JobOrder jobOrder) {
		jobOrder.setJobOrderList(this);
		this.jobOrders.add(jobOrder);
	}
}
