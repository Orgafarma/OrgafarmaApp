package helperClass;

import android.content.Context;
import android.net.ConnectivityManager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public static String getData(){
        return new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
    }

    public static Date getFirstDateOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date getLastDateOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static String getDataFormatted(Date date){
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public static String formatarDecimal(double value, int qtd){
        String zeroAntesDaVirgula = "";
        for (int i = 0;i < qtd; i++){
            zeroAntesDaVirgula += "0";
        }
        DecimalFormat numberFormat = new DecimalFormat("#." + zeroAntesDaVirgula);
        return numberFormat.format(value);
    }

    public static String formatarDecimal(String valueS, int qtd){
        double value = Double.parseDouble(valueS);
        String zeroAntesDaVirgula = "";
        for (int i = 0;i < qtd; i++){
            zeroAntesDaVirgula += "0";
        }
        DecimalFormat numberFormat = new DecimalFormat("#." + zeroAntesDaVirgula);
        return numberFormat.format(value);
    }

}
