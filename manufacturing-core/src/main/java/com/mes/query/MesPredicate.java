package com.mes.query;

import com.mes.config.exception.ManufacturingIllegalArgumentException;
import com.mes.dom.enumerations.application.MaterialUse;
import com.mes.dom.enumerations.application.UnitOfMeasure;
import com.mes.dom.enumerations.application.WorkTypes;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;

public class MesPredicate {
	private static MesPredicate mesQueryPredicate;
	
	private MesPredicate() { }
	
	public static MesPredicate getInstance() {
		if (mesQueryPredicate == null) {
			mesQueryPredicate = new MesPredicate();
		}
		return mesQueryPredicate;
	}
	
	public BooleanExpression get(PathBuilder<?> entityPath, String key, String operator, Object value) {
		if (value == null) {
			return operator.equals("equals")?
					entityPath.get(key).isNull():
					entityPath.get(key).isNotNull();
		}
		String strValue = value.toString();
		if (value instanceof String) {
			BooleanExpression expression = this.getEnum(entityPath, key, strValue);
			if (expression != null) return expression;
			expression = this.getString(entityPath, key, operator, strValue);
        	if (expression != null) return expression;
        	throw new ManufacturingIllegalArgumentException(operator + " operation unsupported for " + key + " [" + value + "]");
        }
    	return this.getNumber(entityPath, key, operator, Integer.parseInt(strValue));
    }

	private BooleanExpression getEnum(PathBuilder<?> entityPath, String key,  String value) {
		if(key.equals("workType")) {
			EnumPath<WorkTypes> path = entityPath.getEnum(key, WorkTypes.class);
			return path.eq(WorkTypes.valueOf(value));
		}
		if (key.equals("materialUse")) {
			EnumPath<MaterialUse> path = entityPath.getEnum(key, MaterialUse.class);
			return path.eq(MaterialUse.valueOf(value));
		}
		if (key.equals("unitOfMeasure")) {
			EnumPath<UnitOfMeasure> path = entityPath.getEnum(key, UnitOfMeasure.class);
			return path.eq(UnitOfMeasure.valueOf(value));
		}
		return null;
	}

	private BooleanExpression getString(PathBuilder<?> entityPath, String key, String operator, String value) {
		StringPath path = entityPath.getString(key);
    	switch (operator) {
            case "equals":
				return path.eq(value);
            case "notEquals":
            	return path.ne(value);
			case "lessThan":
				return path.lt(value);
			case "greaterThan":
				return path.gt(value);
			case "like":
				return path.like(value);
			case "notLike":
				return path.notLike(value);
        }
		return null;
	}

	private BooleanExpression getNumber(PathBuilder<?> entityPath, String key, String operator, int value) {
		NumberPath<Integer> path = entityPath.getNumber(key, Integer.class);
        switch (operator) {
            case "equals":
				return path.eq(value);
            case "notEquals":
            	return path.ne(value);
			case "lessThan":
				return path.lt(value);
			case "greaterThan":
				return path.gt(value);
        }
        throw new ManufacturingIllegalArgumentException(operator + " operation unsupported for " + key + " [" + value + "]");
	}
}
