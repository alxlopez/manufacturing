package com.mes.query.translators;

import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Pageable;

import com.mes.dom.personnel.Person;
import com.mes.dom.personnel.PersonProperty;
import com.mes.dom.personnel.PersonnelClass;
import com.mes.dom.personnel.QPerson;
import com.mes.query.MesPredicate;
import com.mes.query.MesQuery;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

public class QueryPerson implements IQuery {
	private MesPredicate mesPredicate;
	private JPAQuery<Person> query;
	private MesQuery<Person> mesQuery;

	public QueryPerson(EntityManager em) {
		this.query = new JPAQuery<Person>(em);
		this.query.from(QPerson.person);
		this.mesPredicate = MesPredicate.getInstance();
		this.mesQuery = new MesQuery<>(this.query, Person.class);
	}

	public QueryPerson join(String parameter, String classRoot) {
		this.mesQuery.join(parameter, classRoot);
		return this;
	}

	public QueryPerson filter(String key, String operator, Object value) {
		int index = key.indexOf(".");
		if (index != -1) {
			String root = key.substring(0, index);
			index++;
			String path = key.substring(index);
			if (root.equals("property")) {
				this.buildProperties(path, operator, value);
				return this;
			}
			if (root.equals("class")) {
				this.buildClasses(path, operator, value);
				return this;
			}
		}
		PathBuilder<Person> entityPath = new PathBuilder<>(Person.class, "person");
		this.query.where(mesPredicate.get(entityPath, key, operator, value));
		return this;
	}

	public QueryPerson page(Pageable pageable) {
		this.mesQuery.page(pageable);
		return this;
	}

	public List<Person> run() {
		return this.mesQuery.run();
    }

	public long size() {
		return this.mesQuery.size();
	}

	private void buildProperties(String properties, String operator, Object value) {
		QPerson person = QPerson.person;
		String[] keys = properties.split(Pattern.quote("."));
		String acumProperty = "";
		for (int i = 0; i < keys.length; i++) {
			PathBuilder<PersonProperty> paramPath = new PathBuilder<>(PersonProperty.class, keys[i]);
			BooleanExpression expressionOn = paramPath.getString("code").eq(keys[i]);
			if (i == 0) {
				this.query.innerJoin(person.properties, paramPath);
			} else {
				PathBuilder<PersonProperty> prevPath = new PathBuilder<>(PersonProperty.class, keys[i-1]);
				this.query.innerJoin(prevPath.getCollection("properties", PersonProperty.class), paramPath);
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
		QPerson person = QPerson.person;
		PathBuilder<PersonnelClass> classesPath = new PathBuilder<>(PersonnelClass.class, "personnelClass");
		this.query.innerJoin(person.personnelClasses, classesPath).on(mesPredicate.get(classesPath, key, operator, value));
	}
}
