/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import pojo.Colonia;
import pojo.Ruta;


/**
 * FXML Controller class
 *
 * @author andrea
 */
public class FXMLRutasController implements Initializable {

    @FXML
    private ComboBox<Colonia> cbNombresColonias;
    private ObservableList<Colonia> colonias;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        colonias = FXCollections.observableArrayList();
//        try {
//            cargarColonias();
//        } catch (IOException ex) {
//            Logger.getLogger(FXMLRutasController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }    
    
    private void cargarColonias() throws IOException {
        try {
            //TODO
            for (int i = 0;i<77;i++) {
                String n = Integer.toString(i+1);
                String urlArm = "http://localhost:9090/colonias/" + n;
                URL prueba = new URL(urlArm);
                URLConnection conexion = prueba.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String inputLine = in.readLine();
                System.out.println(inputLine);
                ObjectMapper mapper = new ObjectMapper();
                Colonia colonia = mapper.readValue(inputLine, Colonia.class);
                colonias.add(colonia);
            }
            cbNombresColonias.setItems(colonias);
        } catch (MalformedURLException ex) {
            Logger.getLogger(FXMLMenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clicRuta1(ActionEvent event) {
        try {
            //TODO
            URL prueba = new URL("http://127.0.0.1:9090/colonias/1");
            URLConnection conexion = prueba.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            String inputLine = in.readLine();
            ObjectMapper mapper = new ObjectMapper();
            Ruta ruta = mapper.readValue(inputLine, Ruta.class);
            System.out.println(ruta.getRecorrido());
            in.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(FXMLMenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clicRuta2(ActionEvent event) throws IOException {
//        // The message that is going to be sent to the server  
//        // using the POST request  
//        final String messageContent = "{\n" + "\"userId\": 199, \r\n" +  
//        " \"id\": 101, \r\n" +  
//        " \"title\": \"About JavaTpoint\", \r\n" +  
//        " \"body\": \"JavaTpoint is a good site to learn Java. One must visit the site.\"" + "\n}";  
//        // Printing the message  
//        System.out.println(messageContent);  
//        // URL of the API or Server  
//        String url = "http://127.0.0.1:9090/rutas";  
//        URL urlObj = new URL(url);  
//        HttpURLConnection postCon = (HttpURLConnection) urlObj.openConnection();  
//        postCon.setRequestMethod("POST");  
//        postCon.setRequestProperty("userId", "abcdef");  
//        // Setting the message content type as JSON  
//        postCon.setRequestProperty("Content-Type", "application/json");  
//        postCon.setDoOutput(true);  
//        // for writing the message content to the server  
//        OutputStream osObj = postCon.getOutputStream();  
//        osObj.write(messageContent.getBytes());  
//        // closing the output stream  
//        osObj.flush();  
//        osObj.close();  
//        int respCode = postCon.getResponseCode();  
//        System.out.println("Response from the server is: \n");  
//        System.out.println("The POST Request Response Code :  " + respCode);  
//        System.out.println("The POST Request Response Message : " + postCon.getResponseMessage());  
//        if (respCode == HttpURLConnection.HTTP_CREATED)   
//        {   
//        // reaching here means the connection has been established  
//        // By default, InputStream is attached to a keyboard.  
//        // Therefore, we have to direct the InputStream explicitly  
//        // towards the response of the server  
//        InputStreamReader irObj = new InputStreamReader(postCon.getInputStream());   
//        BufferedReader br = new BufferedReader(irObj);  
//        String input = null;  
//        StringBuffer sb = new StringBuffer();  
//        while ((input = br .readLine()) != null)   
//        {  
//            sb.append(input);  
//        }   
//        br.close();  
//        postCon.disconnect();  
//        // printing the response  
//        System.out.println(sb.toString());  
//        }   
//        else   
//        {  
//        // connection was not successful  
//        System.out.println("POST Request did not work.");  
//        }
    }

    @FXML
    private void clicRuta3(ActionEvent event) throws IOException {
        Ruta ruta = new Ruta();
        ruta.setNombre("Xocotla");
        ruta.setRecorrido("Xocotla, Otilpan, Carretera a San Andrés, Luz del Barrio, Av. Fernando Guitierrez Barrios, Sayago, Mártires 28 de agosto, Av. Fernando Guitierrez Barrios, Carretera a San Andrés, Otilpan, Xocotla");
        ruta.setImgPath("path");
        String       postUrl       = "http://127.0.0.1:9090/rutas";// put in your url
        Gson         gson          = new Gson();
        HttpClient   httpClient    = HttpClientBuilder.create().build();
        HttpPost     post          = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(ruta));//gson.tojson() converts your pojo to json
        System.out.println(postingString);
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        HttpResponse  response = httpClient.execute(post);
    }

    @FXML
    private void clicRuta4(ActionEvent event) {
    }

    @FXML
    private void clicRuta5(ActionEvent event) {
    }

    @FXML
    private void clicRuta10(ActionEvent event) {
    }

    @FXML
    private void clicRuta9(ActionEvent event) {
    }

    @FXML
    private void clicRuta8(ActionEvent event) {
    }

    @FXML
    private void clicRuta7(ActionEvent event) {
    }

    @FXML
    private void clicRuta6(ActionEvent event) {
    }

    @FXML
    private void clicRuta15(ActionEvent event) {
    }

    @FXML
    private void clicRuta14(ActionEvent event) {
    }

    @FXML
    private void clicRuta13(ActionEvent event) {
    }

    @FXML
    private void clicRuta12(ActionEvent event) {
    }

    @FXML
    private void clicRuta11(ActionEvent event) {
    }

    @FXML
    private void clicRuta20(ActionEvent event) {
    }

    @FXML
    private void clicRuta19(ActionEvent event) {
    }

    @FXML
    private void clicRuta18(ActionEvent event) {
    }

    @FXML
    private void clicRuta17(ActionEvent event) {
    }

    @FXML
    private void clicRuta16(ActionEvent event) {
    }

    @FXML
    private void clicRuta21(ActionEvent event) {
    }

    @FXML
    private void clicRuta28(ActionEvent event) {
    }

    @FXML
    private void clicRuta23(ActionEvent event) {
    }

    @FXML
    private void clicRuta24(ActionEvent event) {
    }

    @FXML
    private void clicRuta26(ActionEvent event) {
    }

    @FXML
    private void clicRuta27(ActionEvent event) {
    }

    @FXML
    private void clicRuta22(ActionEvent event) {
    }

    @FXML
    private void clicRuta35(ActionEvent event) {
    }

    @FXML
    private void clicRuta30(ActionEvent event) {
    }

    @FXML
    private void clicRuta31(ActionEvent event) {
    }

    @FXML
    private void clicRuta33(ActionEvent event) {
    }

    @FXML
    private void clicRuta34(ActionEvent event) {
    }

    @FXML
    private void clicRuta29(ActionEvent event) {
    }

    @FXML
    private void clicRuta25(ActionEvent event) {
    }

    @FXML
    private void clicRuta32(ActionEvent event) {
    }

    @FXML
    private void clicRuta36(ActionEvent event) {
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setContentText(mensaje);
        a.showAndWait();
    }
}
