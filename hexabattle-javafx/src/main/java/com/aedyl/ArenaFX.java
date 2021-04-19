package com.aedyl;

import com.aedyl.domain.Arena;
import com.aedyl.domain.Fighter;
import com.aedyl.service.ArenaService;
import com.aedyl.view.CharacteristicsBarChart;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArenaFX extends Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(ArenaFX.class);
	private Arena arena;
	ObservableList<Fighter> fighters = FXCollections.observableArrayList();
	CharacteristicsBarChart characteristicsBarChart;
	private ScheduledService<Void> refreshSurvivors;

	@Override
	public void start(Stage stage) {

		ArenaService arenaService = Feign.builder()
				.encoder(new JacksonEncoder())
				.decoder(new JacksonDecoder())
				.logger(new feign.Logger.ErrorLogger())
				.logLevel(feign.Logger.Level.FULL)
				.target(ArenaService.class, "http://localhost:7003");

		refreshSurvivors = new ScheduledService<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						arena.setSurvivors(arenaService.getSurvivors(arena.getId()));
						Platform.runLater(() -> fighters.setAll(arena.getSurvivors()));
						return null;
					}
				};
			}
		};
		refreshSurvivors.setPeriod(Duration.millis(500));

		FlowPane flowTop = new FlowPane();
		Label l = new Label("No arena");
		Button startGame = new Button("Start game");
		startGame.setOnAction(actionEvent -> {
			LOGGER.info("Start clicked!");

			arena = arenaService.createNewArena();
			l.setText("Current Arena: " + arena.getId() + ", status: " + arena.getStatus() + ".");
			fighters.setAll(arena.getSurvivors());
			arenaService.run(arena.getId());
			if (refreshSurvivors.isRunning()){
				refreshSurvivors.cancel();
			}
			if (refreshSurvivors.getState().equals(Worker.State.CANCELLED)){
				refreshSurvivors.reset();
			}
			refreshSurvivors.start();
		});

		flowTop.getChildren().add(startGame);
		flowTop.getChildren().add(l);
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(flowTop);

		ListView<Fighter> fighterList = new ListView<>();
		fighterList.setItems(fighters);
		fighterList.getSelectionModel().selectedItemProperty().addListener(observable -> {
			final Fighter selectedItem = fighterList.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				LOGGER.info("Selected: {}", selectedItem.getName());
				characteristicsBarChart.display(selectedItem);
			}
		});
		borderPane.setLeft(fighterList);

		characteristicsBarChart = new CharacteristicsBarChart();
		borderPane.setCenter(characteristicsBarChart.getView());

		Scene scene = new Scene(borderPane, 800, 600);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}

}
