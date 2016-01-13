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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import BO.VendasBO;
import client.com.br.orgafarma.ActivityPrincipal;
import client.com.br.orgafarma.Modal.PrazoMedio;
import client.com.br.orgafarma.Modal.PrevisaoVendaVendedorTelevendas;
import client.com.br.orgafarma.Modal.VendaVendedorTelevendas;
import client.com.br.orgafarma.R;


public class InboxVendaVendedorFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_inbox_venda_vendedor, container, false);

        ((ActivityPrincipal) getActivity()).getSupportActionBar().setTitle("Venda Vendedor/Televendas");

        new LoadingAsync(getActivity(), view).execute();


        return view;
    }

    private class LoadingAsync extends AsyncTask<Void, Void, PrevisaoVendaVendedorTelevendas> {

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
        protected PrevisaoVendaVendedorTelevendas doInBackground(Void... params) {
            PrevisaoVendaVendedorTelevendas previsaoVenda = new PrevisaoVendaVendedorTelevendas();

            try {
                JSONObject jsonObject = new JSONObject(VendasBO.listaVendaVendedorTelevendas());

                JSONObject vendas = jsonObject.getJSONObject("Vendas");

                JSONArray prazo = vendas.getJSONArray("PrazoMedio");

                PrazoMedio prazoMedio = new PrazoMedio();

                for (int j = 0; j < prazo.length(); j++) {
                    prazoMedio.setMeta(prazo.getJSONObject(j).getString("Meta"));
                    prazoMedio.setAtual(prazo.getJSONObject(j).getString("Atual"));
                    prazoMedio.setVariacao(prazo.getJSONObject(j).getString("Variacao"));
                }

                previsaoVenda.setPrazoMedio(prazoMedio);

                JSONArray pacotes = vendas.getJSONArray("Pacotes");

                previsaoVenda.setListaPrevisaoVendaVendedorTelevendas(new ArrayList<VendaVendedorTelevendas>());

                for (int j = 0; j < pacotes.length(); j++) {
                    VendaVendedorTelevendas vendedorTelevendas = new VendaVendedorTelevendas();
                    vendedorTelevendas.setPacote(pacotes.getJSONObject(j).getString("Pacote"));
                    vendedorTelevendas.setVendedor(pacotes.getJSONObject(j).getString("Vendedor"));
                    vendedorTelevendas.setTelevendas(pacotes.getJSONObject(j).getString("Televendas"));

                    previsaoVenda.getListaPrevisaoVendaVendedorTelevendas().add(vendedorTelevendas);
                }

                previsaoVenda.setValorLogistica(jsonObject.getJSONObject("Vendas").getString("Logistica"));

            } catch (JSONException e) {
                Log.i("ERRO", e.getMessage());
                return null;
            }

            return previsaoVenda;
        }

        @Override
        protected void onPostExecute(PrevisaoVendaVendedorTelevendas previsaoVenda) {
            int totalVendedor = 0;
            int totalTelevendas = 0;

            if (previsaoVenda != null) {
                TableLayout stk = (TableLayout) rootView.findViewById(R.id.tableLayoutVendaVendedor);
                TableLayout stkPrazo = (TableLayout) rootView.findViewById(R.id.tableLayoutPrazo);

                TableRow.LayoutParams params = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.weight = 1.0f;

                addCabecalhoPrazo(params);

                TableRow tableRow0 = new TableRow(mContext);
                adicionaTextViewLinha(tableRow0, params, previsaoVenda.getPrazoMedio().getMeta(), false, 0);
                adicionaTextViewLinha(tableRow0, params, previsaoVenda.getPrazoMedio().getAtual(), false, 0);
                adicionaTextViewLinha(tableRow0, params, previsaoVenda.getPrazoMedio().getVariacao(), false, 0);
                stkPrazo.addView(tableRow0);

                for (int i = 0; i < previsaoVenda.getListaPrevisaoVendaVendedorTelevendas().size(); i++) {
                    params.setMargins(0, 10, 0, 0);
                    TableRow tbrow = new TableRow(mContext);

                    adicionaTextViewLinha(tbrow, params, previsaoVenda.getListaPrevisaoVendaVendedorTelevendas().get(i).getPacote(), false, 0);
                    adicionaTextViewLinha(tbrow, params, previsaoVenda.getListaPrevisaoVendaVendedorTelevendas().get(i).getVendedor(), false, 0);
                    adicionaTextViewLinha(tbrow, params, previsaoVenda.getListaPrevisaoVendaVendedorTelevendas().get(i).getTelevendas(), false, 0);

                    totalVendedor += Integer.parseInt(previsaoVenda.getListaPrevisaoVendaVendedorTelevendas().get(i).getVendedor());
                    totalTelevendas += Integer.parseInt(previsaoVenda.getListaPrevisaoVendaVendedorTelevendas().get(i).getTelevendas());

                    stk.addView(tbrow);
                }

                int valorLogisitica = Integer.parseInt(previsaoVenda.getValorLogistica());

                adicionaLinhaTotal(rootView, mContext, totalTelevendas, totalVendedor, params, stk, valorLogisitica, previsaoVenda);
            } else {
                Log.i("Erro", "Erro no preenchimento das tabelas");
            }

            progressDialog.dismiss();

        }

        private void adicionaLinhaTotal(View rootView, Context context, int totalVendendor, int totalTelevendas, TableRow.LayoutParams params, TableLayout stk, int valorLogistica, PrevisaoVendaVendedorTelevendas previsaoVenda) {
            TableRow tbrow0 = (TableRow) new TableRow(rootView.getContext());

            adicionaTextViewLinha(tbrow0, params, "SubTotal - Vendedor", false, 16f);
            adicionaTextViewLinha(tbrow0, params, String.valueOf(totalVendendor), false, 16f);
            adicionaTextViewLinha(tbrow0, params, String.valueOf(totalTelevendas), false, 16f);

            tbrow0.setBackgroundColor(ContextCompat.getColor(mContext, R.color.md_grey_300));
            stk.addView(tbrow0);


            TableRow tbrow1 = (TableRow) new TableRow(rootView.getContext());

            adicionaTextViewLinha(tbrow1, params, "Logistica", false, 16f);
            adicionaTextViewLinha(tbrow1, params, previsaoVenda.getValorLogistica(), false, 16f);
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


