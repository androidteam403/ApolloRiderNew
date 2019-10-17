package com.ahmadrosid.lib.drawroutemap;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ocittwo on 11/14/16.
 *
 * @Author Ahmad Rosid
 * @Email ocittwo@gmail.com
 * @Github https://github.com/ar-android
 * @Web http://ahmadrosid.com
 */
public class RouteDrawerTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    private PolylineOptions lineOptions = null;
    private ArrayList<LatLng> points = null;

    private GoogleMap mMap;
    private int colorFlag;
    private DirectionApiCallback directionApiCallback;
    private Polyline polyline;

    public RouteDrawerTask(GoogleMap mMap, int colorFlag, DirectionApiCallback directionApiCallback) {
        this.mMap = mMap;
        this.colorFlag = colorFlag;
        this.directionApiCallback = directionApiCallback;
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);

            Log.d("RouteDrawerTask", jsonData[0]);
            DataRouteParser parser = new DataRouteParser(colorFlag,directionApiCallback);
            Log.d("RouteDrawerTask", parser.toString());

            // Starts parsing data
            routes = parser.parse(jObject);
            Log.d("RouteDrawerTask", "Executing routes");
            Log.d("RouteDrawerTask", routes.toString());

        } catch (Exception e) {
            Log.d("RouteDrawerTask", e.toString());
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        if (result != null)
            drawPolyLine(result);
    }

    private void drawPolyLine(List<List<HashMap<String, String>>> result) {
        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }


            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(6);
            if (colorFlag == 0)
                lineOptions.color(ContextCompat.getColor(DrawRouteMaps.getContext(), R.color.delivery_pharmacy));
            else
                lineOptions.color(ContextCompat.getColor(DrawRouteMaps.getContext(), R.color.delivery_user));
        }

//        // Drawing polyline in the Google Map for the i-th route
        if (lineOptions != null && mMap != null) {
                polyline = mMap.addPolyline(lineOptions);
        } else {
            Log.d("onPostExecute", "without Polylines draw");
        }
    }

}
