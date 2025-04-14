package com.mes.query.translators;

import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Pageable;

import com.mes.dom.workSchedule.JobOrder;
import com.mes.dom.workSchedule.JobOrderMaterialRequirement;
import com.mes.dom.workSchedule.JobOrderParameter;
import com.mes.dom.workSchedule.QJobOrder;
import com.mes.query.MesPredicate;
import com.mes.query.MesQuery;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

public class QueryJobOrder implements IQuery {
	private MesPredicate mesPredicate;
	private MesQuery<JobOrder> mesQuery;
	private JPAQuery<JobOrder> query;

	public QueryJobOrder(EntityManager em) {
		this.query = new JPAQuery<JobOrder>(em);
		this.query.from(QJobOrder.jobOrder);
		this.mesPredicate = MesPredicate.getInstance();
		this.mesQuery = new MesQuery<JobOrder>(this.query, JobOrder.class);
	}

	public QueryJobOrder join(String parameter, String classRoot) {
		this.mesQuery.join(parameter, classRoot);
		return this;
	}

	public QueryJobOrder filter(String key, String operator, Object value) {
		int index = key.indexOf(".");
		if (index != -1) {
			String root = key.substring(0, index);
			index++;
			String path = key.substring(index);
			if (root.equals("parameter")) {
				this.buildParams(path, operator, value);
				return this;
			}
			if (root.equals("materialRequirement")) {
				this.buildMaterials(path, operator, value);
				return this;
			}
		}
		PathBuilder<JobOrder> entityPath = new PathBuilder<>(JobOrder.class, "jobOrder");
		this.query.where(mesPredicate.get(entityPath, key, operator, value));
		return this;
	}

	public QueryJobOrder page(Pageable pageable) {
		this.mesQuery.page(pageable);
		return this;
	}

	public List<JobOrder> run() {
		return this.mesQuery.run();
    }

	public long size() {
		return this.mesQuery.size();
	}

	private void buildParams(String properties, String operator, Object value) {
		QJobOrder order = QJobOrder.jobOrder;
		String[] keys = properties.split(Pattern.quote("."));
		String acumProperty = "";
		for (int i = 0; i < keys.length; i++) {
			PathBuilder<JobOrderParameter> paramPath = new PathBuilder<>(JobOrderParameter.class, keys[i]);
			BooleanExpression expressionOn = paramPath.getString("code").eq(keys[i]);
			if (i == 0) {
				this.query.innerJoin(order.parameters, paramPath);
			} else {
				PathBuilder<JobOrderParameter> prevPath = new PathBuilder<>(JobOrderParameter.class, keys[i-1]);
				this.query.innerJoin(prevPath.getCollection("parameters", JobOrderParameter.class), paramPath);
			}
			acumProperty += keys[i];
			PathBuilder<?> innerPath = this.mesQuery.getPath(acumProperty, expressionOn);
			acumProperty += ".";
			if (innerPath != null) {
				this.query.where(paramPath.getString("value")
				.eq(innerPath.getString("id"))
				.and(mesPredicate.get(innerPath, properties.replace(acumProperty, ""), operator, value)));
				return;
			}
			if (i == keys.length-1) {
				expressionOn = expressionOn.and(mesPredicate.get(paramPath, "value", operator, value));
			}
			this.query.on(expressionOn);
		}
	}

	private void buildMaterials(String key, String operator, Object value) {
		QJobOrder order = QJobOrder.jobOrder;
		PathBuilder<JobOrderMaterialRequirement> materialPath = new PathBuilder<>(JobOrderMaterialRequirement.class, "jobOrderMaterialRequirement");
		this.query.innerJoin(order.materialRequirements, materialPath).on(mesPredicate.get(materialPath, key, operator, value));
	}
}
