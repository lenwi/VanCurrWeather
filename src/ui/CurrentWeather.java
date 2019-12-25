package ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;

public class CurrentWeather extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, JSONException {
        primaryStage.setTitle("Current Weather - Vancouver");

        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setStyle("-fx-background-color: #5F9EA0;");

        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.TOP_CENTER);

        String imageSource = getIconURL();
        ImageView imageView = new ImageView(new Image(imageSource));

        Label weather = new Label(weatherAPI());
        weather.setStyle("-fx-font: italic 16 palatino;" + "-fx-text-fill: #F0F8FF;");

        vbox.getChildren().addAll(imageView, weather);

        gp.getChildren().addAll(vbox);

        Scene scene = new Scene(gp, 550, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }


    public static String weatherAPI() throws IOException, JSONException {
        JSONObject json = readJsonFromUrl("https://api.openweathermap.org/data/2.5/weather?q=Vancouver,ca&APPID="
                + "1cf7746e673b29fee70c1502cbaa77fc");
        String curr = "";
        JSONArray arr = json.getJSONArray("weather");
        for (int i = 0; i < arr.length(); i++) {
            String d = arr.getJSONObject(i).getString("description");
            curr = "     Current weather for Vancouver: \n\n" + "                      " + d;
        }
        int c = json.getJSONObject("main").getInt("temp");
        int ss = json.getJSONObject("sys").getInt("sunset");
        String epochString = String.valueOf(ss);
        long epoch = Long.parseLong( epochString );
        Date sunset = new Date( epoch * 1000 );
        curr = curr + "\n" + "                      " + (c - 273) + " °C" + "\n\n" + "Sunset: " + sunset;
        return curr;
        //do sunset time + icon display
    }

    public static String getIconURL() throws IOException, JSONException {
        JSONObject json = readJsonFromUrl("https://api.openweathermap.org/data/2.5/weather?q=Vancouver,ca&APPID="
                + "1cf7746e673b29fee70c1502cbaa77fc");
        String curr = "";
        JSONArray arr = json.getJSONArray("weather");
        for (int i = 0; i < arr.length(); i++) {
            String icon = arr.getJSONObject(i).getString("icon");
            curr = "http://openweathermap.org/img/wn/" + icon + "@2x.png";
        }
        return curr;
    }
}
