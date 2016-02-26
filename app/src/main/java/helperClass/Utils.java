package helperClass;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by rodolfo.rezende on 24/02/2016.
 */
public class Utils {

    //add this line first in your manifest
    //      <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    public static boolean checkConnection(Context ctx){
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
