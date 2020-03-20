package com.nexware.aajapan.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

@Configuration
public class BeanConfig {

	@Bean
	public TilesConfigurer tilesConfigurer() {
		final TilesConfigurer configurer = new TilesConfigurer();
		configurer.setDefinitions("WEB-INF/views/tiles.xml", "WEB-INF/views/shipping/tiles.xml",
				"WEB-INF/views/shipping/master/tiles.xml", "WEB-INF/views/sales/tiles.xml",
				"WEB-INF/views/sales/invoice/tiles.xml", "WEB-INF/views/accounts/tiles.xml",
				"WEB-INF/views/accounts/lc/tiles.xml", "WEB-INF/views/accounts/claim/tiles.xml",
				"WEB-INF/views/accounts/payments/tiles.xml", "WEB-INF/views/accounts/reports/tiles.xml",
				"WEB-INF/views/documents/tiles.xml", "WEB-INF/views/documents/recycle/tiles.xml",
				"WEB-INF/views/accounts/daybook/tiles.xml", "WEB-INF/views/accounts/master/tiles.xml",
				"/WEB-INF/views/accounts/dashboard/tiles.xml", "/WEB-INF/views/shipping/shipping_management/tiles.xml",
				"/WEB-INF/views/accounts/invoice-booking/tiles.xml",
				"/WEB-INF/views/accounts/invoice-approval/tiles.xml", "WEB-INF/views/accounts/parts-purchase/tiles.xml",
				"/WEB-INF/views/shipping/document_tracking/tiles.xml",
				"WEB-INF/views/accounts/bl-manaagement/tiles.xml", "WEB-INF/views/sales/bl-manaagement/tiles.xml");
		configurer.setCheckRefresh(true);
		return configurer;
	}

	@Bean
	public TilesViewResolver tilesViewResolver() {
		final TilesViewResolver resolver = new TilesViewResolver();
		resolver.setViewClass(TilesView.class);
		return resolver;
	}

	@Bean
	MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
		return new MongoTransactionManager(dbFactory);
	}

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
