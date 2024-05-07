package com.example.swifty.utils.handlers;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class MapHandler {

    public static void setUpMap(Context context, MapView mapView, GeoPoint startPoint, GeoPoint endPoint, GeoPoint midPoint, double zoom, long zoomTime) {
        Drawable person = ContextCompat.getDrawable(context, org.osmdroid.library.R.drawable.person);
        //Get map route
        RoadManager roadManager = new OSRMRoadManager(context, "swifty/1.0-beta (Android)");
        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(startPoint);
        waypoints.add(endPoint);
        Road road = roadManager.getRoad(waypoints);
        // Update UI on the main thread
        mapView.post(() -> {
            //Set marker for users current position
            Marker marker = new Marker(mapView);
            marker.setPosition(startPoint);
            marker.setTitle("User Location");
            marker.setIcon(person);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mapView.getOverlays().add(marker);
            mapView.getController().animateTo(midPoint, zoom, zoomTime);
            mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
            mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
            mapView.setMultiTouchControls(true);

            if (road != null) {
                //Set marker for deliveries current position
                Marker deliveryMarker = new Marker(mapView);
                deliveryMarker.setPosition(endPoint);
                deliveryMarker.setTitle("Delivery Location");
                deliveryMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                mapView.getOverlays().add(deliveryMarker);
                //Display road overlay
                Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
                mapView.getOverlays().add(roadOverlay);
                mapView.getController().setCenter(startPoint);
                mapView.invalidate();
            }
        });
    }
}
