/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import pojo.Colonia;

/**
 * FXML Controller class
 *
 * @author andrea
 */
public class FXMLConsultaTuRutaController implements Initializable {

    @FXML
    private ComboBox<Colonia> cbColonias;
    @FXML
    private ComboBox<Colonia> cbColonias2;
    @FXML
    private ImageView ivImagenRuta;
    @FXML
    private TextArea taRecorrido;
    @FXML
    private TextArea taColonias;
    @FXML
    private TextField tfRuta;
    
    private ObservableList<Colonia> colonias;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colonias = FXCollections.observableArrayList();
        try {
            // TODO
            cargarColonias();
        } catch (IOException ex) {
            Logger.getLogger(FXMLConsultaTuRutaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void cargarColonias() throws IOException {
        try {
            //TODO
            for (int i = 0;i<77;i++) {
                String n = Integer.toString(i+1);
                String urlArm = "http://127.0.0.1:9090/colonias/" + n;
                URL url = new URL(urlArm);
                URLConnection conexion = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String inputLine = in.readLine();
                ObjectMapper mapper = new ObjectMapper();
                Colonia colonia = mapper.readValue(inputLine, Colonia.class);
                colonias.add(colonia);
            }
            cbColonias.setItems(colonias);
            cbColonias2.setItems(colonias);
        } catch (MalformedURLException ex) {
            Logger.getLogger(FXMLMenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void clicCerrar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) cbColonias.getScene().getWindow();
        stage.close();
    } 

    @FXML
    private void clicConsultar(ActionEvent event) {
    }
}
