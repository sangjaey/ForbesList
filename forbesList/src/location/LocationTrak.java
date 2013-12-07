package location;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class LocationTrak extends Service {
	public static LocationListener mlocListener;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("BOUND");
		return null;
	}

	@Override
	public void onCreate() {

		mlocListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				System.out.println("aaaaaaaaaaaaaaaaaaaaaaa");
				
				if (location != null) {
					Toast.makeText(
							getBaseContext(),
							"New location latitude [" + location.getLatitude()
									+ "] longitude [" + location.getLongitude()
									+ "]", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "No provider available",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "Provider enabled!",
						Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "GPS status changed",
						Toast.LENGTH_SHORT).show();
			}
		};

	}

}