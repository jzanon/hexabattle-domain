module com.aedyl {
    requires javafx.controls;
	requires org.slf4j;
	requires feign.core;
	requires feign.jackson;
	exports com.aedyl;
	exports com.aedyl.view;
	exports com.aedyl.domain;
	opens com.aedyl.domain;
	exports com.aedyl.service;
}
