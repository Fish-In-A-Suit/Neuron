package org.multiverse;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.multiverse.multiversetools.GeneralTools;
import org.tord.neuroncore.Constants;
import org.tord.neuroncore.system.SystemSpecs;

//todo: extend the google map fragment behind the navigation bar and make the navigation bar color transparent
//todo: add a gray border to compass, profile and binoculars icon.
//todo: in settings, when changing user data, don't forget to implement a process to change only a part of the user data in the database
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText main_search;
    private ImageButton main_search_spyglass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneralTools.changeStatusBarColor(this, R.color.login_background_gradient_top);
        GeneralTools.changeNavigationBarColor(this, R.color.login_background_gradient_bottom);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_map);
        mapFragment.getMapAsync(this);

        main_search = (EditText) findViewById(R.id.main_search_edittext);
        main_search.clearFocus();
        main_search_spyglass = (ImageButton) findViewById(R.id.main_search_spyglass);
    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        positionWatermarkBelowTopToolbar();
    }

    private void positionWatermarkBelowTopToolbar() {
        int screenWidth = SystemSpecs.getScreen().getWidth();
        int screenHeight = SystemSpecs.getScreen().getHeight();

        mMap.setPadding(Constants.MARGIN_SIXTEEN, Constants.MARGIN_DEFAULT, screenWidth - Constants.GOOGLE_MAPS_WATERMARK_WIDTH, screenHeight - Constants.GOOGLE_MAP_WATERMARK_TOP_OFFSET);
    }
}
