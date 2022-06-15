/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.maven.shared.utils.io.FileUtils;

/**
 * FXML Controller class
 *
 * @author andrea
 */
public class FXMLAgregarRutaController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TableColumn tvRutas;
    @FXML
    private ComboBox<?> cbColonias;
    @FXML
    private TextArea taRecorrido;
    @FXML
    private TableView<?> tvColonias;
    
    private String token;
    
    private Path to;
    
    private Path from;
    
    private File selectedFile;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setAccessToken(String token) {
        this.token = token;
    }

    @FXML
    private void clicCerrar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) tfNombre.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clicAgregar(ActionEvent event) {
    }

    @FXML
    private void clicAgregarColonia(ActionEvent event) {
    }

    @FXML
    private void clicSeleccionarImagen(ActionEvent event) throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Attach a file");
        selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            from = Paths.get(selectedFile.toURI());
            to = Paths.get("/Users/carlosperezperez/Library/CloudStorage/OneDrive-UniversidadVeracruzana/UV/Sexto Periodo/Desarrollo de Sistemas en Red/Proyecto/Cliente APISUX/src/img/" + "imagen copiada.png");
            Files.copy(from, to);
        }
    }
}
