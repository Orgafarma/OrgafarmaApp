package client.com.br.orgafarma.Fragment;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import client.com.br.orgafarma.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {


    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    public void showMessageErro(View view, Exception ex){
        String erroMessage = ex.getClass().getName();
        if (erroMessage.isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(view, getContext().getResources().getText(R.string.prob_config), Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar
                    .make(view, erroMessage, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        ex.printStackTrace();
    }

}
