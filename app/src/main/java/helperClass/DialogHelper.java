package helperClass;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by rodolfo.rezende on 24/02/2016.
 */
public class DialogHelper {
    private static ProgressDialog progressDialog;

    public static ProgressDialog getInstance(Context context){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(context);
            return progressDialog;
        } else {
            return progressDialog;
        }
    }

    public static void showDialog(Context ctx, String msg){
        progressDialog = getInstance(ctx);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }
}
