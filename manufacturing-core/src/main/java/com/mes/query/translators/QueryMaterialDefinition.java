package com.mes.query.translators;

import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Pageable;

import com.mes.dom.material.MaterialClass;
import com.mes.dom.material.MaterialDefinition;
import com.mes.dom.material.MaterialDefinitionProperty;
import com.mes.dom.material.QMaterialDefinition;
import com.mes.query.MesPredicate;
import com.mes.query.MesQuery;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

public class QueryMaterialDefinition implements IQuery {
	private MesPredicate mesPredicate;
	private MesQuery<MaterialDefinition> mesQuery;
	private JPAQuery<MaterialDefinition> query;

	public QueryMaterialDefinition(EntityManager em) {
		this.query = new JPAQuery<MaterialDefinition>(em);
		this.query.from(QMaterialDefinition.materialDefinition);
		this.mesPredicate = MesPredicate.getInstance();
		this.mesQuery = new MesQuery<MaterialDefinition>(this.query, MaterialDefinition.class);
	}

	public QueryMaterialDefinition join(String parameter, String classRoot) {
		this.mesQuery.join(parameter, classRoot);
		return this;
	}

	public QueryMaterialDefinition filter(String key, String operator, Object value) {
		int index = key.indexOf(".");
		if (index != -1) {
			String root = key.substring(0, index);
			index++;
			String path = key.substring(index);
			if (root.equals("property")) {
				this.buildProperties(path, operator, value);
				return this;
			} else if (root.equals("class")) {
				this.buildClasses(path, operator, value);
				return this;
			}
		}
		PathBuilder<MaterialDefinition> entityPath = new PathBuilder<>(MaterialDefinition.class, "materialDefinition");
		this.query.where(mesPredicate.get(entityPath, key, operator, value));
		return this;
	}

	public QueryMaterialDefinition page(Pageable pageable) {
		this.mesQuery.page(pageable);
		return this;
	}

	public List<MaterialDefinition> run() {
		return this.mesQuery.run();
    }

	public long size() {
		return this.mesQuery.size();
	}

	private void buildProperties(String properties, String operator, Object value) {
		QMaterialDefinition material = QMaterialDefinition.materialDefinition;
		String[] keys = properties.split(Pattern.quote("."));
		String acumProperty = "";
		for (int i = 0; i < keys.length; i++) {
			PathBuilder<MaterialDefinitionProperty> paramPath = new PathBuilder<>(MaterialDefinitionProperty.class, keys[i]);
			BooleanExpression expressionOn = paramPath.getString("code").eq(keys[i]);
			if (i == 0) {
				this.query.innerJoin(material.properties, paramPath);
			} else {
				PathBuilder<MaterialDefinitionProperty> prevPath = new PathBuilder<>(MaterialDefinitionProperty.class, keys[i-1]);
				this.query.innerJoin(prevPath.getCollection("properties", MaterialDefinitionProperty.class), paramPath);
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

	private void buildClasses(String key, String operator, Object value) {
		QMaterialDefinition material = QMaterialDefinition.materialDefinition;
		PathBuilder<MaterialClass> materialClassPath = new PathBuilder<>(MaterialClass.class, "materialClass");
		this.query.innerJoin(material.materialClasses, materialClassPath).on(mesPredicate.get(materialClassPath, key, operator, value));
	}
}
