//  Created by David Brodsky
//  Copyright (c) 2013 OpenWatch FPC. All rights reserved.
//
//  This file is part of ACLU-AZ-Android.
//
//  ACLU-AZ-Android is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  ACLU-AZ-Android is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with ACLU-AZ-Android.  If not, see <http://www.gnu.org/licenses/>.
package net.openwatch.acluaz.location;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

//	Thanks, Fedor!
public class DeviceLocation {
    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_enabled=false;
    boolean network_enabled=false;
    private static final String TAG = "DeviceLocation";
    private Location bestLocation;
    private boolean waitForGpsFix;
    
    private static final float ACCURATE_LOCATION_THRESHOLD_METERS = 20;
    
    /**
     * This method will call gotLocation each time a new location is received that is more accurate
     * than the last available
     * @param context
     * @param result
     * @param waitForGpsFix even if a network location is gotten, wait for a gps fix
     * @return
     */
    public boolean getLocation(Context context, LocationResult result, boolean waitForGpsFix)
    {
    	this.waitForGpsFix = waitForGpsFix;
    	
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult=result;
        if(lm==null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //exceptions will be thrown if provider is not permitted.
        try{gps_enabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
        try{network_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}

        //don't start listeners if no provider is enabled
        if(!gps_enabled && !network_enabled)
            return false;

        if(gps_enabled)
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
        if(network_enabled)
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
        timer1=new Timer();
        timer1.schedule(new GetBestLocation(), 10000);
        return true;
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
        	boolean sentLocation = false;
        	Log.i(TAG, "got GPS loc accurate to " + String.valueOf(location.getAccuracy()) + "m");
        	if(bestLocation == null || bestLocation.getAccuracy() > location.getAccuracy()){
            	bestLocation = location;
            	locationResult.gotLocation(bestLocation);
            	sentLocation = true;
        	}
        	
        	if(!waitForGpsFix || bestLocation.getAccuracy() < ACCURATE_LOCATION_THRESHOLD_METERS){
        		timer1.cancel();
        		if(!sentLocation)
        			locationResult.gotLocation(bestLocation);
                lm.removeUpdates(this);
                lm.removeUpdates(locationListenerNetwork);
        	}
            
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
        	boolean sentLocation = false;
        	Log.i(TAG, "got network loc accurate to " + String.valueOf(location.getAccuracy()) + "m");
        	if(bestLocation == null || bestLocation.getAccuracy() > location.getAccuracy()){
            	bestLocation = location;
            	locationResult.gotLocation(bestLocation);
            	sentLocation = true;
        	}
        	
        	if(!waitForGpsFix || bestLocation.getAccuracy() < ACCURATE_LOCATION_THRESHOLD_METERS){
        		timer1.cancel();
        		if(!sentLocation)
        			locationResult.gotLocation(bestLocation);
                lm.removeUpdates(this);
                lm.removeUpdates(locationListenerGps);
        	}
      
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    class GetBestLocation extends TimerTask {
        @Override
        public void run() {
        	Log.i(TAG, "Timer expired before adequate location acquired");
             lm.removeUpdates(locationListenerGps);
             lm.removeUpdates(locationListenerNetwork);

             Location net_loc=null, gps_loc=null;
             if(gps_enabled)
                 gps_loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
             if(network_enabled)
                 net_loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

             //if there are both values use the latest one
             if(gps_loc!=null && net_loc!=null){
                 if(gps_loc.getTime()>net_loc.getTime())
                     locationResult.gotLocation(gps_loc);
                 else
                     locationResult.gotLocation(net_loc);
                 return;
             }

             if(gps_loc!=null){
                 locationResult.gotLocation(gps_loc);
                 return;
             }
             if(net_loc!=null){
                 locationResult.gotLocation(net_loc);
                 return;
             }
             locationResult.gotLocation(null);
        }
    }

    public static abstract class LocationResult{
        public abstract void gotLocation(Location location);
    }
 
}