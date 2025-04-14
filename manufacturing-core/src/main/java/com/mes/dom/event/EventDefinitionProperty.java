package com.mes.dom.event;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mes.dom.common.Property;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=true)
public class EventDefinitionProperty extends Property implements Serializable {
	private static final long serialVersionUID = 6998448939042962735L;
	@ManyToOne
	@JoinColumn(name = "eventDefinition", referencedColumnName="id")
	@JsonBackReference("eventDefinition")
	private EventDefinition eventDefinition;

	public EventDefinitionProperty() { }

	public EventDefinitionProperty(Long id) {
		this.setId(id);
	}
}
