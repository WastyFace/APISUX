/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import static java.lang.Integer.parseInt;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.maven.shared.utils.io.FileUtils;
import pojo.Colonia;
import pojo.Ruta;
import pojo.Token;

/**
 * FXML Controller class
 *
 * @author andrea
 */
public class FXMLAgregarRutaController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private ComboBox<Colonia> cbColonias;
    @FXML
    private TextArea taRecorrido;
    @FXML
    private TableView<Colonia> tvColonias;
    
    private ObservableList<Colonia> colonias;
    
    private String token;
    
    private Path destinationPath;
    
    private Path sourcePath;
    
    private File selectedFile;
    
    private ObservableList<String> cadenaColonias;
    @FXML
    private TableColumn tcColonias;
    @FXML
    private ImageView imageView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        colonias = FXCollections.observableArrayList();
        cadenaColonias = FXCollections.observableArrayList();
        tcColonias.setCellValueFactory(new PropertyValueFactory("nombre"));
        try {
            cargarColonias();
        } catch (IOException ex) {
            Logger.getLogger(FXMLAgregarRutaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void setAccessToken(String token) {
        this.token = token;
        System.out.println("Token inicializado: " + token);
    }
    
    private void cargarColonias() throws IOException {
        try {
            int responseCode = 0;
            int n = 1;
            while (responseCode != 404) {
                try {
                    String urlArm = "http://127.0.0.1:9090/colonias/" + Integer.toString(n);
                    URL url = new URL(urlArm);
                    HttpURLConnection conexion = (HttpURLConnection)url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                    String inputLine = in.readLine();
                    responseCode = conexion.getResponseCode();
                    System.out.println(responseCode);
                    ObjectMapper mapper = new ObjectMapper();
                    Colonia colonia = mapper.readValue(inputLine, Colonia.class);
                    colonias.add(colonia);
                    n++;
                } catch (java.io.FileNotFoundException e) {
                    responseCode = 404;
                    break;
                }
            }
            cbColonias.setItems(colonias);
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
        Stage stage = (Stage) tfNombre.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clicAgregar(ActionEvent event) throws IOException, ParseException {
        Ruta ruta = new Ruta();
        ruta.setNombre(tfNombre.getText());
        ruta.setRecorrido(taRecorrido.getText());
        ruta.setColonias(cadenaColonias.toString());
        ruta.setImgPath("./img/" + ruta.getNombre() + ".png");
        System.out.println(ruta.getImgPath());
        String       postUrl       = "http://127.0.0.1:9090/rutas";// put in your url
        Gson         gson          = new Gson();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost     post          = new HttpPost(postUrl);
        //post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        post.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        post.setHeader("Content-Encoding", "foo-1.0");
        post.setHeader("Content-Type", "application/json; charset=UTF-8");
        StringEntity postingString = new StringEntity(gson.toJson(ruta));
        post.setEntity(postingString);
        System.out.println(postingString.getContentEncoding());
        //post.setHeader("Content-type", "application/json;charset=UTF-8");
        HttpContext responseHandler = null;
        CloseableHttpResponse response = httpClient.execute(post);
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        response.setHeader(HttpHeaders.CONTENT_ENCODING, "UTF-8");
        HttpEntity entity = response.getEntity();
        String str = EntityUtils.toString(entity);
        System.out.println("respuesta: " + postingString);
        if (response.getCode() == 201) {
            mostrarAlerta("Ruta agregada", "La ruta ha sido agregada", Alert.AlertType.INFORMATION);
            copiarImagen(ruta);
        } else if (response.getCode() == 400) {
            mostrarAlerta("Error", "Nombre ya utilizado", Alert.AlertType.ERROR);                 
        } else {
            mostrarAlerta("Error", "No se ha agregado la colonia", Alert.AlertType.ERROR);                 
        }
    }

    @FXML
    private void clicAgregarColonia(ActionEvent event) {
        if (!cbColonias.getSelectionModel().isEmpty() && !tvColonias.getItems().contains(cbColonias.getSelectionModel().getSelectedItem())) {
            cadenaColonias.add(cbColonias.getSelectionModel().getSelectedItem().getNombre());
            tvColonias.getItems().add(cbColonias.getSelectionModel().getSelectedItem());
            System.out.println(cadenaColonias);
        }
    }

    @FXML
    private void clicSeleccionarImagen(ActionEvent event) throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Agrega una imagen");
        selectedFile = fc.showOpenDialog(null);
        Image image = new Image(selectedFile.toURI().toString());
        imageView.setImage(image);
    }
    
    private void copiarImagen(Ruta ruta) throws IOException {
        if (selectedFile != null) {
            sourcePath = Paths.get(selectedFile.toURI());
            destinationPath = Paths.get("./img/" + ruta.getNombre() + ".png");
            Files.copy(sourcePath, destinationPath);
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setContentText(mensaje);
        a.showAndWait();
    }
}
