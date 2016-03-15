package client.com.br.orgafarma.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import BO.VendasBO;
import adapter.VerCotacaoAdapter;
import adapter.VerCotacaoDetalheAdapter;
import application.OrgafarmaApplication;
import client.com.br.orgafarma.Modal.BuscarCotacao;
import client.com.br.orgafarma.Modal.ItemVerCotacao;
import client.com.br.orgafarma.Modal.ListItemVerCotacaoEspecifico;
import client.com.br.orgafarma.Modal.TodosItemVerCotacao;
import client.com.br.orgafarma.R;
import helperClass.ExpandableListView;
import helperClass.Mask;
import helperClass.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerCotacoesFragment extends Fragment {

    //  ---  Views
    private EditText mDataInicio;
    private EditText mDataFim;
    private Spinner mEmpresa;
    private Spinner mSituacao;
    private ExpandableListView mLvCotacoes;
    private Button mBuscar;
    private View mView;
    private ImageView mOpenDatePickerInicial;
    private ImageView mOpenDatePickerFim;

    // --- Models
    private TodosItemVerCotacao mItemVerCotacoes;
    private BuscarCotacao mCurrentBusca;
    private boolean mIsThereHeader = false;
    private ListItemVerCotacaoEspecifico mItensCotacaoEspecifico;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_ver_cotacoes, container, false);
        initVies();
        return mView;
    }

    private void initVies(){
        mDataInicio = (EditText) mView.findViewById(R.id.data_inicio);
        mDataInicio.addTextChangedListener(Mask.insert("##/##/####", mDataInicio));
        mDataInicio.setText(Utils.getDataFormatted(Utils.getFirstDateOfCurrentMonth()));

        mOpenDatePickerInicial = (ImageView) mView.findViewById(R.id.dataInicialPicker);
        mOpenDatePickerInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.Builder builder;
                builder = new DatePickerDialog.Builder(R.style.Material_App_Dialog_DatePicker_Light){
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        DatePickerDialog dialog = (DatePickerDialog)fragment.getDialog();
                        mDataInicio.setText(Utils.getDataFormatted(dialog.getDate()));
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        super.onNegativeActionClicked(fragment);
                    }
                };

                builder.positiveAction("OK")
                        .negativeAction("CANCELAR");
                DialogFragment fragment = DialogFragment.newInstance(builder);
                fragment.show(getFragmentManager(), null);
            }
        });

        mOpenDatePickerFim = (ImageView) mView.findViewById(R.id.dataFinalPicker);
        mOpenDatePickerFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.Builder builder;
                builder = new DatePickerDialog.Builder(R.style.Material_App_Dialog_DatePicker_Light){
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        DatePickerDialog dialog = (DatePickerDialog)fragment.getDialog();
                        mDataFim.setText(dialog.getFormattedDate(new SimpleDateFormat("dd/MM/yyyy")));
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        super.onNegativeActionClicked(fragment);
                    }
                };

                builder.positiveAction("OK")
                        .negativeAction("CANCELAR");
                DialogFragment fragment = DialogFragment.newInstance(builder);
                fragment.show(getFragmentManager(), null);
            }
        });

        mDataFim = (EditText) mView.findViewById(R.id.data_fim);
        mDataFim.setText(Utils.getDataFormatted(Utils.getLastDateOfCurrentMonth()));

        mDataFim.addTextChangedListener(Mask.insert("##/##/####", mDataFim));
        initSpinner();
        mBuscar = (Button) mView.findViewById(R.id.buscar);
        mBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDataInputs()) {
                    buscar();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(mView, getContext().getResources().getText(R.string.data_n_config), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    private boolean checkDataInputs(){
        String aux = mDataInicio.getText().toString();
        String aux2 = mDataFim.getText().toString();
        boolean dataInicio =  (aux.charAt(2) == '/' && aux.charAt(5) == '/' && aux.length() == 10) ? true : false;
        boolean dataFinal = (aux2.charAt(2) == '/' && aux2.charAt(5) == '/' && aux2.length() == 10) ? true : false;
        if (dataInicio && dataFinal){
            return true;
        }
        return false;
    }

    private void initSpinner(){
        mSituacao = (Spinner) mView.findViewById(R.id.situacao);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.row_spn, new String[]{"Todos", "Pendente", "Finalizados"});
        adapter2.setDropDownViewResource(R.layout.row_spn_dropdown);
        mSituacao.setAdapter(adapter2);
        mSituacao.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                buscar();
            }
        });
    }

    private void initListView(){
        mLvCotacoes = (ExpandableListView) mView.findViewById(R.id.cotacoes);
        VerCotacaoAdapter adapter = new VerCotacaoAdapter(mItemVerCotacoes.getItens(), getActivity().getApplicationContext(), new VerCotacaoAdapter.VerCotacao() {
            @Override
            public void Mais(ItemVerCotacao Cotacao, int index) {
                new LoadCotacaoCodigo(getContext(), mView, Cotacao, index).execute();
            }

            @Override
            public void Delete() {
            }
        });
        mLvCotacoes.setExpanded(true);

        if (!mIsThereHeader) {
            mLvCotacoes.addHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.item_ver_cotacao_header, null));
            mIsThereHeader = true;
        }
        mLvCotacoes.setAdapter(adapter);
    }

    private void showDialogCotacao(){
        final int PENDENTE = 1;
        final int FINALIZADO = 2;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_ver_cotacao, null);

        //   ----header
        TextView codCotacao = (TextView) view.findViewById(R.id.cod_cotacao);
        TextView vlrCotacao = (TextView) view.findViewById(R.id.vlr_cotacao);
        TextView dataPedido = (TextView) view.findViewById(R.id.data_pedido);
        TextView situacao = (TextView) view.findViewById(R.id.situacao);
        TextView usuarioEncerramento = (TextView) view.findViewById(R.id.usuario_encerramento);

        ItemVerCotacao item = mItemVerCotacoes.getItens().get(mItensCotacaoEspecifico.getIndexOfCotacoes());

        codCotacao.setText(item.getCodigoCotacao());
        vlrCotacao.setText(Utils.formatarDecimal(item.getVlrTotal(), 1));
        dataPedido.setText(item.getData());

        switch (Integer.parseInt(item.getStatus())){
            case PENDENTE:
                situacao.setText(getResources().getText(R.string.pendente));
                break;
            case FINALIZADO:
                situacao.setText(getResources().getText(R.string.finalizados));
                break;
            default:
                situacao.setText(getResources().getText(R.string.indefined));
                break;
        }
        //   ----header

        VerCotacaoDetalheAdapter adapter = new VerCotacaoDetalheAdapter(mItensCotacaoEspecifico.getmItens(), getActivity().getApplicationContext());
        ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.cotacoes_dialog);
        listView.setExpanded(true);
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_item_ver_cotacao_header, null);
        listView.addHeaderView(headerView);
        listView.setAdapter(adapter);
        builder.setView(view);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private class LoadingAsync extends AsyncTask<Void, Void, TodosItemVerCotacao> {
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
        protected TodosItemVerCotacao doInBackground(Void... params) {
            Gson gson = new Gson();
            try {
                mItemVerCotacoes =  gson.fromJson(new JSONObject(VendasBO.buscarCotacoes(gson.toJson(mCurrentBusca))).toString(), TodosItemVerCotacao.class);
            } catch (JSONException e) {
                Log.i("ERRO", e.getMessage());
                return null;
            } catch(Exception ex){
                Snackbar snackbar = Snackbar
                        .make(mView, getContext().getResources().getText(R.string.prob_config), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            return mItemVerCotacoes;
        }

        @Override
        protected void onPostExecute(TodosItemVerCotacao cotacao) {
            if (mItemVerCotacoes != null) {
                initListView();
            }
            progressDialog.dismiss();
        }
    }

    private class LoadCotacaoCodigo extends AsyncTask<Void, Void, ListItemVerCotacaoEspecifico> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        private Context mContext;
        private View rootView;
        private ItemVerCotacao codCotacao;
        private int index;

        public LoadCotacaoCodigo(Context context, View rootView, ItemVerCotacao Cotacao, int index) {
            this.mContext = context;
            this.rootView = rootView;
            this.codCotacao = Cotacao;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Carregando...");
            progressDialog.show();
        }

        @Override
        protected ListItemVerCotacaoEspecifico doInBackground(Void... params) {
            Gson gson = new Gson();
            try {
                mItensCotacaoEspecifico =  gson.fromJson(new JSONObject(VendasBO.buscarCotacaoEspecifico(OrgafarmaApplication.TOKEN, codCotacao.getCodigoCotacao())).toString(), ListItemVerCotacaoEspecifico.class);
                mItensCotacaoEspecifico.setIndexOfCotacoes(index);
            } catch (JSONException e) {
                Log.i("ERRO", e.getMessage());
                return null;
            } catch(Exception ex){
                Snackbar snackbar = Snackbar
                        .make(mView, getContext().getResources().getText(R.string.prob_config), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            return mItensCotacaoEspecifico;
        }

        @Override
        protected void onPostExecute(ListItemVerCotacaoEspecifico cotacao) {
            if (mItensCotacaoEspecifico != null) {
                showDialogCotacao();
            } else {
                Snackbar snackbar = Snackbar
                        .make(mView, getContext().getResources().getText(R.string.prob_config), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            progressDialog.dismiss();
        }
    }

    private void buscar() {
        try {
            String dataInicio = mDataInicio.getText().toString();
            String dataFim = mDataFim.getText().toString();
            String status = mSituacao.getSelectedItemPosition() + "";
            String codRepresentante = OrgafarmaApplication.REPRESENTANTE_CODIGO;
            mCurrentBusca = new BuscarCotacao(codRepresentante, dataInicio, dataFim, status);
            new LoadingAsync(getContext(), mView).execute();
        } catch (Exception ex){
            Snackbar snackbar = Snackbar
                    .make(mView, getContext().getResources().getText(R.string.prob_config), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
