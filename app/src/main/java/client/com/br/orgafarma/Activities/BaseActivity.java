package client.com.br.orgafarma.Activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import client.com.br.orgafarma.R;

/**
 * Created by rodolfo.rezende on 24/03/2016.
 */
public class BaseActivity extends AppCompatActivity {

    public void showMessageErro(View view, Exception ex){
        String erroMessage = ex.getMessage();
        if (erroMessage.isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(view, this.getResources().getText(R.string.prob_config), Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar
                    .make(view, ex.getMessage(), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        ex.printStackTrace();
    }
    public void showMessageErro(View view, String ex){
        String erroMessage = ex;
        if (erroMessage.isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(view, this.getResources().getText(R.string.prob_config), Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar
                    .make(view, ex, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

}
