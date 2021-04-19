package com.aedyl.view;

import com.aedyl.domain.Characteristics;
import com.aedyl.domain.Fighter;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class CharacteristicsBarChart {

	final BarChart<String, Number> bc;

	public CharacteristicsBarChart() {
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		bc = new BarChart<>(xAxis, yAxis);
		bc.setTitle("Characteristics Summary");
		xAxis.setLabel("Characteristic");
		yAxis.setLabel("Value");
	}

	public Node getView() {
		return bc;
	}

	public void display(Fighter fighter) {
		final Characteristics characteristics = fighter.getCharacteristics();
		XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		series1.setName(String.format("%s [%s/%s]", fighter.getName(), characteristics.primary(), characteristics.secondary()));
		series1.getData().add(new XYChart.Data<>("Life", characteristics.life()));
		series1.getData().add(new XYChart.Data<>("Max Life", characteristics.maxLife()));
		series1.getData().add(new XYChart.Data<>("Initiative", characteristics.initiative()));
		series1.getData().add(new XYChart.Data<>("Strength", characteristics.strength()));
		bc.getData().setAll(series1);
	}
}
