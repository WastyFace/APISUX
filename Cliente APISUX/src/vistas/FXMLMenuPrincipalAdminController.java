/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import com.google.gson.Gson;
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
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.protocol.HttpContext;
import pojo.Token;

/**
 * FXML Controller class
 *
 * @author andrea
 */
public class FXMLMenuPrincipalAdminController implements Initializable {

    @FXML
    private Label lbTitulo;
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
    private void clicAgregarRuta(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLAgregarRuta.fxml"));
            Parent root = loader.load();
            FXMLAgregarRutaController controladorForm = loader.getController();
            controladorForm.setAccessToken(token);
            Scene escenaFormulario = new Scene(root);
            Stage escenario = new Stage();
            escenario.setScene(escenaFormulario);
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLAgregarRutaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clicAgregarColonia(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLAgregarColonia.fxml"));
            Parent root = loader.load();
            FXMLAgregarColoniaController controladorForm = loader.getController();
            controladorForm.setAccessToken(token);
            Scene escenaFormulario = new Scene(root);
            Stage escenario = new Stage();
            escenario.setScene(escenaFormulario);
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLAgregarColoniaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) throws IOException, ParseException {
        String       postUrl       = "http://127.0.0.1:9090/revoke";// put in your url
        Gson         gson          = new Gson();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost     post          = new HttpPost(postUrl);
        post.setHeader(HttpHeaders.CONTENT_TYPE,"application/json");
        post.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        HttpContext responseHandler = null;
        CloseableHttpResponse response = httpClient.execute(post);
        HttpEntity entity = response.getEntity();
        String str = EntityUtils.toString(entity);
        Token token = gson.fromJson(str, Token.class);
        System.out.println(token.getAccess_token());
        if (response.getCode() == 200) {
            cerrarVentana();
        }
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) lbTitulo.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clicModificarColonia(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLModificarColonia.fxml"));
            Parent root = loader.load();
            FXMLModificarColoniaController controladorForm = loader.getController();
            controladorForm.setAccessToken(token);
            Scene escenaFormulario = new Scene(root);
            Stage escenario = new Stage();
            escenario.setScene(escenaFormulario);
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLModificarColoniaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clicModificarRuta(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLModificarRuta.fxml"));
            Parent root = loader.load();
            FXMLModificarRutaController controladorForm = loader.getController();
            controladorForm.setAccessToken(token);
            Scene escenaFormulario = new Scene(root);
            Stage escenario = new Stage();
            escenario.setScene(escenaFormulario);
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLModificarRutaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clicEliminarColonia(ActionEvent event) {
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLEliminarColonia.fxml"));
            Parent root = loader.load();
            FXMLEliminarColoniaController controladorForm = loader.getController();
            controladorForm.setAccessToken(token);
            Scene escenaFormulario = new Scene(root);
            Stage escenario = new Stage();
            escenario.setScene(escenaFormulario);
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLEliminarColoniaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clicEliminarRuta(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLEliminarRuta.fxml"));
            Parent root = loader.load();
            FXMLEliminarRutaController controladorForm = loader.getController();
            controladorForm.setAccessToken(token);
            Scene escenaFormulario = new Scene(root);
            Stage escenario = new Stage();
            escenario.setScene(escenaFormulario);
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLEliminarRutaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
