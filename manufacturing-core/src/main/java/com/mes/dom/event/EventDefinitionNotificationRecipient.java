package com.mes.dom.event;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode
public class EventDefinitionNotificationRecipient implements Serializable{
	private static final long serialVersionUID = -5960351308312399976L;
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column(name="id")	
	private Long id;
	@ManyToOne
	@JoinColumn(name = "eventDefinition", referencedColumnName="id")
	@JsonBackReference("eventDefinition")
	private EventDefinition eventDefinition;
	private String name;
	private String email;

	public EventDefinitionNotificationRecipient() { }

	public EventDefinitionNotificationRecipient(Long id) {
		this.id=id;
	}
}
