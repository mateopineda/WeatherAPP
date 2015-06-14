package g1.aplicaciones.com.weatherapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by PINEDA on 04/06/2015.
 */
public class WeatherHttpClient {
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?units=metric&q=";
    private static String IMG_URL = "http://openweathermap.org/img/w/";
    Bitmap imagen;

    public String getWeatherData(String location) {
        HttpURLConnection conn = null;
        InputStream is = null;
        URL url = null;
        try {
            url = new URL(BASE_URL + location);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            StringBuffer buffer = new StringBuffer();
            is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null)
                buffer.append(line + "\r\n");
            is.close();
            conn.disconnect();
            return buffer.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                conn.disconnect();
            } catch (Throwable t) {
            }
        }
        return null;
    }

    public Bitmap retornaIcono(String icono){
        URL imageUrl = null;
        HttpURLConnection conn = null;
        String Url = IMG_URL + icono + ".png";
        try {
            imageUrl = new URL(Url);
            conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagen;
    }
}