package schemashark;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
	
	Stage window;
	TextArea textArea;
	Document document;

	public static void main(String[] args) {
		launch(args);
	}
	
	private void fileOpen() {
		if (!tryUnloadDocument()) {
			return;
		}
		FileChooser dialog = new FileChooser();
		dialog.setTitle("Open");
		dialog.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("XML Schemas", "*.xsd"),
				new FileChooser.ExtensionFilter("All Files", "*")
		);
		File f = dialog.showOpenDialog(window);
		if (f != null) {
			System.out.println(f.getName());
		}
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
		fileOpenMI.setOnAction(e -> fileOpen());
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
		if (document.isDirty()) {
			sb.append('*');
		}
		sb.append(document.getShortName());
		sb.append(" - Schema Shark");
		window.setTitle(sb.toString());
	}
	
	private boolean tryUnloadDocument() {
		if (document.isDirty()) {
			int result = AlertBox.display("SchemaShark", "Do you want to save changes to " + document.getShortName() + "?", "Save", "Don't Save", "Cancel");
			if (result == 1) {
				// save it
				return true;
			} else if (result == 2) {
				// don't save it
				return true;
			} else {
				// cancel
				return false;
			}
		} else {
			return true;
		}
	}
	
	private void tryClose() {
		if (tryUnloadDocument()) {
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
		
		document = new Document();
		
		updateWindowTitle();
		
		MenuBar menuBar = buildMenuBar();
		
		textArea = new TextArea();
		textArea.setOnKeyTyped(e -> {
			document.setDirty();
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
