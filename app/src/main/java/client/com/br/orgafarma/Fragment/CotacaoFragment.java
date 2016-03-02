package client.com.br.orgafarma.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rey.material.widget.CheckBox;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import BO.VendasBO;
import Constantes.Constants;
import adapter.CotacaoAdapter;
import application.OrgafarmaApplication;
import client.com.br.orgafarma.Modal.Cotacao;
import client.com.br.orgafarma.Modal.CotacaoRepresentante;
import client.com.br.orgafarma.Modal.ItemCotacao;
import client.com.br.orgafarma.Modal.Produto;
import client.com.br.orgafarma.Modal.TodosClientes;
import client.com.br.orgafarma.R;
import helperClass.ExpandableListView;
import helperClass.SharedPref;
import helperClass.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CotacaoFragment extends Fragment {

    // --- Views
    private ExpandableListView mListProduto;
    private ExpandableListView mListValor;
    private ExpandableListView mListQtd;
    private EditText mQtd;
    private EditText mValor;
    private AutoCompleteTextView mDescricao;
    private AutoCompleteTextView mClienteNome;
    private View mView;
    private Button mAddItem;
    private Button mConcluir;
    private TextView mTotalValor;
    private TextView mTotalQtd;
    private CheckBox mAceitaGenerico;
    private ImageView mRemoveDescr;

    // --- Adapters
    private CotacaoAdapter mAdapterProduto;
    private CotacaoAdapter mAdapterQtd;
    private CotacaoAdapter mAdapterValor;

    // --- Fields
    private Cotacao mCotacao;
    private List<ItemCotacao> mCotacoesEnvio = new ArrayList<>();
    private CotacaoRepresentante mCotacaoRepresentante;
    private boolean mIsTransferenciaOk;
    private TodosClientes mClientes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cotacao, container, false);

        if (Utils.checkConnection(getContext())) {
            new LoadingAsync(getActivity(), mView).execute();
            new BuscarClientes(getActivity(), mView).execute();
        }
        return mView;
    }

    private void initViews(){
        mListProduto = (ExpandableListView) mView.findViewById(R.id.list_produto);
        mAdapterProduto = new CotacaoAdapter(getContext(), new ArrayList(), Constants.PRODUTO);
        mListProduto.setAdapter(mAdapterProduto);
        setLists(mListProduto, getString(R.string.produto));

        mListValor = (ExpandableListView) mView.findViewById(R.id.list_valor);
        mAdapterValor = new CotacaoAdapter(getContext(), new ArrayList(), Constants.VALOR);
        mListValor.setAdapter(mAdapterValor);
        setLists(mListValor, getString(R.string.valor));

        mListQtd = (ExpandableListView) mView.findViewById(R.id.list_qtd);
        mAdapterQtd = new CotacaoAdapter(getContext(), new ArrayList(), new CotacaoAdapter.deleteInterface() {
            @Override
            public void deleteItem(int index) {
                deleteItemOnLists(index);
            }
        }, Constants.QTD);
        mListQtd.setAdapter(mAdapterQtd);
        setLists(mListQtd, getString(R.string.qtd));

        mQtd = (EditText) mView.findViewById(R.id.quantidade_edit);
        mValor = (EditText) mView.findViewById(R.id.valor_edit);

        mClienteNome = (AutoCompleteTextView) mView.findViewById(R.id.cliente_edit);
        addValuesToClientNome();
        mClienteNome.setThreshold(1);
        mClienteNome.setDropDownBackgroundResource(R.drawable.white_background);


        mDescricao = (AutoCompleteTextView) mView.findViewById(R.id.descricao_edit);
        mDescricao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mValor.requestFocus();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mDescricao.setThreshold(1);
        mDescricao.setDropDownBackgroundResource(R.drawable.white_background);
        addValuesToDescricao();
        mDescricao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mAdapterProduto.contains(s.toString())) {
                    mValor.setEnabled(false);
                } else {
                    mValor.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mRemoveDescr = (ImageView) mView.findViewById(R.id.remove_txt);
        mRemoveDescr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDescricao.setText("");
            }
        });

        mTotalValor = (TextView) mView.findViewById(R.id.vlrTotal);
        mTotalQtd = (TextView) mView.findViewById(R.id.qtdTotal);

        mAddItem = (Button)  mView.findViewById(R.id.add_item);

        View add = mView.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtd = 0;
                if (!mQtd.getText().toString().isEmpty()){
                    qtd = Integer.parseInt(mQtd.getText().toString());
                    if(qtd >= 10000) {
                        mQtd.setText("0");
                    } else {
                        mQtd.setText(++qtd + "");
                    }
                }
                else {
                    mQtd.setText("1");
                }
            }
        });

        View remove = mView.findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mQtd.getText().toString().isEmpty()) {
                    int qtd = Integer.parseInt(mQtd.getText().toString());
                    if (qtd <= 0){
                        mQtd.setText("10000");
                    } else if(qtd > 10000){
                        mQtd.setText("0");
                    } else {
                        mQtd.setText(--qtd + "");
                    }
                } else {
                    mQtd.setText("10000");
                }
            }
        });
        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValor.setEnabled(false);
                AddItem();
            }
        });

        mAceitaGenerico = (CheckBox) mView.findViewById(R.id.AceitaGenerico);
        mAceitaGenerico.setChecked(Whoswinning());
        mConcluir = (Button) mView.findViewById(R.id.concluir_btn);
        mConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                concluir();
            }
        });
    }

    private void deleteItemOnLists(int index){
        ItemCotacao aux = mCotacoesEnvio.get(index);
        mCotacoesEnvio.remove(index);
        if (aux.getmProduto().equals(mDescricao.getText().toString())){
            mValor.setEnabled(true);
        }

        ItemCotacao excluido = CotacaoAdapter.deleteItem(index);
        int qtd = excluido.getmQtd();
        double valor = excluido.getmValor();
        double valorTotal = qtd * valor;

        double qtdPassada = Double.parseDouble(mTotalQtd.getText().toString());
        double valorPassado = Double.parseDouble(mTotalValor.getText().toString());

        double qtdAtual = qtdPassada - qtd;
        double valorAtual = valorPassado - valorTotal;

        mTotalQtd.setText(qtdAtual + "");
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        mTotalValor.setText(numberFormat.format(valorAtual));
        notifyDataSetChangedAll();
    }

    private String getProdutoCodigo(String descricao){
        List<Produto> aux = mCotacao.getmProduto();
        for (Produto i : aux){
            if (i.getmDescricao().equals(descricao)){
                return i.getmCodigo();
            }
        }
        return null;
    }

    private void AddItem(){                                                  // refatorar este metodo dps
        if (!mClienteNome.getText().toString().isEmpty()) {
            if (!mDescricao.getText().toString().isEmpty()) {
                String produto = mDescricao.getText().toString();
                String cliente = mClienteNome.getText().toString();

                if (getClientes().contains(cliente)){
                    if (getProdutos().contains(produto)) {
                        AIAceitaGenerico(mAceitaGenerico.isChecked());
                        int qtd = (mQtd.getText().toString().isEmpty()) ? 0 : Integer.parseInt(mQtd.getText().toString());
                        double valor = (mValor.getText().toString().isEmpty()) ? 0 : Double.parseDouble(mValor.getText().toString());
                        boolean isGenerico = mAceitaGenerico.isChecked();
                        ItemCotacao envio = new ItemCotacao(getProdutoCodigo(produto), produto, valor, qtd, isGenerico);

                        if (!mAdapterProduto.contains(envio)) {
                            CotacaoAdapter.addItemParaTodos(envio);
                            sumTotal(mTotalQtd, qtd);
                            sumTotal(mTotalValor, valor * qtd);
                            mCotacoesEnvio.add(envio);
                            notifyDataSetChangedAll();
                        } else {
                            int index = mAdapterProduto.getItemIndex(envio);
                            int newQtd = mAdapterQtd.sumQtd(index, qtd);
                            envio.setmQtd(newQtd);
                            sumTotal(mTotalQtd, qtd);
                            sumTotal(mTotalValor, valor * qtd);
                            mCotacoesEnvio.set(index, envio);
                        }
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(mView, getContext().getResources().getText(R.string.produto_nao_cadastrado), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar
                            .make(mView, getContext().getResources().getText(R.string.cliente_nao_cadastrado), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar
                        .make(mView, getContext().getResources().getText(R.string.descricao_vazio), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        } else {
            Snackbar snackbar = Snackbar
                    .make(mView, getContext().getResources().getText(R.string.cliente_vazio), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private void sumTotal(TextView view, double vlr){
        double current = Double.parseDouble(view.getText().toString());
        double total = current + vlr;
        view.setText(total + "");
    }

    private void addValuesToDescricao(){
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, getProdutos());
        mDescricao.setAdapter(adapter);
    }

    private void addValuesToClientNome(){
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, getClientes());
        mClienteNome.setAdapter(adapter);
    }

    private List<String> getProdutos(){
        List<String> produtos = new ArrayList<>();
        for (int i = 0; i < mCotacao.getmProduto().size(); i++){
            produtos.add(mCotacao.getmProduto().get(i).getmDescricao());
        }
        return produtos;
    }

    private List<String> getClientes(){
        List<String> clientes = new ArrayList<>();
        for (int i = 0; i < mClientes.getClientes().size(); i++){
            clientes.add(mClientes.getClientes().get(i).getClienteNome());
        }
        return clientes;
    }


    private class LoadingAsync extends AsyncTask<Void, Void, Cotacao> {
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
        protected Cotacao doInBackground(Void... params) {
            Gson gson = new Gson();
            try {
                mCotacao = gson.fromJson(new JSONObject(VendasBO.listaProdutoCotacao()).toString(), Cotacao.class);
            } catch (JSONException e) {
                Log.i("ERRO", e.getMessage());
                return null;
            }
            return mCotacao;
        }

        @Override
        protected void onPostExecute(Cotacao cotacao) {
            progressDialog.dismiss();
        }
    }

    private class SendCotacaoAsync extends AsyncTask<Void, Void, Cotacao> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        private Context mContext;
        private View rootView;

        public SendCotacaoAsync(Context context, View rootView) {
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
        protected Cotacao doInBackground(Void... params) {
            Gson gson = new Gson();
            mIsTransferenciaOk = true;
            try {
                String cotacao = VendasBO.sendCotacoes(gson.toJson(mCotacaoRepresentante));
                if (cotacao == null){
                    mIsTransferenciaOk = false;
                }
            } catch (Exception e) {
                Log.i("ERRO", e.getMessage());
                return null;
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    respostaSendCotacao();
                }
            });

            return mCotacao;
        }

        @Override
        protected void onPostExecute(Cotacao cotacao) {
            progressDialog.dismiss();
        }
    }

    private class BuscarClientes extends AsyncTask<Void, Void, Cotacao> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        private Context mContext;
        private View rootView;

        public BuscarClientes(Context context, View rootView) {
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
        protected Cotacao doInBackground(Void... params) {
            Gson gson = new Gson();
            try {
                mClientes =  gson.fromJson(new JSONObject(VendasBO.buscarClientes(OrgafarmaApplication.TOKEN, OrgafarmaApplication.REPRESENTANTE_CODIGO)).toString(), TodosClientes.class);
            } catch (Exception e) {
                Log.i("ERRO", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Cotacao cotacao) {
            initViews();
            progressDialog.dismiss();
        }
    }

    private void setLists(ExpandableListView list, String title){
        list.setExpanded(true);
        list.setDividerHeight(0);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.cotacao_header_list, null);
        TextView titleTxt = (TextView) view.findViewById(R.id.title);
        titleTxt.setText(title);
        list.addHeaderView(view);
    }

    private void notifyDataSetChangedAll(){
        mAdapterProduto.notifyDataSetChanged();
        mAdapterQtd.notifyDataSetChanged();
        mAdapterValor.notifyDataSetChanged();
    }

    private void concluir(){
        if (Utils.checkConnection(getContext())) {
            mCotacaoRepresentante = new CotacaoRepresentante(
                    mClienteNome.getText().toString(),
                    createCodigo(),
                    getData(),
                    OrgafarmaApplication.REPRESENTANTE_CODIGO,
                    Double.parseDouble(mTotalValor.getText().toString()),
                    Double.parseDouble(mTotalQtd.getText().toString()),
                    mCotacoesEnvio
            );
            new SendCotacaoAsync(getContext(), mView).execute();
        }
    }

    private void respostaSendCotacao(){
        if (mIsTransferenciaOk) {
            Snackbar snackbar = Snackbar
                    .make(mView, getContext().getResources().getText(R.string.cotacao_realizada), Snackbar.LENGTH_LONG);
            snackbar.show();

            //views
            mDescricao.setText("");
            mQtd.setText("");
            mValor.setText("");
            mAceitaGenerico.setChecked(Whoswinning());
            mTotalValor.setText("0");
            mTotalQtd.setText("0");

            //adapters
            mAdapterQtd.clearCotacao();
            mAdapterValor.clearCotacao();
            mAdapterProduto.clearCotacao();

            //field
            mCotacoesEnvio.clear();

            mCotacaoRepresentante = null;
            mDescricao.requestFocus();
        } else {
            Snackbar snackbar = Snackbar
                    .make(mView, getContext().getResources().getText(R.string.cotacao_falha), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private String createCodigo(){
        String hour = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
        String codigo = hour + OrgafarmaApplication.REPRESENTANTE_ID;
        return codigo;
    }

    private String getData(){
        return new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
    }

    //artificial inteligence
    private void AIAceitaGenerico(boolean isAceito) {
        int qtd_sim = SharedPref.getInt(getContext(), "aceitaGenerico");
        int qtd_n = SharedPref.getInt(getContext(), "n_aceitaGenerico");

        if (qtd_n == 5 && qtd_sim == 5){
            qtd_n = 0;
            qtd_sim = 0;
        }

        if (isAceito) {
            if (qtd_sim == 5) {
                SharedPref.setInt(getContext(), "aceitaGenerico", 5);
            } else {
                SharedPref.setInt(getContext(), "aceitaGenerico", ++qtd_sim);
            }

        } else {
            if (qtd_n == 5) {
                    SharedPref.setInt(getContext(), "n_aceitaGenerico", 5);
                } else {
                    SharedPref.setInt(getContext(), "n_aceitaGenerico", ++qtd_n);
                }
            }
    }

    private boolean Whoswinning(){
        int aceita = SharedPref.getInt(getContext(), "aceitaGenerico");
        int nAceita = SharedPref.getInt(getContext(), "n_aceitaGenerico");
        return aceita > nAceita;
    }
}
