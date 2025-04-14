package com.mes.dom.workSchedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mes.dom.common.Property;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude={"properties"}, callSuper=true)
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="@UUID")
@Table(name="schedule_job_order_material_requirement_property")
public class JobOrderMaterialRequirementProperty extends Property implements Serializable {
	private static final long serialVersionUID = -5445192153384254489L;
	@ManyToOne
	@JoinColumn(name = "jobOrderMaterialRequirement", referencedColumnName="id")
	@JsonBackReference("jobOrderMaterialRequirement-property")
	private JobOrderMaterialRequirement jobOrderMaterialRequirement;
	/*----------------------------------------------------------------------------*/
	//Changed assembly in another table to be contained in the same parent table
	/*----------------------------------------------------------------------------*/
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "parent_id",insertable=false,updatable=true) 
	@JsonBackReference("jobOrderMaterialRequirementProperty-parent-child")
	private JobOrderMaterialRequirementProperty parent;
	@OneToMany(cascade={CascadeType.ALL},orphanRemoval = true ) 
	@JoinColumn(name = "parent_id") 
	@JsonManagedReference("jobOrderMaterialRequirementProperty-parent-child")
	private Set<JobOrderMaterialRequirementProperty> properties = new HashSet<JobOrderMaterialRequirementProperty>();
	/*----------------------------------------------------------------------------*/
	protected JobOrderMaterialRequirementProperty() { }

	public JobOrderMaterialRequirementProperty getProperty(String code) {
		for (JobOrderMaterialRequirementProperty jobOrderMaterialRequirementProperty: this.properties) {
			if (jobOrderMaterialRequirementProperty.getCode().equals(code)) {
				return jobOrderMaterialRequirementProperty;
			}
		}
		return null;
	}

	public List<JobOrderMaterialRequirementProperty> getProperties(String code) {
		ArrayList<JobOrderMaterialRequirementProperty> properties = new ArrayList<JobOrderMaterialRequirementProperty>();
		for (JobOrderMaterialRequirementProperty jobOrderMaterialRequirementProperty: this.properties) {
			if (jobOrderMaterialRequirementProperty.getCode().equals(code)) {
				properties.add(jobOrderMaterialRequirementProperty);
			}
		}
		return properties;
	}

	public void addProperty(JobOrderMaterialRequirementProperty property) {
		property.setParent(this);
		this.properties.add(property);
	}

	public void addProperty(String code, String value) {
		JobOrderMaterialRequirementProperty property = new JobOrderMaterialRequirementProperty();
		property.setCode(code);
		property.setValue(value);
		property.setParent(this);
		this.properties.add(property);
	}
}
