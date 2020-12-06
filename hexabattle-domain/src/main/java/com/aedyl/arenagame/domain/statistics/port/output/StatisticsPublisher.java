package com.aedyl.arenagame.domain.statistics.port.output;

public interface StatisticsPublisher {
	void publish(StatisticsEvent statisticsEvent);
}
