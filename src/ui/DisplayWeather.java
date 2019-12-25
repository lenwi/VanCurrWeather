package ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;

public class DisplayWeather {

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

    public static void main(String[] args) throws IOException, JSONException {
        System.out.println(weatherAPI());
    }

    public static String weatherAPI() throws IOException, JSONException {
        JSONObject json = readJsonFromUrl("https://api.openweathermap.org/data/2.5/weather?q=Vancouver,ca&APPID="
                + "1cf7746e673b29fee70c1502cbaa77fc");
        String curr = "";
        JSONArray arr = json.getJSONArray("weather");
        for (int i = 0; i < arr.length(); i++) {
            String d = arr.getJSONObject(i).getString("description");
            String icon = arr.getJSONObject(i).getString("icon");
            URL url = new URL("http://openweathermap.org/img/wn/" + icon + "@2x.png");
            Image image = ImageIO.read(url);
            curr = "\nCurrent weather for Vancouver: \n" + d + "      " + image;
        }
        int c = json.getJSONObject("main").getInt("temp");
        int ss = json.getJSONObject("sys").getInt("sunset");
        String epochString = String.valueOf(ss);
        long epoch = Long.parseLong( epochString );
        Date sunset = new Date( epoch * 1000 );
        curr = curr + "\n" + (c - 273) + " °C" + "\n" + "Sunset: " + sunset;
        return curr;
        //do sunset time + icon display
    }
}
