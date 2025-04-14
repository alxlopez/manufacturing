package com.mes.query.translators;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface IQuery {
	public IQuery join(String parameter, String entity);
	public IQuery filter(String key, String operation, Object value);
	public IQuery page(Pageable pageable);
	public List<?> run();
	public long size();
}
