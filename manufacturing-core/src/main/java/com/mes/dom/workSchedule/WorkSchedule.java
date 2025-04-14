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
import com.mes.dom.enumerations.application.ScheduleState;
import com.mes.dom.enumerations.application.WorkTypes;
import com.mes.dom.equipment.Equipment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"workRequests", "workSchedule"})
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
@Table(name="schedule_work_schedule")
public class WorkSchedule implements Serializable{
	private static final long serialVersionUID = -1139039185983626078L;
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
	private ScheduleState scheduleState;
	private Timestamp publishedDate; 
	@OneToMany(mappedBy = "workSchedule",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference("workSchedule")
	private Collection<WorkRequest> workRequests = new HashSet<WorkRequest>();
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=true,updatable=true) 
	@JsonBackReference("workSchedule-parent-child")
	private WorkSchedule parent;
	@OneToMany(cascade={CascadeType.PERSIST} ) 
	@JoinColumn(name = "parent_id", insertable = true, updatable = false) 
	@JsonManagedReference("workSchedule-parent-child")
	private Set<WorkSchedule> workSchedule = new HashSet<WorkSchedule>();
	/*----------------------------------------------------------------------------*/

	public WorkSchedule() { }
	
	public WorkSchedule(Long id) {
		this.id=id;
	}
	
	public WorkRequest getWorkRequest(String code) {
		for (WorkRequest workRequest: this.workRequests) {
			if (workRequest.getCode().equals(code)) {
				return workRequest;
			}
		}
		return null;
	}
	
	public List<WorkRequest> getWorkRequests(String code) {
		ArrayList<WorkRequest> workRequests = new ArrayList<WorkRequest>();
		for (WorkRequest workRequest: this.workRequests) {
			if (workRequest.getCode().equals(code)) {
				workRequests.add(workRequest);
			}
		}
		return workRequests;
	}
	
	public WorkSchedule getWorkSchedule(String code) {
		for (WorkSchedule workScheduleObj: this.workSchedule) {
			if (workScheduleObj.getCode().equals(code)) {
				return workScheduleObj;
			}
		}
		return null;
	}
	
	public List<WorkSchedule> getWorkSchedules(String code) {
		ArrayList<WorkSchedule> workSchedules = new ArrayList<WorkSchedule>();
		for (WorkSchedule workScheduleObj: this.workSchedule) {
			if (workScheduleObj.getCode().equals(code)) {
				workSchedules.add(workScheduleObj);
			}
		}
		return workSchedules;
	}
	
	public void addWorkRequests(WorkRequest workRequest) {
		workRequest.setWorkSchedule(this);
		this.workRequests.add(workRequest);
	}
	
	public void addWorkSchedules(WorkSchedule workSchedule) {
		workSchedule.setParent(this);
		this.workSchedule.add(workSchedule);
	}
}
