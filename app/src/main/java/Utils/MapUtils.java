package Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.whereabuses.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MapUtils {

    public static Bitmap getBusIcon(Context context){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.busicon);
        return Bitmap.createScaledBitmap(bitmap, 50, 50, false);
    }

    public static ArrayList<LatLng> getListOfLocations() {
        ArrayList<LatLng> locations = new ArrayList<>();
        locations.add(new LatLng(38.748406, -9.157238));
        locations.add(new LatLng(38.748440, -9.157130));
        locations.add(new LatLng(38.748480, -9.157120));
        locations.add(new LatLng(38.748520, -9.157110));
        locations.add(new LatLng(38.748560, -9.157100));
        return locations;
    }
}
