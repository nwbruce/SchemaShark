package schemashark.tutorial;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AlertBox {
	private int result = 0;
	private Stage window;
	private AlertBox(String title, String message, String... buttons) {
		window = new Stage();
		
		// set modal
		window.initModality(Modality.APPLICATION_MODAL);
		window.initStyle(StageStyle.UTILITY);
		window.setResizable(false);
		window.setTitle(title);
		
		Label label = new Label();
		label.setText(message);
		label.setWrapText(false);
		
		HBox buttonLayout = new HBox(10);
		buttonLayout.setAlignment(Pos.CENTER);

		int counter = 0;
		for (String lbl : buttons) {
			final int index = ++counter;
			Button b = new Button(lbl);
			b.setOnAction(e -> {
				result = index;
				window.close();
			});
			buttonLayout.getChildren().add(b);
		}
				
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, buttonLayout);
		layout.setAlignment(Pos.CENTER);
		
		layout.setPadding(new Insets(10));
				
		
		Scene panel = new Scene(layout);
		window.setScene(panel);
		
		
	}
	
	private int show() {
		window.showAndWait();
		return result;
	}
	
	public static int display(String title, String message, String... buttons) {
		AlertBox ab = new AlertBox(title, message, buttons);
		return ab.show();
	}
}
