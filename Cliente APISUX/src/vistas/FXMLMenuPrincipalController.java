/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author andrea
 */
public class FXMLMenuPrincipalController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicRutas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLRutas.fxml"));
            Parent root = loader.load();
            FXMLRutasController controladorForm = loader.getController();
            Scene escenaFormulario = new Scene(root);
            Stage escenario = new Stage();
            escenario.setScene(escenaFormulario);
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLRutasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clicRutasColonia(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLRutasColonia.fxml"));
            Parent root = loader.load();
            FXMLRutasColoniaController controladorForm = loader.getController();
            Scene escenaFormulario = new Scene(root);
            Stage escenario = new Stage();
            escenario.setScene(escenaFormulario);
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLRutasColoniaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clicConsultaTuRuta(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLConsultaTuRuta.fxml"));
            Parent root = loader.load();
            FXMLConsultaTuRutaController controladorForm = loader.getController();
            Scene escenaFormulario = new Scene(root);
            Stage escenario = new Stage();
            escenario.setScene(escenaFormulario);
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLConsultaTuRutaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    private void clicIniciarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLIniciarSesion.fxml"));
            Parent root = loader.load();
            FXMLIniciarSesionController controladorForm = loader.getController();
            Scene escenaFormulario = new Scene(root);
            Stage escenario = new Stage();
            escenario.setScene(escenaFormulario);
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLIniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
