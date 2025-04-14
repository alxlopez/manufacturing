package com.mes.dom.event;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.flowable.rest.util.DateToStringSerializer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mes.dom.enumerations.application.EventEntities;
import com.mes.dom.enumerations.application.EventTypes;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(exclude={"parameters"})
public class ExecutionEvent implements Serializable {
	private static final long serialVersionUID = -1939843370614525105L;
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column(name="id")	
	private Long id;
	@ManyToOne
	@JoinColumn(name = "eventDefinition", referencedColumnName="id")
	@JsonBackReference("eventDefinition")
	private EventDefinition eventDefinition;
	@Enumerated (EnumType.STRING)
	private  EventTypes eventType;
	private String reporterId;	
	@Enumerated (EnumType.STRING)
	private EventEntities entityType;
	private Long entityId;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = DateToStringSerializer.class, as=Date.class)
	private Date throwTime;
	private String action;
	private String actionResult;
	@OneToMany(mappedBy = "event",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
	@JsonManagedReference("event")
	private Collection<EventParameter> parameters = new HashSet<EventParameter>();

	public ExecutionEvent() { }

	public ExecutionEvent(Long id) {
		this.id = id;
	}

	public EventParameter getParameter(String code) {
		for (EventParameter eventProperty: this.parameters) {
			if (eventProperty.getCode().equals(code)) {
				return eventProperty;
			}
		}
		return null;
	}

	public Collection<EventParameter> getParameters(String code) {
		Collection<EventParameter> properties = Collections.emptyList();
		for (EventParameter eventProperty: this.parameters) {
			if (eventProperty.getCode().equals(code)) {
				properties.add(eventProperty);
			}
		}
		return properties;
	}

	public ExecutionEvent clone() {
		ExecutionEvent obj = new ExecutionEvent();
		obj.setEntityId(this.entityId);
		obj.setEntityType(this.entityType);  
		obj.setEventDefinition(this.eventDefinition);
		obj.setReporterId(this.reporterId);       
        Set<EventParameter> parameterCollection = new HashSet<EventParameter>();
        for(EventParameter parameter: this.parameters){
        	EventParameter newParameter = parameter.clone();
        	newParameter.setEvent(obj);
        	parameterCollection.add(newParameter);
        }        
        obj.setParameters(parameterCollection);
        return obj;
    }
}
