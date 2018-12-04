package com.boe.esl.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class VOConverter {

	public <T> T execute(VOCallback<T> voCallback) {
		return voCallback.convert();
	}
	
	public <T,V> T defaultExecute(final T t, final V v) {
		return this.execute(new VOCallback<T>() {

			@Override
			public T convert() {
				if(v==null) return null;
                BeanUtils.copyProperties(v,t);
                return t;
			}
		});
	}
}
