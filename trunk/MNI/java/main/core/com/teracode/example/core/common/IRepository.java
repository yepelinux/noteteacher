package com.teracode.example.core.common;

import java.util.List;

public interface IRepository<T extends Entity> {

	public T get(int id);
	
	public void add(T t);
	
	public void remove(T t);
	
	public List<T> getAll();
}