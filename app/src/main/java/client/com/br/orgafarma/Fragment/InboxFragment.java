package client.com.br.orgafarma.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import BO.VendasBO;
import client.com.br.orgafarma.Activities.ActivityPrincipal;
import client.com.br.orgafarma.Modal.Logistica;
import client.com.br.orgafarma.Modal.Mes;
import client.com.br.orgafarma.Modal.PrevisaoVenda;
import client.com.br.orgafarma.R;
import helperClass.Utils;


public class InboxFragment extends BaseFragment {
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_inbox, container, false);
        initViews();
        ((ActivityPrincipal) getActivity()).getSupportActionBar().setTitle("VendaMes do Mês");
        new LoadingAsync(getActivity(), mView).execute();
        return mView;
    }

    private void initViews(){
        TextView mes = (TextView) mView.findViewById(R.id.mes);
        mes.setText("Mês: " + Utils.getCurrentMonthName());
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
            Gson gson = new Gson();
            PrevisaoVenda previsaoVenda;

            try {
                previsaoVenda = gson.fromJson( new JSONObject(VendasBO.listaPrevisaoVenda(getContext())).toString(), PrevisaoVenda.class );
            } catch (JSONException e) {
                Log.i("ERRO", e.getMessage());
                return  null;
            } catch (Exception ex){
                showMessageErro(mView, ex);
                return null;
            }

            return previsaoVenda;
        }

        @Override
        protected void onPostExecute(PrevisaoVenda previsaoVenda) {

            double totalMeta = 0;
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
                adicionaTextViewLinha(tableRowPos, params, previsaoVenda.getVenda().getPositivados().get(0).getBaseClientes(), false, 0);
                adicionaTextViewLinha(tableRowPos,params, previsaoVenda.getVenda().getPositivados().get(0).getMeta(), false, 0);
                adicionaTextViewLinha(tableRowPos,params, previsaoVenda.getVenda().getPositivados().get(0).getPositivados(), false, 0);
                adicionaTextViewLinha(tableRowPos,params, previsaoVenda.getVenda().getPositivados().get(0).getCob(), false, 0);
                adicionaTextViewLinha(tableRowPos,params, previsaoVenda.getVenda().getPositivados().get(0).getProjecao(), false, 0);
                adicionaTextViewLinha(tableRowPos, params, previsaoVenda.getVenda().getPositivados().get(0).getVazio(), false, 0);

                stkPositivacao.addView(tableRowPos);

                TextView txtNomeVendedor = (TextView) rootView.findViewById(R.id.txtVendedor);
                txtNomeVendedor.setText(previsaoVenda.getVenda().getVendedor());


                List<Mes> meses =  previsaoVenda.getVenda().getMes();
                for (int i = 0; i < meses.size(); i++) {
                    Mes mes =  meses.get(i);

                    params.setMargins(0, 10, 0, 0);
                    TableRow tbrow = new TableRow(mContext);;

                    adicionaTextViewLinha(tbrow,params, mes.getPacote(), false, 0);
                    adicionaTextViewLinha(tbrow, params, mes.getMeta(), false, 0);
                    adicionaTextViewLinha(tbrow,params, mes.getVenda(), false, 0);
                    adicionaTextViewLinha(tbrow,params,mes.getCob(), false, 0);
                    adicionaTextViewLinha(tbrow, params, mes.getProjecao(), false, 0);

                    ImageView view = new ImageView(mContext);

                    double vazio = Double.parseDouble(mes.getVazio());

                    if(vazio <= 100.00) {
                        view.setImageResource(R.drawable.status_fail);
                    }else {
                        view.setImageResource(R.drawable.status_ok);
                    }

                    adicionaImageViewLinha(tbrow, params, view, false, 0);

                    adicionaTextViewLinha(tbrow, params, mes.getVazio() + "%", false, 0);


                    totalMeta +=  Integer.parseInt(mes.getMeta());
                    totalVenda +=  Double.parseDouble(mes.getVenda());
                    totalCOB +=  Double.parseDouble(mes.getCob());
                    totalProjecao +=  Double.parseDouble(mes.getProjecao());
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

                total = df.format(totalCOB);
                total = total.replace(",", ".");
                totalCOB = Double.parseDouble(total);

                adicionaTotaisVendaDireta(params,totalMeta,totalVenda,totalCOB,totalProjecao,totalVazio,stk,"VendaMes Direta");

                totalMeta = 0;
                totalVenda = 0;
                totalCOB = 0;
                totalProjecao = 0;
                totalVazio = 0;


                List<Logistica> logisticas = previsaoVenda.getVenda().getLogistica();
                for (int i = 0; i < logisticas.size(); i++) {
                    Logistica log = logisticas.get(i);

                    params.setMargins(0, 10, 0, 0);
                    TableRow tbrow = new TableRow(mContext);

                    adicionaTextViewLinha(tbrow, params, log.getPacote(), false, 0);
                    adicionaTextViewLinha(tbrow,params, log.getMeta(), false, 0);
                    adicionaTextViewLinha(tbrow,params, log.getVenda(), false, 0);
                    adicionaTextViewLinha(tbrow,params, log.getCob(), false, 0);
                    adicionaTextViewLinha(tbrow,params, log.getProjecao(), false, 0);
                    adicionaTextViewLinha(tbrow,params, log.getVazio(), false, 0);

                    totalMeta += Double.parseDouble(log.getMeta());
                    totalVenda +=  Double.parseDouble(log.getVenda());
                    totalCOB += Double.parseDouble(log.getCob());
                    totalProjecao += Double.parseDouble(log.getProjecao());
                    totalVazio += Double.parseDouble(log.getVazio());

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
            adicionaTextViewLinha(tbrow0, params, "VendaMes", false, 16f);
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

        private void adicionaTotaisVendaDireta(TableRow.LayoutParams params, double totalMeta, double totalVenda,
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

