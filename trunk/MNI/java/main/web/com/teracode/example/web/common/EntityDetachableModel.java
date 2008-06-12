package com.teracode.example.web.common;

import wicket.Component;
import wicket.model.AbstractReadOnlyDetachableModel;
import wicket.model.IModel;

import com.teracode.example.core.common.Entity;

@SuppressWarnings("serial")
public class EntityDetachableModel extends AbstractReadOnlyDetachableModel {

	private Integer id;
	private transient Entity entity;
	
	public EntityDetachableModel(Entity entity)
	{
		super();
		this.id = entity.getId();
		this.entity = entity;
	}

	public Integer getId()
	{
		return this.id;
	}
	
	@Override
	public IModel getNestedModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onAttach() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDetach() {
		this.entity = null;
		
	}

	@Override
	protected Object onGetObject(Component arg0) {
		// TODO Auto-generated method stub
		return this.entity;
	}
	

}
