package org.sugarframework.context;

import java.util.Date;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class DefaultContextInitializer {

	private static Log log = LogFactory.getLog(DefaultContextInitializer.class);

	private static DefaultContext context;
	
	public static final ConvertUtilsBean CONVERTER = new ConvertUtilsBean();
	
	public static final ConvertUtilsBean ERROR_THROWING_CONVERTER = new ConvertUtilsBean();

	
	static {
		CONVERTER.deregister(Date.class);
		DateTimeConverter dtConverter = new DateConverter(null);
		dtConverter.setPattern("MM/dd/yyyy");		
		CONVERTER.register(dtConverter, Date.class);
		CONVERTER.register(false, true, 0);
		
		ERROR_THROWING_CONVERTER.deregister(Date.class);
		ERROR_THROWING_CONVERTER.register(dtConverter, Date.class);
		ERROR_THROWING_CONVERTER.register(true, true, 0);

	}
	


	public static DefaultContext getContext() {
		return context;
	}

	public static void startContext(Class<?> contextClass) {
		context = new DefaultContext(contextClass);
		log.info("Context started.");
	}

}
