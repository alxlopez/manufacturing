package com.mes.query;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mes.config.exception.ManufacturingIllegalArgumentException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

public class MesQuery<T> {
	private HashMap<String, PathBuilder<?>> instances;
	private HashMap<String, String> classes;
	private JPAQuery<T> query;
	private Class<T> typeParameterClass;

	public MesQuery(JPAQuery<T> query, Class<T> typeParameterClass) {
		this.instances = new HashMap<>();
		this.classes = new HashMap<>();
		this.query = query;
		this.typeParameterClass = typeParameterClass;
	}

	public void join(String parameter, String classRoot) {
		this.classes.put(parameter, classRoot);
	}
	
	public void page(Pageable pageable) {
		this.query.offset(pageable.getOffset());
        this.query.limit(pageable.getPageSize());
        Sort sort = pageable.getSort();
        if (sort != null) {
        	String name = this.typeParameterClass.getSimpleName();
        	name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
        	PathBuilder<T> entityPath = new PathBuilder<>(this.typeParameterClass, name);
        	for (Sort.Order order : sort) {
        		Order ord = Order.valueOf(order.getDirection().name());
        		ComparablePath<String> path = entityPath.getComparable(order.getProperty(), String.class);
                this.query.orderBy(new OrderSpecifier<>(ord, path));
            }
        }
	}
	
	public List<T> run() {
		try {
			return this.query.fetch();
		} catch (java.lang.IllegalArgumentException e) {
			throw new ManufacturingIllegalArgumentException(e.getMessage());
		}
	}
	
	public long size() {
		try {
			return this.query.fetchCount();
		} catch (java.lang.IllegalArgumentException e) {
			throw new ManufacturingIllegalArgumentException(e.getMessage());
		}
	}

	public PathBuilder<?> getPath(String property, BooleanExpression expressionOn) {
		if (!this.instances.containsKey(property)) {
			if(!this.classes.containsKey(property)) {
				return null;
			}
			String classRoot = this.classes.get(property);
			int index = classRoot.lastIndexOf(".") + 1;
			String className = classRoot.substring(index);
			String objectName = Character.toLowerCase(className.charAt(0)) + className.substring(1);
			try {
				Class<?> entity = Class.forName(classRoot);
				PathBuilder<?> entityPath = new PathBuilder<>(entity, objectName);
				this.instances.put(property, entityPath);
				this.query.on(expressionOn).from(entityPath);
			} catch (ClassNotFoundException e) {
				throw new ManufacturingIllegalArgumentException("The entity " + e.getMessage() + " does not exist");
			}
		}
		return this.instances.get(property);
	}
}
