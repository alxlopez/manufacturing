package com.mes.dom.event;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.enumerations.application.EventType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(exclude={"properties", "events", "recipients"})
public class EventDefinition implements Serializable {
	private static final long serialVersionUID = -540966975841999118L;
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column(name="id")	
	private Long id;
	@Column(nullable=false,length=70)
	@Enumerated (EnumType.STRING)
	private Commands code;
	@Enumerated (EnumType.STRING)
	private EventType eventType;
	private String description;
	@Column(columnDefinition = "mediumtext")
	private String notificationBody;
	@OneToMany(mappedBy = "eventDefinition",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
	@JsonManagedReference("eventDefinition")
	private Collection<EventDefinitionProperty> properties;
	@OneToMany(mappedBy = "eventDefinition",fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval=true)
	@JsonManagedReference("eventDefinition")
	private Collection<ExecutionEvent> events;
	@OneToMany(mappedBy = "eventDefinition",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
	@JsonManagedReference("eventDefinition")
	private Collection<EventDefinitionNotificationRecipient> recipients;

	public EventDefinition() { }

	public EventDefinition(Long id) {
		this.id=id;
	}

	public EventDefinitionProperty getProperty(String code) {
		for (EventDefinitionProperty eventDefinitionProperty: this.properties) {
			if (eventDefinitionProperty.getCode().equals(code)) {
				return eventDefinitionProperty;
			}
		}
		return null;
	}

	public Collection<EventDefinitionProperty> getProperties(String code) {
		Collection<EventDefinitionProperty> properties = Collections.emptyList();
		for (EventDefinitionProperty eventDefinitionProperty: this.properties) {
			if (eventDefinitionProperty.getCode().equals(code)) {
				properties.add(eventDefinitionProperty);
			}
		}
		return properties;
	}
}
