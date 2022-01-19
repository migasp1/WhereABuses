package Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.whereabuses.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MapUtils {

    public static Bitmap getBusIcon(Context context){
        if(context != null) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.busicon);
            return Bitmap.createScaledBitmap(bitmap, 50, 50, false);
        }
        return null;
    }
    public static Bitmap getAccidentIcon(Context context){
        if(context != null) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.carcrash);
            return Bitmap.createScaledBitmap(bitmap, 50, 50, false);
        }
        return null;
    }

    public static Bitmap getSlowTrafficIcon(Context context){
        if(context != null) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.trafficjam);
            return Bitmap.createScaledBitmap(bitmap, 50, 50, false);
        }
        return null;
    }
}
