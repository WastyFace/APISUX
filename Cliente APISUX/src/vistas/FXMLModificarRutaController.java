/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author andrea
 */
public class FXMLModificarRutaController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TableView<?> tvColonias;
    @FXML
    private TableColumn<?, ?> tvRutas;
    @FXML
    private ComboBox<?> cbColonias;
    @FXML
    private TextArea taRecorrido;
    @FXML
    private ComboBox<?> cbRutas;
    
    private String token;

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
    private void clicGuardar(ActionEvent event) {
    }

    @FXML
    private void clicAgregarColonia(ActionEvent event) {
    }

    @FXML
    private void clicSeleccionarImagen(ActionEvent event) {
    }

    @FXML
    private void clicEliminarColonia(ActionEvent event) {
    }
    
}
