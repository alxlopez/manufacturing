package com.mes.dom.event;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mes.dom.common.Parameter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=true)
public class EventParameter extends Parameter implements Serializable {
	private static final long serialVersionUID = 1205533697784712448L;	
	@OneToOne
	@JoinColumn(name="eventDefinitionProperty",referencedColumnName="id")
	private EventDefinitionProperty eventDefinitionProperty;
	@ManyToOne
	@JoinColumn(name = "event", referencedColumnName="id")
	@JsonBackReference("event")
	private ExecutionEvent event;
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=false,updatable=true) 
	@JsonBackReference("Parameter-parent-child")
	private EventParameter parent;
	@OneToMany(cascade={CascadeType.ALL},orphanRemoval = true) 
	@JoinColumn(name = "parent_id") 
	@JsonManagedReference("Parameter-parent-child")
	private Collection<EventParameter> parameters = new HashSet<EventParameter>();	

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

	public EventParameter clone(){
		EventParameter obj = new EventParameter();
		obj.setCode(this.getCode());
		obj.setDescription(this.getDescription());
		obj.setEnumeration(this.getEnumeration());
		obj.setUnitOfMeasure(this.getUnitOfMeasure());
		obj.setValue(this.getValue());
		obj.setValueType(this.getValueType());
		Set<EventParameter> parametersSet = new HashSet<EventParameter>();
		for(EventParameter parameter : this.parameters){
			EventParameter newParameter = parameter.clone();
			newParameter.setParent(obj);
			parametersSet.add(newParameter);
		}
		obj.setParameters(parametersSet);
		return obj;
	}
}
