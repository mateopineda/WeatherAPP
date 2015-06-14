package g1.aplicaciones.com.weatherapp;



import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by PINEDA on 04/06/2015.
 */
public class Inicio extends Activity implements OnClickListener {

        private TextView ciudad, temperatura, descripcion;
        private EditText lbldato;
        private Button btnRequest;
        private ImageView icono;
        private Bitmap imagen;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_inicio);

            ciudad = (TextView) findViewById(R.id.lblCiudad);
            temperatura = (TextView) findViewById(R.id.lblTemperatura);
            descripcion = (TextView) findViewById(R.id.lblDescripcion);
            lbldato = (EditText) findViewById(R.id.obtenerCiudad);
            icono = (ImageView) findViewById(R.id.icono);
            btnRequest = (Button) findViewById(R.id.btnRequest);
            btnRequest.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (v.getId() == btnRequest.getId()) {
                String buscar = lbldato.getText().toString();
                WeatherTask weather = new WeatherTask();
                weather.execute(buscar);
                lbldato.setText("");
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private class WeatherTask extends AsyncTask<String, Void, Void> {
            private static final String TAG = "WeatherTask";
            private String Content;
            private String Error = null;
            private ProgressDialog Dialog = new ProgressDialog(Inicio.this);
            String data = "";

            @Override
            protected void onPreExecute() {
                Dialog.setMessage("Please wait..");
                Dialog.show();
            }

            @Override
            protected Void doInBackground(String... params) {
                try {
                    data = ((new WeatherHttpClient()).getWeatherData(params[0]));
                    JSONObject jsonResponse = new JSONObject(data);
                    String OutputData = jsonResponse.getJSONArray("weather")
                            .getJSONObject(0).optString("icon").toString();
                    imagen = ((new WeatherHttpClient()).retornaIcono(OutputData));
                } catch (Exception ex) {
                    Error = ex.getMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Dialog.dismiss();
                if (Error != null) {
                } else {
                    String OutputData = "";
                    JSONObject jsonResponse;
                    try {
                        jsonResponse = new JSONObject(data);
                        jsonResponse.optJSONArray("Android");
                        OutputData = jsonResponse.optString("name").toString();
                        ciudad.setText(OutputData);
                        OutputData = jsonResponse.getJSONObject("main")
                                .optString("temp").toString();
                        temperatura.setText(OutputData + "°C");
                        OutputData = jsonResponse.getJSONArray("weather")
                                .getJSONObject(0).optString("main").toString();
                        descripcion.setText(OutputData);
                        icono.setImageBitmap(imagen);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
