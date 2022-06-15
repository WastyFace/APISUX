package vistas;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import pojo.Colonia;
import pojo.Ruta;

/**
 * FXML Controller class
 *
 * @author andrea
 */
public class FXMLEliminarRutaController implements Initializable {

    @FXML
    private ComboBox<Ruta> cbRutas;
    @FXML
    private TextArea taRecorrido;
    @FXML
    private ImageView ivImagenRuta;
    @FXML
    private TableView<Colonia> tvColonias;
    
    private String token;
    @FXML
    private TableColumn tcColonias;

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
        Stage stage = (Stage) taRecorrido.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clicEliminar(ActionEvent event) {
    }
    
}
