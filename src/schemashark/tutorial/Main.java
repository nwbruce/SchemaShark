package schemashark.tutorial;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	Stage window;
	Button button;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage w) throws Exception {
		window = w;
		window.setTitle("SchemaShark");
		window.setOnCloseRequest(e -> {
			e.consume();
			// conditionally...
			window.close();
		});
		
		button = new Button("Click me");
		button.setOnAction(e -> AlertBox.display("SchemaShark", "The button was clicked"));
		
		StackPane layout = new StackPane();
		layout.getChildren().add(button);
		
		Scene panel = new Scene(layout, 300, 250);
		
		window.setScene(panel);
		window.show();
	}

}
