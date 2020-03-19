package com.pnc.assignment.aggregator.actuator;

import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Endpoint(id = "zeyaul", enableByDefault = true)
public class CustomActuatorEndpoint {

	private Map<String, String> features = new ConcurrentHashMap<>();

	@ReadOperation
	public Map<String, String> features() {
		return features;
	}

	@ReadOperation
	public String feature(@Selector String name) {
		return features.get(name);
	}

	@WriteOperation
	public void configureFeature(@Selector String name, String value) {
		features.put(name, value);
	}

	@DeleteOperation
	public void deleteFeature(@Selector String name) {
		features.remove(name);
	}
}
