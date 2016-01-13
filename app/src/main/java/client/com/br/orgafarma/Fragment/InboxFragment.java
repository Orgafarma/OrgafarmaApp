package client.com.br.orgafarma.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import BO.VendasBO;
import Util.MensagemUtil;
import client.com.br.orgafarma.ActivityPrincipal;
import client.com.br.orgafarma.Modal.Positivacao;
import client.com.br.orgafarma.Modal.PrevisaoVenda;
import client.com.br.orgafarma.Modal.VendaMes;
import client.com.br.orgafarma.Modal.Vendedor;
import client.com.br.orgafarma.R;


public class InboxFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        ((ActivityPrincipal) getActivity()).getSupportActionBar().setTitle("Venda do Mês");

        new LoadingAsync(getActivity(), view).execute();


        return view;
    }



    private class LoadingAsync extends AsyncTask<Void, Void, PrevisaoVenda> {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        private Context mContext;
        private View rootView;

        public LoadingAsync(Context context, View rootView){
            this.mContext=context;
            this.rootView=rootView;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Carregando...");
            progressDialog.show();
        }

        @Override
        protected PrevisaoVenda doInBackground(Void... params) {
            PrevisaoVenda previsaoVenda = new PrevisaoVenda();

            try {
                JSONObject jsonObject = new JSONObject(VendasBO.listaPrevisaoVenda());

                JSONObject vendas = jsonObject.getJSONObject("Vendas");


                JSONArray positivados = vendas.getJSONArray("Positivados");
                Positivacao positivacao = new Positivacao();

                for(int i=0; i < positivados.length(); i++){
                    positivacao.setBaseClientes(positivados.getJSONObject(i).getString("BaseClientes"));
                    positivacao.setMeta(positivados.getJSONObject(i).getString("Meta"));
                    positivacao.setPositivados(positivados.getJSONObject(i).getString("Positivados"));
                    positivacao.setCOB(positivados.getJSONObject(i).getString("COB"));
                    positivacao.setProjecao(positivados.getJSONObject(i).getString("Projecao"));
                    positivacao.setVazio(positivados.getJSONObject(i).getString("Vazio"));
                }

                previsaoVenda.setPositivacao(positivacao);

                JSONArray mes = vendas.getJSONArray("Mes");

                previsaoVenda.setListaVendaMes(new ArrayList<VendaMes>());

                for (int j = 0; j < mes.length(); j++) {
                    VendaMes vendaMes = new VendaMes();
                    vendaMes.setPacote(mes.getJSONObject(j).getString("Pacote"));
                    vendaMes.setMeta(mes.getJSONObject(j).getString("Meta"));
                    vendaMes.setVenda(mes.getJSONObject(j).getString("Venda"));
                    vendaMes.setCOB(mes.getJSONObject(j).getString("COB"));
                    vendaMes.setProjecao(mes.getJSONObject(j).getString("Projecao"));
                    vendaMes.setVazio(mes.getJSONObject(j).getString("Vazio"));

                    previsaoVenda.getListaVendaMes().add(vendaMes);
                }

                JSONArray mesLogistica = vendas.getJSONArray("Logistica");
                previsaoVenda.setListaVendaMesLogistica(new ArrayList<VendaMes>());
                for (int j = 0; j < mesLogistica.length(); j++) {
                    VendaMes vendaMes = new VendaMes();
                    vendaMes.setPacote(mesLogistica.getJSONObject(j).getString("Pacote"));
                    vendaMes.setMeta(mesLogistica.getJSONObject(j).getString("Meta"));
                    vendaMes.setVenda(mesLogistica.getJSONObject(j).getString("Venda"));
                    vendaMes.setCOB(mesLogistica.getJSONObject(j).getString("COB"));
                    vendaMes.setProjecao(mesLogistica.getJSONObject(j).getString("Projecao"));
                    vendaMes.setVazio(mesLogistica.getJSONObject(j).getString("Vazio"));

                    previsaoVenda.getListaVendaMesLogistica().add(vendaMes);
                }

                Vendedor vendedor = new Vendedor();
                vendedor.setNome(jsonObject.getJSONObject("Vendas").getString("Vendedor"));
                previsaoVenda.setVendedor(vendedor);

            } catch (JSONException e) {
                Log.i("ERRO", e.getMessage());
                return  null;
            }

            return previsaoVenda;
        }

        @Override
        protected void onPostExecute(PrevisaoVenda previsaoVenda) {

            int totalMeta = 0;
            double totalVenda = 0;
            double totalCOB = 0;
            double totalProjecao = 0;
            double totalVazio = 0;

            if(previsaoVenda != null){
                TableLayout stk = (TableLayout) rootView.findViewById(R.id.tableLayoutMes);
                TableLayout stkPositivacao = (TableLayout) rootView.findViewById(R.id.tableLayoutPositivacao);

                TableRow.LayoutParams params = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.weight = 2.0f;
                params.gravity = Gravity.CENTER_HORIZONTAL;

                addLinhaMesVenda(params);
                addLinhaPositivacao(params);

                TableRow tableRowPos = new TableRow(mContext);
                adicionaTextViewLinha(tableRowPos, params, previsaoVenda.getPositivacao().getBaseClientes(), false, 0);
                adicionaTextViewLinha(tableRowPos,params, previsaoVenda.getPositivacao().getMeta(), false, 0);
                adicionaTextViewLinha(tableRowPos,params, previsaoVenda.getPositivacao().getPositivados(), false, 0);
                adicionaTextViewLinha(tableRowPos,params, previsaoVenda.getPositivacao().getCOB(), false, 0);
                adicionaTextViewLinha(tableRowPos,params, previsaoVenda.getPositivacao().getProjecao(), false, 0);
                adicionaTextViewLinha(tableRowPos,params, previsaoVenda.getPositivacao().getVazio(), false, 0);

                stkPositivacao.addView(tableRowPos);

                TextView txtNomeVendedor = (TextView) rootView.findViewById(R.id.txtVendedor);
                txtNomeVendedor.setText(previsaoVenda.getVendedor().getNome());

                for (int i = 0; i < previsaoVenda.getListaVendaMes().size(); i++) {
                    params.setMargins(0, 10, 0, 0);
                    TableRow tbrow = new TableRow(mContext);;

                    adicionaTextViewLinha(tbrow,params, previsaoVenda.getListaVendaMes().get(i).getPacote(), false, 0);
                    adicionaTextViewLinha(tbrow, params, previsaoVenda.getListaVendaMes().get(i).getMeta(), false, 0);
                    adicionaTextViewLinha(tbrow,params, previsaoVenda.getListaVendaMes().get(i).getVenda(), false, 0);
                    adicionaTextViewLinha(tbrow,params, previsaoVenda.getListaVendaMes().get(i).getCOB(), false, 0);
                    adicionaTextViewLinha(tbrow, params, previsaoVenda.getListaVendaMes().get(i).getProjecao(), false, 0);

                    ImageView view = new ImageView(mContext);

                    double vazio = Double.parseDouble(previsaoVenda.getListaVendaMes().get(i).getVazio());

                    if(vazio <= 100.00) {
                        view.setImageResource(R.drawable.status_fail);
                    }else {
                        view.setImageResource(R.drawable.status_ok);
                    }

                    adicionaImageViewLinha(tbrow, params, view, false, 0);

                    adicionaTextViewLinha(tbrow, params, previsaoVenda.getListaVendaMes().get(i).getVazio() + "%", false, 0);


                    totalMeta +=  Integer.parseInt(previsaoVenda.getListaVendaMes().get(i).getMeta());
                    totalVenda +=  Double.parseDouble(previsaoVenda.getListaVendaMes().get(i).getVenda());
                    totalCOB +=  Double.parseDouble(previsaoVenda.getListaVendaMes().get(i).getCOB());
                    totalProjecao +=  Double.parseDouble(previsaoVenda.getListaVendaMes().get(i).getProjecao());
                    //totalVazio +=  Double.parseDouble(previsaoVenda.getListaVendaMes().get(i).getVazio());

                    stk.addView(tbrow);
                }

                DecimalFormat df = new DecimalFormat("0.##");
                String total = df.format(totalVenda);
                total = total.replace(",", ".");
                totalVenda = Double.parseDouble(total);

                total = df.format(totalProjecao);
                total = total.replace(",", ".");
                totalProjecao = Double.parseDouble(total);

                totalVazio = ((totalProjecao * 100)/totalMeta);
                total = df.format(totalVazio);
                total = total.replace(",", ".");
                totalVazio = Double.parseDouble(total);


                adicionaTotaisVendaDireta(params,totalMeta,totalVenda,totalCOB,totalProjecao,totalVazio,stk,"Venda Direta");

                totalMeta = 0;
                totalVenda = 0;
                totalCOB = 0;
                totalProjecao = 0;
                totalVazio = 0;

                for (int i = 0; i < previsaoVenda.getListaVendaMesLogistica().size(); i++) {
                    params.setMargins(0, 10, 0, 0);
                    TableRow tbrow = new TableRow(mContext);

                    adicionaTextViewLinha(tbrow, params, previsaoVenda.getListaVendaMesLogistica().get(i).getPacote(), false, 0);
                    adicionaTextViewLinha(tbrow,params, previsaoVenda.getListaVendaMesLogistica().get(i).getMeta(), false, 0);
                    adicionaTextViewLinha(tbrow,params, previsaoVenda.getListaVendaMesLogistica().get(i).getVenda(), false, 0);
                    adicionaTextViewLinha(tbrow,params, previsaoVenda.getListaVendaMesLogistica().get(i).getCOB(), false, 0);
                    adicionaTextViewLinha(tbrow,params, previsaoVenda.getListaVendaMesLogistica().get(i).getProjecao(), false, 0);
                    adicionaTextViewLinha(tbrow,params, previsaoVenda.getListaVendaMesLogistica().get(i).getVazio(), false, 0);

                    totalMeta += Integer.parseInt(previsaoVenda.getListaVendaMesLogistica().get(i).getMeta());
                    totalVenda +=  Double.parseDouble(previsaoVenda.getListaVendaMesLogistica().get(i).getVenda());
                    totalCOB += Double.parseDouble(previsaoVenda.getListaVendaMesLogistica().get(i).getCOB());
                    totalProjecao += Double.parseDouble(previsaoVenda.getListaVendaMesLogistica().get(i).getProjecao());
                    totalVazio += Double.parseDouble(previsaoVenda.getListaVendaMesLogistica().get(i).getVazio());

                    stk.addView(tbrow);
                }

                //totalVazio = ((totalProjecao * 100)/totalMeta);

                adicionaTotaisVendaDireta(params,totalMeta,totalVenda,totalCOB,totalProjecao,totalVazio,stk,"Logistica");

            }
            else {
                Log.i("Erro", "Erro no preenchimento das tabelas");
            }

            progressDialog.dismiss();
        }


        private void addLinhaMesVenda(TableRow.LayoutParams params){

            TableRow tbrow0 = (TableRow) rootView.findViewById(R.id.tableRowMesVenda);

            adicionaTextViewLinha(tbrow0, params, "Pacote", false, 16f);
            adicionaTextViewLinha(tbrow0, params, "Meta", false, 16f);
            adicionaTextViewLinha(tbrow0, params, "Venda", false, 16f);
            adicionaTextViewLinha(tbrow0, params, "%COB", false, 16f);
            adicionaTextViewLinha(tbrow0, params, "Projeção", false, 16f);
            adicionaTextViewLinha(tbrow0, params, "", false, 16f);

        }

        private void addLinhaPositivacao(TableRow.LayoutParams params){

            TableRow tbrow0 = (TableRow) rootView.findViewById(R.id.tableRowCabecalhoPositivacao);

            adicionaTextViewLinha(tbrow0, params, "Base Clientes", false, 16f);
            adicionaTextViewLinha(tbrow0, params, "Meta", false, 16f);
            adicionaTextViewLinha(tbrow0, params, "Positivados", false, 16f);
            adicionaTextViewLinha(tbrow0, params, "%COB", false, 16f);
            adicionaTextViewLinha(tbrow0, params, "Projeção", false, 16f);
            adicionaTextViewLinha(tbrow0, params, "", false, 16f);

        }

        private void adicionaTotaisVendaDireta(TableRow.LayoutParams params, int totalMeta, double totalVenda,
                                               double totalCOB, double totalProjecao, double totalVazio, TableLayout tableLayout, String Titulo){

            TableRow tbrow0 = new TableRow(rootView.getContext());;

            adicionaTextViewLinha(tbrow0, params, Titulo, true, 16f);
            adicionaTextViewLinha(tbrow0, params, String.valueOf(totalMeta), true, 16f);
            adicionaTextViewLinha(tbrow0, params, String.valueOf(totalVenda), true, 16f);
            adicionaTextViewLinha(tbrow0, params, String.valueOf(totalCOB), true, 16f);
            adicionaTextViewLinha(tbrow0, params, String.valueOf(totalProjecao), true, 16f);

            ImageView view = new ImageView(mContext);

            if(totalVazio <= 100.00) {
                view.setImageResource(R.drawable.status_fail);
            }else {
                view.setImageResource(R.drawable.status_ok);
            }

            adicionaImageViewLinha(tbrow0, params, view, false, 0);

            adicionaTextViewLinha(tbrow0, params, String.valueOf(totalVazio) + "%", true, 16f);

            tbrow0.setBackgroundColor(ContextCompat.getColor(mContext, R.color.md_grey_300));

            tableLayout.addView(tbrow0);
        }

        private void adicionaTextViewLinha(TableRow tableRow, TableRow.LayoutParams params, String texto, boolean bold, float textSize){

            TextView textView = new TextView(rootView.getContext());
            textView.setText(texto);

            if(params != null){
                textView.setLayoutParams(params);
            }

            if(textSize != 0){
                textView.setTextSize(textSize);
            }

            if(bold){
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            }

            tableRow.setPadding(10, 0, 10, 0);
            tableRow.addView(textView);

        }

        private void adicionaImageViewLinha(TableRow tableRow, TableRow.LayoutParams params, ImageView view, boolean bold, float textSize){
            tableRow.addView(view);
        }

    }

}

