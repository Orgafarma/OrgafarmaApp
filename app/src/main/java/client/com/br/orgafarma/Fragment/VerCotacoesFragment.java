package client.com.br.orgafarma.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import BO.VendasBO;
import adapter.VerCotacaoAdapter;
import application.OrgafarmaApplication;
import client.com.br.orgafarma.Modal.BuscarCotacao;
import client.com.br.orgafarma.Modal.ItemVerCotacao;
import client.com.br.orgafarma.Modal.TodosItemVerCotacao;
import client.com.br.orgafarma.R;
import helperClass.ExpandableListView;
import helperClass.Mask;

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

        mOpenDatePickerInicial = (ImageView) mView.findViewById(R.id.dataInicialPicker);
        mOpenDatePickerInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.Builder builder;
                builder = new DatePickerDialog.Builder(R.style.Material_App_Dialog_DatePicker_Light){
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        DatePickerDialog dialog = (DatePickerDialog)fragment.getDialog();
                        mDataInicio.setText(dialog.getFormattedDate(new SimpleDateFormat("dd/MM/yyyy")));
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
        mDataFim.addTextChangedListener(Mask.insert("##/##/####", mDataFim));
        initSpinner();
        mBuscar = (Button) mView.findViewById(R.id.buscar);
        mBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar();
            }
        });
    }

    private void initSpinner(){
        mSituacao = (Spinner) mView.findViewById(R.id.situacao);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.row_spn, new String[]{"Todos", "Pendente", "Finalizados"});
        adapter2.setDropDownViewResource(R.layout.row_spn_dropdown);
        mSituacao.setAdapter(adapter2);
    }

    private void initListView(){
        mLvCotacoes = (ExpandableListView) mView.findViewById(R.id.cotacoes);
        VerCotacaoAdapter adapter = new VerCotacaoAdapter(mItemVerCotacoes.getItens(), getActivity().getApplicationContext(), new VerCotacaoAdapter.VerCotacao() {
            @Override
            public void Mais() {
                //showDialogCotacao();
            }

            @Override
            public void Delete() {
            }
        });
        mLvCotacoes.setAdapter(adapter);
        mLvCotacoes.setExpanded(true);
        mLvCotacoes.addHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.item_ver_cotacao_header, null));
    }

   /* private void showDialogCotacao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_ver_cotacao, null);
        VerCotacaoAdapter adapter = new VerCotacaoAdapter(getItens(), getActivity().getApplicationContext(), null);
        ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.cotacoes_dialog);
        listView.setExpanded(true);
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.item_ver_cotacao_header, null);
        //headerView.findViewById(R.id.analise).setVisibility(View.GONE);
        listView.addHeaderView(headerView);
        listView.setAdapter(adapter);
        builder.setView(view);
        builder.setPositiveButton("OK", null);
        builder.show();
    }*/

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
            }
            return mItemVerCotacoes;
        }

        @Override
        protected void onPostExecute(TodosItemVerCotacao cotacao) {
            initListView();
            progressDialog.dismiss();
        }
    }

    private void buscar(){
        String dataInicio = mDataInicio.getText().toString();
        String dataFim = mDataFim.getText().toString();
        String status = mSituacao.getSelectedItemPosition() + "";
        String codRepresentante = OrgafarmaApplication.REPRESENTANTE_CODIGO;
        mCurrentBusca = new BuscarCotacao(codRepresentante, dataInicio, dataFim, status);
        new LoadingAsync(getContext(), mView).execute();
    }

}
