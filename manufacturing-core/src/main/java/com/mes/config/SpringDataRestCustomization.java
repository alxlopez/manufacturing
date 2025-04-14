package com.mes.config;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.stereotype.Component;

import com.mes.dom.equipment.Equipment;
import com.mes.dom.equipment.EquipmentCapabilityTestSpecification;
import com.mes.dom.equipment.EquipmentClass;
import com.mes.dom.equipment.EquipmentClassProperty;
import com.mes.dom.equipment.EquipmentProperty;
import com.mes.dom.event.ExecutionEvent;
import com.mes.dom.event.EventDefinition;
import com.mes.dom.event.EventDefinitionProperty;
import com.mes.dom.event.EventDefinitionNotificationRecipient;
import com.mes.dom.event.EventParameter;
import com.mes.dom.event.EventType;
import com.mes.dom.event.Failure;
import com.mes.dom.material.MaterialClass;
import com.mes.dom.material.MaterialClassProperty;
import com.mes.dom.material.MaterialDefinition;
import com.mes.dom.material.MaterialDefinitionProperty;
import com.mes.dom.material.MaterialLot;
import com.mes.dom.material.MaterialLotProperty;
import com.mes.dom.material.MaterialSubLot;
import com.mes.dom.material.MaterialSubLotProperty;
import com.mes.dom.material.MaterialTestSpecification;
import com.mes.dom.material.TestedMaterialClassProperty;
import com.mes.dom.material.TestedMaterialDefinitionProperty;
import com.mes.dom.personnel.Person;
import com.mes.dom.personnel.PersonProperty;
import com.mes.dom.personnel.PersonnelClass;
import com.mes.dom.personnel.PersonnelClassProperty;
import com.mes.dom.personnel.QualificationTestSpecification;
import com.mes.dom.resourceRelationshipNetwork.ConnectionProperty;
import com.mes.dom.resourceRelationshipNetwork.ConnectionPropertyValue;
import com.mes.dom.resourceRelationshipNetwork.FromResourceReference;
import com.mes.dom.resourceRelationshipNetwork.FromResourceReferenceProperty;
import com.mes.dom.resourceRelationshipNetwork.FromResourceReferencePropertyValue;
import com.mes.dom.resourceRelationshipNetwork.ResourceNetworkConnection;
import com.mes.dom.resourceRelationshipNetwork.ResourceRelationshipNetwork;
import com.mes.dom.resourceRelationshipNetwork.ToResourceReference;
import com.mes.dom.resourceRelationshipNetwork.ToResourceReferenceProperty;
import com.mes.dom.resourceRelationshipNetwork.ToResourceReferencePropertyValue;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.dom.workDirective.WorkDirectiveMaterialSpecification;
import com.mes.dom.workDirective.WorkDirectiveMaterialSpecificationProperty;
import com.mes.dom.workDirective.WorkDirectiveParameter;
import com.mes.dom.workDirective.WorkMaster;
import com.mes.dom.workDirective.WorkMasterParameter;
import com.mes.dom.workSchedule.JobOrder;
import com.mes.dom.workSchedule.JobOrderMaterialRequirement;
import com.mes.dom.workSchedule.JobOrderMaterialRequirementProperty;
import com.mes.dom.workSchedule.JobOrderParameter;
import com.mes.dom.workSchedule.WorkRequest;

/*
 * it allows to define which jpa entities will be exposed the id in the rest resource
 */
@Component
public class SpringDataRestCustomization extends RepositoryRestConfigurerAdapter {
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(
				MaterialClass.class,
				MaterialClassProperty.class,
				MaterialDefinition.class,
				MaterialDefinitionProperty.class,
				MaterialLot.class,
				MaterialLotProperty.class,
				MaterialSubLot.class,
				MaterialSubLotProperty.class,
				MaterialTestSpecification.class,
				TestedMaterialClassProperty.class,
				TestedMaterialDefinitionProperty.class,
				EquipmentClass.class,
				EquipmentClassProperty.class,
				Equipment.class,
				EquipmentProperty.class,
				EquipmentCapabilityTestSpecification.class,
				PersonnelClass.class,
				PersonnelClassProperty.class,
				Person.class,
				PersonProperty.class,
				QualificationTestSpecification.class,
				WorkRequest.class,
				JobOrder.class,
				JobOrderParameter.class,
				JobOrderMaterialRequirement.class,
				JobOrderMaterialRequirementProperty.class,
				ResourceRelationshipNetwork.class,
				ResourceNetworkConnection.class,
				ConnectionProperty.class,
				ConnectionPropertyValue.class,
				FromResourceReference.class,
				FromResourceReferenceProperty.class,
				FromResourceReferencePropertyValue.class,
				ToResourceReference.class,
				ToResourceReferenceProperty.class,
				ToResourceReferencePropertyValue.class,
				WorkDirective.class,
				WorkDirectiveMaterialSpecification.class,
				WorkDirectiveMaterialSpecificationProperty.class,
				WorkDirectiveParameter.class,
				ExecutionEvent.class,
				EventParameter.class,
				EventType.class,
				EventDefinition.class,
				EventDefinitionProperty.class,
				EventDefinitionNotificationRecipient.class,
				Failure.class,
				WorkMaster.class,
				WorkMasterParameter.class
		);
	}
}
