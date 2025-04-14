package com.mes.modules.workDirective.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.config.exception.ManufacturingResourceNotFoundException;
import com.mes.dom.workDirective.BaseWorkDirective;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.dom.workDirective.WorkDirectiveMaterialSpecification;
import com.mes.dom.workDirective.WorkDirectiveMaterialSpecificationProperty;
import com.mes.dom.workDirective.WorkDirectiveParameter;
import com.mes.dom.workSchedule.JobOrder;
import com.mes.dom.workSchedule.JobOrderMaterialRequirement;
import com.mes.dom.workSchedule.JobOrderMaterialRequirementProperty;
import com.mes.dom.workSchedule.JobOrderParameter;
import com.mes.modules.workDirective.controller.exceptions.WorkDirectiveExceptionCodes;
import com.mes.modules.workSchedule.services.JobOrderRepository;

@Service("workDirectiveService")
public class WorkDirectiveServiceImpl implements WorkDirectiveService {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private WorkDirectiveRepository workDirectiveRepository;
	@Autowired
	private JobOrderRepository jobOrderRepository;

	public WorkDirective create(WorkDirective workDirective) {
		workDirective = workDirectiveRepository.save(workDirective);
		return workDirective;
	}

	public WorkDirective createBasedOnJobOrder(WorkDirective workDirective) {
		if (workDirective.getJobOrder() != null) {
			JobOrder jobOrder = jobOrderRepository.findById(workDirective.getJobOrder().getId());
			workDirective.setHierarchyScope(jobOrder.getLocation());
			workDirective.setWorkType(jobOrder.getWorkType());
			workDirective.setWorkMaster(jobOrder.getWorkMaster());
			workDirective.setParameters(this.parseParameters(workDirective, jobOrder.getParameters()));
			workDirective
			.setMaterialSpecifications(this.parseMaterials(workDirective, jobOrder.getMaterialRequirements()));
			workDirective.setDescription(jobOrder.getDescription());
			workDirective.setJobOrder(jobOrder);
			workDirective = workDirectiveRepository.save(workDirective);
		}
		return workDirective;
	}

	private Set<WorkDirectiveParameter> parseParameters(WorkDirective workDirective, Collection<JobOrderParameter> params) {
		Set<WorkDirectiveParameter> parameters = new HashSet<WorkDirectiveParameter>();
		for (JobOrderParameter param : params) {
			WorkDirectiveParameter parameter = this.getParameter(param);
			parameter.setWorkDirective(workDirective);
			parameters.add(parameter);
		}
		return parameters;
	}

	private Set<WorkDirectiveParameter> parseParameters(WorkDirectiveParameter workDirectiveParameter,
			Collection<JobOrderParameter> params) {
		Set<WorkDirectiveParameter> parameters = new HashSet<WorkDirectiveParameter>();
		for (JobOrderParameter param : params) {
			WorkDirectiveParameter parameter = this.getParameter(param);
			parameter.setParent(workDirectiveParameter);
			parameters.add(parameter);
		}
		return parameters;
	}

	private WorkDirectiveParameter getParameter(JobOrderParameter param) {
		WorkDirectiveParameter parameter = new WorkDirectiveParameter();
		parameter.setCode(param.getCode());
		parameter.setDescription(param.getDescription());
		parameter.setEnumeration(param.getEnumeration());
		parameter.setParameters(this.parseParameters(parameter, param.getParameters()));
		parameter.setUnitOfMeasure(param.getUnitOfMeasure());
		parameter.setValue(param.getValue());
		parameter.setValueType(param.getValueType());
		return parameter;
	}

	private List<WorkDirectiveMaterialSpecification> parseMaterials(WorkDirective workDirective,
			Collection<JobOrderMaterialRequirement> materialRequitements) {
		List<WorkDirectiveMaterialSpecification> materialSpecifications = new ArrayList<WorkDirectiveMaterialSpecification>();
		for (JobOrderMaterialRequirement materialRequirement : materialRequitements) {
			WorkDirectiveMaterialSpecification material = this.getMaterial(materialRequirement);
			material.setWorkDirective(workDirective);
			materialSpecifications.add(material);
		}
		return materialSpecifications;
	}

	private Set<WorkDirectiveMaterialSpecification> parseMaterials(
			WorkDirectiveMaterialSpecification materialSpecification,
			Collection<JobOrderMaterialRequirement> materialRequitements) {
		Set<WorkDirectiveMaterialSpecification> materialSpecifications = new HashSet<WorkDirectiveMaterialSpecification>();
		for (JobOrderMaterialRequirement materialRequirement : materialRequitements) {
			WorkDirectiveMaterialSpecification material = this.getMaterial(materialRequirement);
			material.setParent(materialSpecification);
			materialSpecifications.add(material);
		}
		return materialSpecifications;
	}

	private WorkDirectiveMaterialSpecification getMaterial(JobOrderMaterialRequirement materialReq) {
		WorkDirectiveMaterialSpecification material = new WorkDirectiveMaterialSpecification();
		material.setCode(materialReq.getCode());
		material.setDescription(materialReq.getDescription());
		material.setMaterialClass(materialReq.getMaterialClass());
		material.setMaterialDefinition(materialReq.getMaterialDefinition());
		material.setMaterialUse(materialReq.getMaterialUse());
		material.setQuantity(materialReq.getQuantity());
		material.setUnitOfMeasure(materialReq.getUnitOfMeasure());
		material.setSpecifications(this.parseMaterials(material, materialReq.getMaterialRequirements()));
		material.setUnitOfMeasure(materialReq.getUnitOfMeasure());
		material.setProperties(this.parseMaterialProperties(material, materialReq.getProperties()));
		return material;
	}

	private Set<WorkDirectiveMaterialSpecificationProperty> parseMaterialProperties(
			WorkDirectiveMaterialSpecification material, Collection<JobOrderMaterialRequirementProperty> props) {
		Set<WorkDirectiveMaterialSpecificationProperty> properties = new HashSet<WorkDirectiveMaterialSpecificationProperty>();
		for (JobOrderMaterialRequirementProperty prop : props) {
			WorkDirectiveMaterialSpecificationProperty property = this.getMaterialProperty(prop);
			property.setWorkDirectiveMaterialSpecification(material);
			properties.add(property);
		}
		return properties;
	}

	private Set<WorkDirectiveMaterialSpecificationProperty> parseMaterialProperties(
			WorkDirectiveMaterialSpecificationProperty materialProperty,
			Collection<JobOrderMaterialRequirementProperty> props) {
		Set<WorkDirectiveMaterialSpecificationProperty> properties = new HashSet<WorkDirectiveMaterialSpecificationProperty>();
		for (JobOrderMaterialRequirementProperty prop : props) {
			WorkDirectiveMaterialSpecificationProperty property = this.getMaterialProperty(prop);
			property.setParent(materialProperty);
			properties.add(property);
		}
		return properties;
	}

	private WorkDirectiveMaterialSpecificationProperty getMaterialProperty(JobOrderMaterialRequirementProperty prop) {
		WorkDirectiveMaterialSpecificationProperty property = new WorkDirectiveMaterialSpecificationProperty();
		property.setCode(prop.getCode());
		property.setDescription(prop.getDescription());
		property.setEnumeration(prop.getEnumeration());
		property.setProperties(this.parseMaterialProperties(property, prop.getProperties()));
		property.setUnitOfMeasure(prop.getUnitOfMeasure());
		property.setValue(prop.getValue());
		property.setValueType(prop.getValueType());
		return property;
	}

	@Override
	public WorkDirective validateWorkDirective(Long workDirectiveId) {
		WorkDirective workDirective = workDirectiveRepository.findById(workDirectiveId);
		if (workDirective == null) {
			throw new ManufacturingResourceNotFoundException(WorkDirectiveExceptionCodes.notFound);
		}
		return workDirective;
	}

	@Override
	public WorkDirective updateDescription(Long workDirectiveId, String description) {
		WorkDirective workDirective = workDirectiveRepository.findById(workDirectiveId);
		workDirective.setDescription(description);
		return workDirectiveRepository.save(workDirective);
	}

	@Override
	public void validateReferencedDirectivesIdsParameter(WorkDirective workDirective) {
		WorkDirectiveParameter referencedDirectivesIdsParam = workDirective
				.getParameter(BaseWorkDirective.PROP_REFERENCED_DIRECTIVE_IDS);
		if (referencedDirectivesIdsParam != null) {
			Collection<WorkDirectiveParameter> directivesIds = referencedDirectivesIdsParam.getParameters();
			for (WorkDirectiveParameter directiveId : directivesIds) {
				Long workDirectiveId = Long.valueOf(directiveId.getValue());
				WorkDirective referencedWorkDirective = workDirectiveRepository.findById(workDirectiveId);
				if (referencedWorkDirective == null) {
					throw new ManufacturingResourceNotFoundException("referenced workDirective " + workDirectiveId
							+ " sent as workDirectiveParameter does not exists in manufacturing repository");
				}
			}
		}

	}

	@Override
	public void saveWorkDirectiveParameters(WorkDirective workDirective, Map<String, Object> variables) {
		for (Map.Entry<String, Object> entry : variables.entrySet()) {
			String key = entry.getKey();
			String value = (String) entry.getValue();
			WorkDirectiveParameter parameter = workDirective.getParameter(key);
			if (parameter != null) {
				parameter.setValue(value);
			} else {
				workDirective.addParameter(key, value);
			}
			workDirectiveRepository.save(workDirective);
		}
	}
}
