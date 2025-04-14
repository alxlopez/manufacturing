package com.mes.modules.milanes.artifacts;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class TaskCustomer implements Serializable {
	private static final long serialVersionUID = 2807009544412619594L;
	private String id;
	private String name;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'",timezone="America/Bogota")
	private Date createTime;
	private Integer priority;
	private Boolean suspended;
	private String taskDefinitionKey;
	private String category;
	private String executionId;
	private String processInstanceId;
	private String processDefinitionId;
}
