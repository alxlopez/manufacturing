package com.mes.dom.enumerations.custom;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(exclude={"enumerations"})
public class EnumerationSet implements Serializable{
	private static final long serialVersionUID = 7125142406997631932L;
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO )
	@Column(name="id")
	private Long id;
	@Column(nullable=false,length=70)
	private String code;
	@Column(columnDefinition = "mediumtext")
	private String description;
	@OneToMany(mappedBy="enumerationSet", cascade = CascadeType.ALL)
	@JsonManagedReference("enumerationSet")
	private Collection<Enumeration> enumerations;

	public EnumerationSet() { }

	public EnumerationSet(Long id) {
		this.id = id; 
	}
}
