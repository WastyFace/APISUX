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
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.apache.hc.client5.http.classic.methods.HttpPatch;
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
import pojo.Ruta;

/**
 * FXML Controller class
 *
 * @author andrea
 */
public class FXMLModificarRutaController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TableView<Colonia> tvColonias;
    @FXML
    private TableColumn tvRutas;
    @FXML
    private ComboBox<Colonia> cbColonias;
    @FXML
    private TextArea taRecorrido;
    @FXML
    private ComboBox<Ruta> cbRutas;
    
    private String token;
    @FXML
    private ImageView ivRuta;
    
    private ObservableList<Colonia> colonias;
    
    private ObservableList<Ruta> rutas;
    
    private Colonia coloniaSeleccionada;
    
    private String idColoniaEdicion;
    
    private Ruta rutaSeleccionada;
    
    private String idRutaSeleccion;
    
    private Path destinationPath;
    
    private Path sourcePath;
    
    private File selectedFile = null;
    
    private ObservableList<String> cadenaColonias;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        colonias = FXCollections.observableArrayList();
        rutas = FXCollections.observableArrayList();
        tvRutas.setCellValueFactory(new PropertyValueFactory("nombre"));
        cadenaColonias = FXCollections.observableArrayList();
        try {
            // TODO
            cargarColonias();
            cargarRutas();
        } catch (IOException ex) {
            mostrarAlerta("No existe conexion con el servidor", "Por el momento no se logro establecer la conexion, intente más tarde", Alert.AlertType.ERROR);
            Logger.getLogger(FXMLConsultaTuRutaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void setAccessToken(String token) {
        this.token = token;
    }
    
    private void cargarRutas() throws IOException {
        try {
            int responseCode = 0;
            int n = 1;
            while (responseCode != 404) {
                try {
                    String urlArm = "http://127.0.0.1:9090/rutas/" + Integer.toString(n);
                    URL url = new URL(urlArm);
                    HttpURLConnection conexion = (HttpURLConnection)url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                    String inputLine = in.readLine();
                    responseCode = conexion.getResponseCode();
                    System.out.println(responseCode);
                    ObjectMapper mapper = new ObjectMapper();
                    Ruta ruta = mapper.readValue(inputLine, Ruta.class);
                    rutas.add(ruta);
                    n++;
                } catch (java.io.FileNotFoundException e) {
                    responseCode = 404;
                    break;
                }
            }
            cbRutas.setItems(rutas);
            cbRutas.valueProperty().addListener(new ChangeListener<Ruta>() {
                @Override
                public void changed(ObservableValue<? extends Ruta> observable, Ruta oldValue, Ruta newValue) {
                    if (newValue != null) {
                        rutaSeleccionada = newValue;
                        cargarRutaSeleccionada(newValue);
                        idRutaSeleccion = newValue.getId();
                    }
                }
            });
        } catch (MalformedURLException ex) {
            Logger.getLogger(FXMLMenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void cargarRutaSeleccionada(Ruta ruta) {
        tfNombre.setText(ruta.getNombre());
        taRecorrido.setText(ruta.getRecorrido());
        File file = new File (rutaSeleccionada.getImgPath());
        Image image = new Image(file.toURI().toString());
        ivRuta.setImage(image);
        String coloniasRuta = rutaSeleccionada.getColonias();
        String strFinal = coloniasRuta.substring(1,coloniasRuta.length()-1);
        String cols[] = strFinal.split(", ");
        for (int i=0;i<cols.length;i++) {
            Colonia aux = new Colonia();
            aux.setNombre(cols[i]);
            cadenaColonias.add(aux.getNombre());
            tvColonias.getItems().add(aux);
        }
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
            cbColonias.valueProperty().addListener(new ChangeListener<Colonia>() {
                @Override
                public void changed(ObservableValue<? extends Colonia> observable, Colonia oldValue, Colonia newValue) {
                    if (newValue != null) {
                        coloniaSeleccionada = newValue;
                        idColoniaEdicion = newValue.getId();
                    }
                }
            });
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
    private void clicGuardar(ActionEvent event) throws IOException, ParseException {
        if(tfNombre.getText().isEmpty() || taRecorrido.getText().isEmpty() || cadenaColonias.toString().isEmpty() || selectedFile == null){
            mostrarAlerta("Existen campos vacios", "Hay campos vacios necesarios, verificar la información", Alert.AlertType.ERROR);
        }else{
            Ruta ruta = new Ruta();
            ruta.setNombre(tfNombre.getText());
            ruta.setRecorrido(taRecorrido.getText());
            ruta.setColonias(cadenaColonias.toString());
            ruta.setImgPath("./img/" + ruta.getNombre() + ".png");
            System.out.println(ruta.getImgPath());
            String       patchUrl       = "http://127.0.0.1:9090/rutas/" + idRutaSeleccion;// put in your url
            Gson         gson          = new Gson();
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPatch     patch          = new HttpPatch(patchUrl);
            patch.setHeader(HttpHeaders.CONTENT_TYPE,"application/json");
            patch.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            StringEntity postingString = new StringEntity(gson.toJson(ruta));
            patch.setEntity(postingString);
            patch.setHeader("Content-type", "application/json");
            HttpContext responseHandler = null;
            CloseableHttpResponse response = httpClient.execute(patch);
            HttpEntity entity = response.getEntity();
            String str = EntityUtils.toString(entity);
            if (response.getCode() == 200) {
                mostrarAlerta("Ruta editada", "La ruta ha sido editada", Alert.AlertType.INFORMATION);
                cerrarVentana();
                if (selectedFile==null) {
                    cambiarNombreImagen(rutaSeleccionada, ruta);
                } else {
                    copiarImagen(ruta);
                }
                cerrarVentana();
            } else {
                mostrarAlerta("Error", "No se pudo procesar la solicitud", Alert.AlertType.ERROR);                 
            }
        }
    }

    @FXML
    private void clicAgregarColonia(ActionEvent event) {
        if (!cbColonias.getSelectionModel().isEmpty() && !tvColonias.getItems().contains(cbColonias.getSelectionModel().getSelectedItem()) && !cadenaColonias.contains(cbColonias.getSelectionModel().getSelectedItem().getNombre())) {
            cadenaColonias.add(cbColonias.getSelectionModel().getSelectedItem().getNombre());
            tvColonias.getItems().add(cbColonias.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void clicSeleccionarImagen(ActionEvent event) throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Agrega una imagen");
        selectedFile = fc.showOpenDialog(null);
        Image image = new Image(selectedFile.toURI().toString());
        ivRuta.setImage(image);
    }
    
    private void cambiarNombreImagen(Ruta oldR, Ruta newR) {
        File file = new File (oldR.getImgPath());
        File newFile = new File (newR.getImgPath());
        file.renameTo(newFile);
    }
    
    private void copiarImagen(Ruta ruta) throws IOException {
        File file = new File (rutaSeleccionada.getImgPath());
        file.delete();
        if (selectedFile != null) {
            sourcePath = Paths.get(selectedFile.toURI());
            destinationPath = Paths.get("./img/" + ruta.getNombre() + ".png");
            Files.copy(sourcePath, destinationPath);
        }
    }

    @FXML
    private void clicEliminarColonia(ActionEvent event) {
        if (!tvColonias.selectionModelProperty().getValue().isEmpty()) {
            int filaSeleccion = tvColonias.getSelectionModel().getSelectedIndex();
            Colonia coloniaSelec = tvColonias.selectionModelProperty().getValue().getSelectedItem();
            cadenaColonias.remove(coloniaSelec.getNombre());
            tvColonias.getItems().remove(filaSeleccion);
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setContentText(mensaje);
        a.showAndWait();
    }
}
