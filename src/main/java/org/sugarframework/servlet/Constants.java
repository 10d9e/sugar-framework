package org.sugarframework.servlet;

import java.util.Date;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;

class Constants {

	public static final ConvertUtilsBean CONVERTER = new ConvertUtilsBean();
	static {
		DateTimeConverter dtConverter = new DateConverter();
		dtConverter.setPattern("MM/dd/yyyy");		
		CONVERTER.register(dtConverter, Date.class);
	}
}
