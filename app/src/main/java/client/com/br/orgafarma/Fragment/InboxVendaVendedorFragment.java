package client.com.br.orgafarma.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import BO.VendasBO;
import client.com.br.orgafarma.ActivityPrincipal;
import client.com.br.orgafarma.Modal.Pacote;
import client.com.br.orgafarma.Modal.PrazoMedio;
import client.com.br.orgafarma.Modal.PrevisaoVendaVendedorTelevendas;
import client.com.br.orgafarma.Modal.VendaTelevenda;
import client.com.br.orgafarma.Modal.VendaVendedorTelevendas;
import client.com.br.orgafarma.R;


public class InboxVendaVendedorFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_inbox_venda_vendedor, container, false);

        ((ActivityPrincipal) getActivity()).getSupportActionBar().setTitle("VendaMes Vendedor/Televendas");

        new LoadingAsync(getActivity(), view).execute();


        return view;
    }

    private class LoadingAsync extends AsyncTask<Void, Void, VendaTelevenda> {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        private Context mContext;
        private View rootView;

        public LoadingAsync(Context context, View rootView) {
            this.mContext = context;
            this.rootView = rootView;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Carregando...");
            progressDialog.show();
        }

        @Override
        protected VendaTelevenda doInBackground(Void... params) {
            VendaTelevenda vendaTelevenda;
            Gson gson = new Gson();
            try {
                vendaTelevenda = gson.fromJson(new JSONObject(VendasBO.listaVendaVendedorTelevendas(getContext())).toString(), VendaTelevenda.class);
            } catch (JSONException e) {
                Log.i("ERRO", e.getMessage());
                return null;
            }
            return vendaTelevenda;
        }

        @Override
        protected void onPostExecute(VendaTelevenda previsaoVenda) {
            int totalVendedor = 0;
            int totalTelevendas = 0;

            if (previsaoVenda != null) {
                TableLayout stk = (TableLayout) rootView.findViewById(R.id.tableLayoutVendaVendedor);
                TableLayout stkPrazo = (TableLayout) rootView.findViewById(R.id.tableLayoutPrazo);

                TableRow.LayoutParams params = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.weight = 1.0f;

                addCabecalhoPrazo(params);

                TableRow tableRow0 = new TableRow(mContext);
                adicionaTextViewLinha(tableRow0, params, previsaoVenda.getVendaMesTel().getPrazoMedios().get(0).getMeta(), false, 0);
                adicionaTextViewLinha(tableRow0, params, previsaoVenda.getVendaMesTel().getPrazoMedios().get(0).getAtual(), false, 0);
                adicionaTextViewLinha(tableRow0, params, previsaoVenda.getVendaMesTel().getPrazoMedios().get(0).getVariacao(), false, 0);
                stkPrazo.addView(tableRow0);



                for (int i = 0; i < previsaoVenda.getVendaMesTel().getPacotes().size(); i++) {
                    Pacote pct = previsaoVenda.getVendaMesTel().getPacotes().get(i);

                    params.setMargins(0, 10, 0, 0);
                    TableRow tbrow = new TableRow(mContext);

                    adicionaTextViewLinha(tbrow, params, pct.getPacote(), false, 0);
                    adicionaTextViewLinha(tbrow, params, pct.getVendedor() + "", false, 0);
                    adicionaTextViewLinha(tbrow, params, pct.getTelevenda() + "", false, 0);

                    totalVendedor += new Double(pct.getVendedor()).intValue();
                    totalTelevendas += new Double(pct.getTelevenda()).intValue();

                    stk.addView(tbrow);
                }
                int valorLogisitica = new Double(previsaoVenda.getVendaMesTel().getLogistica()).intValue();
                adicionaLinhaTotal(rootView, mContext, totalTelevendas, totalVendedor, params, stk, valorLogisitica, previsaoVenda);
            } else {
                Log.i("Erro", "Erro no preenchimento das tabelas");
            }
            progressDialog.dismiss();
        }

        private void adicionaLinhaTotal(View rootView, Context context, int totalVendendor, int totalTelevendas, TableRow.LayoutParams params, TableLayout stk, int valorLogistica, VendaTelevenda previsaoVenda) {
            TableRow tbrow0 = (TableRow) new TableRow(rootView.getContext());

            adicionaTextViewLinha(tbrow0, params, "SubTotal - Vendedor", false, 16f);
            adicionaTextViewLinha(tbrow0, params, String.valueOf(totalVendendor), false, 16f);
            adicionaTextViewLinha(tbrow0, params, String.valueOf(totalTelevendas), false, 16f);

            tbrow0.setBackgroundColor(ContextCompat.getColor(mContext, R.color.md_grey_300));
            stk.addView(tbrow0);


            TableRow tbrow1 = (TableRow) new TableRow(rootView.getContext());

            adicionaTextViewLinha(tbrow1, params, "Logistica", false, 16f);
            adicionaTextViewLinha(tbrow1, params, previsaoVenda.getVendaMesTel().getLogistica() + "", false, 16f);
            stk.addView(tbrow1);


            int soma = totalVendendor + totalTelevendas + valorLogistica;

            TableRow tbrow2 = (TableRow) new TableRow(rootView.getContext());

            adicionaTextViewLinha(tbrow2, params, "Total Geral", true, 16f);
            adicionaTextViewLinha(tbrow2, params, String.valueOf(soma), true, 16f);

            tbrow2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.md_grey_300));

            stk.addView(tbrow2);

        }

        private void addCabecalhoPrazo(TableRow.LayoutParams params){

            TableRow tbrow0 = (TableRow) rootView.findViewById(R.id.tableRowCabecalhoPrazo);

            adicionaTextViewLinha(tbrow0, params, "Meta Prazo Médio", false, 16f);
            adicionaTextViewLinha(tbrow0, params, "Prazo Atual", false, 16f);
            adicionaTextViewLinha(tbrow0, params, "Variação PM", false, 16f);

        }

        private void adicionaTextViewLinha(TableRow tableRow, TableRow.LayoutParams params, String texto, boolean bold, float textSize) {

            TextView textView = new TextView(rootView.getContext());
            textView.setText(texto);

            if (params != null) {
                textView.setLayoutParams(params);
            }

            if (textSize != 0) {
                textView.setTextSize(textSize);
            }

            if (bold) {
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            }

            tableRow.addView(textView);
        }

    }

}


