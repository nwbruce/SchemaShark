package schemashark.tutorial;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	Stage window;
	TextArea textArea;
	String loadedDocumentName;
	boolean documentIsDirty;

	public static void main(String[] args) {
		launch(args);
	}
	
	private MenuBar buildMenuBar() {
		/* File Menu */
		Menu fileMenu = new Menu("_File");
		
		MenuItem fileNewMI = new MenuItem("_New");
		fileNewMI.setAccelerator(KeyCombination.keyCombination("Shortcut+N"));
		fileNewMI.setOnAction(e -> textArea.clear());
		fileMenu.getItems().add(fileNewMI);
		
		MenuItem fileOpenMI = new MenuItem("_Open...");
		fileOpenMI.setAccelerator(KeyCombination.keyCombination("Shortcut+O"));
		fileMenu.getItems().add(fileOpenMI);
		
		MenuItem fileSaveMI = new MenuItem("_Save");
		fileSaveMI.setAccelerator(KeyCombination.keyCombination("Shortcut+S"));
		fileMenu.getItems().add(fileSaveMI);
		
		MenuItem fileSaveAsMI = new MenuItem("Save _As...");
		fileMenu.getItems().add(fileSaveAsMI);
		
		fileMenu.getItems().add(new SeparatorMenuItem());
		
		MenuItem fileExitMI = new MenuItem("E_xit");
		fileExitMI.setOnAction(e -> tryClose());
		fileMenu.getItems().add(fileExitMI);

		/* Edit Menu */
		Menu editMenu = new Menu("_Edit");
		
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, editMenu);
		return menuBar;
	}
	
	private void updateWindowTitle() {
		StringBuilder sb = new StringBuilder();
		if (documentIsDirty) {
			sb.append('*');
		}
		sb.append(loadedDocumentName);
		sb.append(" - Schema Shark");
		window.setTitle(sb.toString());
	}
	
	private void tryClose() {
		if (documentIsDirty) {
			int result = AlertBox.display("SchemaShark", "Do you want to save changes to " + loadedDocumentName + "?", "Save", "Don't Save", "Cancel");
			if (result == 0 || result == 3) {
				// cancel
			} else {
				window.close();
			}
		} else {
			window.close();
		}
	}

	@Override
	public void start(Stage w) throws Exception {
		window = w;
		window.setOnCloseRequest(e -> {
			e.consume();
			tryClose();
		});
		
		documentIsDirty = false;
		loadedDocumentName = "Untitled";
		updateWindowTitle();
		
		MenuBar menuBar = buildMenuBar();
		
		textArea = new TextArea();
		textArea.setOnKeyTyped(e -> {
			documentIsDirty = true;
			updateWindowTitle();
		});
		
		BorderPane layout = new BorderPane();
		layout.setTop(menuBar);
		layout.setCenter(textArea);
		
		Scene panel = new Scene(layout, 600, 500);
		
		window.setScene(panel);
		window.show();
	}

}
