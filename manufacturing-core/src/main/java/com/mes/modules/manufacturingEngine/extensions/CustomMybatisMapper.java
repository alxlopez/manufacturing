package com.mes.modules.manufacturingEngine.extensions;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

public interface CustomMybatisMapper {
	@Select({
		"SELECT task.ID_ as taskId, variable.LONG_ as variableValue FROM ACT_RU_VARIABLE variable",
		"inner join ACT_RU_TASK task on variable.TASK_ID_ = task.ID_",
		"where variable.NAME_ = #{variableName}"
	})
	List<Map<String, Object>> selectTaskWithSpecificVariable(String variableName);
}