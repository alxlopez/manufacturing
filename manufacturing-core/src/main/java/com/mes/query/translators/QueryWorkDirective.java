package com.mes.query.translators;

import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Pageable;

import com.mes.dom.workDirective.QWorkDirective;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.dom.workDirective.WorkDirectiveMaterialSpecification;
import com.mes.dom.workDirective.WorkDirectiveParameter;
import com.mes.query.MesPredicate;
import com.mes.query.MesQuery;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

public class QueryWorkDirective implements IQuery {
	private MesPredicate mesPredicate;
	private MesQuery<WorkDirective> mesQuery;
	private JPAQuery<WorkDirective> query;

	public QueryWorkDirective(EntityManager em) {
		this.query = new JPAQuery<WorkDirective>(em);
		this.query.from(QWorkDirective.workDirective);
		this.mesPredicate = MesPredicate.getInstance();
		this.mesQuery = new MesQuery<WorkDirective>(this.query, WorkDirective.class);
	}

	public QueryWorkDirective join(String parameter, String classRoot) {
		this.mesQuery.join(parameter, classRoot);
		return this;
	}

	public QueryWorkDirective filter(String key, String operator, Object value) {
		int index = key.indexOf(".");
		if (index != -1) {
			String root = key.substring(0, index);
			index++;
			String path = key.substring(index);
			if (root.equals("parameter")) {
				this.buildParams(path, operator, value);
				return this;
			}
			if (root.equals("materialSpecification")) {
				this.buildMaterials(path, operator, value);
				return this;
			}
		}
		PathBuilder<WorkDirective> entityPath = new PathBuilder<>(WorkDirective.class, "workDirective");
		this.query.where(mesPredicate.get(entityPath, key, operator, value));
		return this;
	}

	public QueryWorkDirective page(Pageable pageable) {
		this.mesQuery.page(pageable);
		return this;
	}

	public List<WorkDirective> run() {
		return this.mesQuery.run();
    }

	public long size() {
		return this.mesQuery.size();
	}

	private void buildParams(String properties, String operator, Object value) {
		QWorkDirective work = QWorkDirective.workDirective;
		String[] keys = properties.split(Pattern.quote("."));
		String acumProperty = "";
		for (int i = 0; i < keys.length; i++) {
			PathBuilder<WorkDirectiveParameter> paramPath = new PathBuilder<>(WorkDirectiveParameter.class, keys[i]);
			BooleanExpression expressionOn = paramPath.getString("code").eq(keys[i]);
			if (i == 0) {
				this.query.innerJoin(work.parameters, paramPath);
			} else {
				PathBuilder<WorkDirectiveParameter> prevPath = new PathBuilder<>(WorkDirectiveParameter.class, keys[i-1]);
				this.query.innerJoin(prevPath.getCollection("parameters", WorkDirectiveParameter.class), paramPath);
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
		QWorkDirective work = QWorkDirective.workDirective;
		PathBuilder<WorkDirectiveMaterialSpecification> materialPath = new PathBuilder<>(WorkDirectiveMaterialSpecification.class, "workDirectiveMaterialSpecification");
		this.query.innerJoin(work.materialSpecifications, materialPath).on(mesPredicate.get(materialPath, key, operator, value));
	}
}
