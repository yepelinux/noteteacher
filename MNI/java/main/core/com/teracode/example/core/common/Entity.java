package com.teracode.example.core.common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Entity implements Serializable {

	private static int instances = 0;
	
	private int id = instances++;

	public int getId() {
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
}
