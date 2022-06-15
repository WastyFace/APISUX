/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import com.google.gson.Gson;
import java.io.IOException;
import static java.lang.Integer.parseInt;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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
import pojo.Colonia;
import pojo.Token;
import pojo.User;

/**
 * FXML Controller class
 *
 * @author andrea
 */
public class FXMLAgregarColoniaController implements Initializable {

    @FXML
    private TextField tfCodigoPostal;
    @FXML
    private TextField tfNombre;
    
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
    private void clicAgregar(ActionEvent event) throws IOException, ParseException {
        Colonia colonia = new Colonia();
        colonia.setNombre(tfNombre.getText());
        colonia.setCodigoPostal(parseInt(tfCodigoPostal.getText()));
        String       postUrl       = "http://127.0.0.1:9090/colonias";// put in your url
        Gson         gson          = new Gson();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost     post          = new HttpPost(postUrl);
        post.setHeader(HttpHeaders.CONTENT_TYPE,"application/json");
        post.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        StringEntity postingString = new StringEntity(gson.toJson(colonia));
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        HttpContext responseHandler = null;
        CloseableHttpResponse response = httpClient.execute(post);
        HttpEntity entity = response.getEntity();
        String str = EntityUtils.toString(entity);
        Token token = gson.fromJson(str, Token.class);
        System.out.println(token.getAccess_token());
        if (response.getCode() == 201) {
            mostrarAlerta("Colonia agregada", "La colonia ha sido agregada", Alert.AlertType.INFORMATION);                 
        } else if (response.getCode() == 400) {
            mostrarAlerta("Error", "Nombre ya utilizado", Alert.AlertType.ERROR);                 
        } else {
            mostrarAlerta("Error", "No se ha agregado la colonia", Alert.AlertType.ERROR);                 
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setContentText(mensaje);
        a.showAndWait();
    }
    
}
