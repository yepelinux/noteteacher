package com.teracode.example.core.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class AbstractReporsitory<T extends Entity> implements IRepository<T> {

	private static int ids = 0;
	
	protected Map<Integer, T> objects;

	public AbstractReporsitory()
	{
		this.objects = new HashMap<Integer, T>();
	}
	
	public T get(int id)
	{
		if(!this.objects.containsKey(id))
			throw new ObjectNotFoundException();
			
		return this.objects.get(id);
	}
	
	public void add(T t)
	{
		t.setId(ids++);
		this.objects.put(t.getId(), t);
	}
	
	public void remove(T t)
	{
		this.objects.remove(t.getId());
	}
	
	public List<T> getAll()
	{
		return new ArrayList<T>(this.objects.values());
	}
}
