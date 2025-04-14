package com.mes.dom.workSchedule;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mes.dom.enumerations.application.Priority;
import com.mes.dom.enumerations.application.WorkTypes;
import com.mes.dom.equipment.Equipment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"jobOrders", "workRequests"})
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
@Table(name="schedule_work_request")
public class WorkRequest implements Serializable{
	private static final long serialVersionUID = -4248636895065381652L;
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column(name="id")	
	private Long id;
	@Column(nullable=true,length=70)
	private String code;	
	@Enumerated(EnumType.STRING)	
	private WorkTypes workType;
	private String hierarchyScope;
	@OneToOne
	@JoinColumn(name="location",referencedColumnName="id")
	private Equipment location;
	@Column(columnDefinition = "mediumtext")
	private String description;
	private Timestamp startTime;
	private Timestamp endTime;
	@Enumerated(EnumType.STRING)
	private Priority priority;
	@OneToMany(mappedBy = "workRequest",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference("workRequest")
	private Collection<JobOrder> jobOrders = new HashSet<JobOrder>() ;	
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=true,updatable=true) 
	@JsonBackReference("workRequest-parent-child")
	private WorkRequest parent;
	@OneToMany(cascade={CascadeType.PERSIST} ) 
	@JoinColumn(name = "parent_id", insertable = true, updatable = false) 
	@JsonManagedReference("workRequest-parent-child")
	private Set<WorkRequest> workRequests = new HashSet<WorkRequest>();
	/*----------------------------------------------------------------------------*/
	@ManyToOne
	@JoinColumn(name = "workSchedule", referencedColumnName="id")
	@JsonBackReference("workSchedule")
	private WorkSchedule workSchedule;

	public WorkRequest() { }

	public WorkRequest(Long id) {
		this.id=id;
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
}
