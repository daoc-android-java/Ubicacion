package daoc.ubicacion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

// http://developer.android.com/training/location/index.html
public class MainActivity extends Activity {

	private LocationManager locationManager;
	private TextView ubicacion;
	private WebView web;

	// Este ejemplo usa WebView. Se puede hacer mucho mas con la API de mapas:
	// https://developers.google.com/maps/
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ubicacion = (TextView) findViewById(R.id.ubicacion);

		web = (WebView) findViewById(R.id.web);
		web.setWebViewClient(new WebViewClient());
		web.getSettings().setJavaScriptEnabled(true);
		web.loadUrl("https://www.android.com");
	}

	//onClick definido para el boton en el archivo de layout
	public void iniciaUbicar(View v) {
		//En el emulador puede simular la ubicacion asi:
		//telnet localhost 5554
		//geo fix <longitud> <latitud>
		//Ej Quito: geo fix -0.18 -78.5
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		if (locationManager == null) {
			Toast.makeText(this, "No hay GPS en el dispositivo", Toast.LENGTH_SHORT).show();
		} else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, locationListener);
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			muestraUbicacion(location);
		} else {
			Toast.makeText(this, "El GPS no está habilitado", Toast.LENGTH_SHORT).show();
		}		
	}
	
	private LocationListener locationListener = new LocationListener() {	
		@Override
		public void onLocationChanged(Location location) {
			muestraUbicacion(location);
		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
		@Override
		public void onProviderEnabled(String provider) {}
		@Override
		public void onProviderDisabled(String provider) {}			
	};

	private void muestraUbicacion(Location location) {
		if (location != null) {
			Toast.makeText(this, "Ubicacion obtenida", Toast.LENGTH_SHORT).show();
			ubicacion.setText("Long: " + location.getLongitude() + " Lat: " + location.getLatitude());
			String url = "https://www.google.com/maps/@" + 
				location.getLatitude() + "," +
				location.getLongitude() + ",15z"; // z es el nivel de zoom
			web.loadUrl(url);
		} else {
			Toast.makeText(this, "El GPS aun no obtiene la ubicacion", Toast.LENGTH_SHORT).show();
		}		
	}	
	
}
