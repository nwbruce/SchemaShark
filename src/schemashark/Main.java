package schemashark;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
	
	private void fileNew() {
		if (!tryUnloadDocument()) {
			return;
		}
		document = new Document();
		resetTextAreaToDefault();
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
			document.setFile(f);
			try {
				textArea.setText(document.read());
				updateWindowTitle();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void fileSaveAs() {
		FileChooser dialog = new FileChooser();
		dialog.setTitle("Save As");
		dialog.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("XML Schemas", "*.xsd"),
				new FileChooser.ExtensionFilter("All Files", "*")
		);
		File f = dialog.showSaveDialog(window);
		if (f != null) {
			document.setFile(f);
			try {
				document.write(textArea.getText());
				updateWindowTitle();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void fileSave() {
		if (document.hasFileName()) {
			try {
				document.write(textArea.getText());
				updateWindowTitle();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// save as...
			fileSaveAs();
		}
	}
	
	private MenuBar buildMenuBar() {
		/* File Menu */
		Menu fileMenu = new Menu("_File");
		
		MenuItem fileNewMI = new MenuItem("_New");
		fileNewMI.setAccelerator(KeyCombination.keyCombination("Shortcut+N"));
		fileNewMI.setOnAction(e -> fileNew());
		fileMenu.getItems().add(fileNewMI);
		
		MenuItem fileOpenMI = new MenuItem("_Open...");
		fileOpenMI.setAccelerator(KeyCombination.keyCombination("Shortcut+O"));
		fileOpenMI.setOnAction(e -> fileOpen());
		fileMenu.getItems().add(fileOpenMI);
		
		MenuItem fileSaveMI = new MenuItem("_Save");
		fileSaveMI.setAccelerator(KeyCombination.keyCombination("Shortcut+S"));
		fileSaveMI.setOnAction(e -> fileSave());
		fileMenu.getItems().add(fileSaveMI);
		
		MenuItem fileSaveAsMI = new MenuItem("Save _As...");
		fileSaveAsMI.setOnAction(e -> fileSaveAs());
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
				fileSave();
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
	
	private void resetTextAreaToDefault() {
		String[] pair = document.getEmptyXsd();
		textArea.setText(pair[0]);
		textArea.appendText(pair[1]);
		textArea.requestFocus();
		textArea.selectRange(pair[0].length(), pair[0].length());
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
		textArea.setStyle("-fx-font-family: Consolas;");
		resetTextAreaToDefault();
		
		Tab tab1 = new Tab();
		tab1.setText("Source");
		tab1.setContent(textArea);
		tab1.setClosable(false);

		Tab tab2 = new Tab();
		tab2.setText("Edit");
		tab2.setClosable(false);
		
		Tab tab3 = new Tab();
		tab3.setText("Browse");
		tab3.setClosable(false);
		
		TabPane tabPane = new TabPane();
		tabPane.setSide(Side.LEFT);
		tabPane.getTabs().add(tab1);
		tabPane.getTabs().add(tab2);
		tabPane.getTabs().add(tab3);
		
		
		BorderPane layout = new BorderPane();
		layout.setTop(menuBar);
		layout.setCenter(tabPane);
		
		Scene panel = new Scene(layout, 600, 500);
		
		window.setScene(panel);
		window.show();
	}

}
