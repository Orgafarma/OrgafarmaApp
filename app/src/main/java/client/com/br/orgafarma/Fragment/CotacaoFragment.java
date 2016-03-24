package client.com.br.orgafarma.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import client.com.br.orgafarma.Activities.NoConnection;
import client.com.br.orgafarma.Modal.BuscarCliente;
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
public class CotacaoFragment extends BaseFragment {
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
    private ImageView mRemoveCliente;

    // --- Adapters
    private CotacaoAdapter mAdapterProduto;
    private CotacaoAdapter mAdapterQtd;
    private CotacaoAdapter mAdapterValor;

    // --- Fields
    private Cotacao mCotacao;
    private List<ItemCotacao> mCotacoesEnvio = new ArrayList<>();
    private CotacaoRepresentante mCotacaoRepresentante;
    private TodosClientes mClientes;
    private android.os.Handler repeatUpdateHandler = new android.os.Handler();

    // -- primitive types
    private boolean mIsTransferenciaOk;
    private boolean mAutoIncrement;
    private boolean mAutoDecrement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cotacao, container, false);
        if (Utils.checkConnection(getContext())) {
            new buscarProdutos(getActivity(), mView).execute();
            new BuscarClientes(getActivity(), mView).execute();
        } else {
            Intent intent = new Intent(getActivity(), NoConnection.class);
            startActivity(intent);
        }
        return mView;
    }

    private void initViews(){
        try {
            mListProduto = (ExpandableListView) mView.findViewById(R.id.list_produto);
            mAdapterProduto = new CotacaoAdapter(getContext(), new ArrayList(), Constants.PRODUTO);
            setLists(mListProduto, getString(R.string.produto));
            mListProduto.setAdapter(mAdapterProduto);

            mListValor = (ExpandableListView) mView.findViewById(R.id.list_valor);
            mAdapterValor = new CotacaoAdapter(getContext(), new ArrayList(), Constants.VALOR);
            setLists(mListValor, getString(R.string.valor));
            mListValor.setAdapter(mAdapterValor);

            mListQtd = (ExpandableListView) mView.findViewById(R.id.list_qtd);
            mAdapterQtd = new CotacaoAdapter(getContext(), new ArrayList(), new CotacaoAdapter.deleteInterface() {
                @Override
                public void deleteItem(int index) {
                    deleteItemOnLists(index);
                }
            }, Constants.QTD);
            setLists(mListQtd, getString(R.string.qtd));
            mListQtd.setAdapter(mAdapterQtd);

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
                    mDescricao.requestFocus();
                }
            });

            mRemoveCliente = (ImageView) mView.findViewById(R.id.remove_cliente_txt);
            mRemoveCliente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClienteNome.setText("");
                    mClienteNome.requestFocus();
                }
            });

            mTotalValor = (TextView) mView.findViewById(R.id.vlrTotal);
            mTotalQtd = (TextView) mView.findViewById(R.id.qtdTotal);

            mAddItem = (Button) mView.findViewById(R.id.add_item);

            View add = mView.findViewById(R.id.add);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    increment();
                }
            });
            add.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mAutoIncrement = true;
                    repeatUpdateHandler.post(new RptUpdater());
                    return false;
                }
            });

            add.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                            && mAutoIncrement) {
                        mAutoIncrement = false;
                    }
                    return false;
                }
            });

            View remove = mView.findViewById(R.id.remove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decrement();
                }
            });
            remove.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mAutoDecrement = true;
                    repeatUpdateHandler.post(new RptUpdater());
                    return false;
                }
            });
            remove.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL)
                            && mAutoDecrement ){
                        mAutoDecrement = false;
                    }
                    return false;
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
                    if (verifyInputs()){
                        concluir();
                    }
                }
            });
        } catch (final Exception ex){
            Snackbar snackbar = Snackbar
                    .make(mView, getContext().getResources().getText(R.string.prob_config), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private boolean verifyInputs(){
        return mAdapterProduto.getCount() > 0;
    }

    private void deleteItemOnLists(int index){
        ItemCotacao aux = mCotacoesEnvio.get(index);
        mCotacoesEnvio.remove(index);
        if (aux.getmProduto().equals(mDescricao.getText().toString())){
            mValor.setEnabled(true);
        }
        if (mCotacoesEnvio.size() == 0){
            mClienteNome.setEnabled(true);
            mRemoveCliente.setEnabled(true);
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

    private void AddItem() {
        if (!mClienteNome.getText().toString().isEmpty()) {
            if (!mDescricao.getText().toString().isEmpty()) {
                String produto = mDescricao.getText().toString();
                String cliente = mClienteNome.getText().toString();

                if (getClientes().contains(cliente)){
                    try {
                        if (getProdutos().contains(produto)) {
                            mRemoveCliente.setEnabled(false);
                            mClienteNome.setEnabled(false);

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
                    } catch (Exception e) {
                        e.printStackTrace();
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
        double current = Double.parseDouble(view.getText().toString().replace(",","."));
        double total = current + vlr;
        view.setText(total + "");
    }

    private void addValuesToDescricao() throws Exception {
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

    private List<String> getProdutos() throws Exception{

        if (mClientes != null) {
            List<String> produtos = new ArrayList<>();
            for (int i = 0; i < mCotacao.getmProduto().size(); i++) {
                produtos.add(mCotacao.getmProduto().get(i).getmDescricao());
            }
            return produtos;
        }
        return new ArrayList<String>();
    }

    private List<String> getClientes(){
        if (mClientes != null) {
            List<String> clientes = new ArrayList<>();
            for (int i = 0; i < mClientes.getClientes().size(); i++) {
                clientes.add(mClientes.getClientes().get(i).getClienteNome());
            }
            return clientes;
        }
        return new ArrayList<String>();
    }


    private class buscarProdutos extends AsyncTask<Void, Void, Cotacao> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        private Context mContext;
        private View rootView;

        public buscarProdutos(Context context, View rootView) {
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
                showMessageErro(mView, e);
                return null;
            } catch(Exception ex){
                showMessageErro(mView, ex);
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
                showMessageErro(mView, e);
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
            if (mIsTransferenciaOk){
                mRemoveCliente.setEnabled(true);
                mClienteNome.setEnabled(true);
            }
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
                showMessageErro(mView, e);
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
        if (list.getHeaderViewsCount() < 1) {
            list.addHeaderView(view);
        }
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
                    getCodigoCliente(mClienteNome.getText().toString()),
                    createCodigo(),
                    getData(),
                    OrgafarmaApplication.REPRESENTANTE_CODIGO,
                    Double.parseDouble(mTotalValor.getText().toString()),
                    Double.parseDouble(mTotalQtd.getText().toString()),
                    mCotacoesEnvio
            );
            new SendCotacaoAsync(getContext(), mView).execute();
        } else {
            Snackbar snackbar = Snackbar
                    .make(mView, getContext().getResources().getText(R.string.sem_conexao), Snackbar.LENGTH_LONG);
            snackbar.show();
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
            mClienteNome.setText("");

            //adapters
            mAdapterQtd.clearCotacao();
            mAdapterValor.clearCotacao();
            mAdapterProduto.clearCotacao();

            //field
            mCotacoesEnvio.clear();

            mCotacaoRepresentante = null;
            mClienteNome.requestFocus();
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

    private String getCodigoCliente(String nameClient){
        for (BuscarCliente i : mClientes.getClientes()) {
            if (i.getClienteNome().equals(nameClient)) {
                return i.getCodCliente();
            }
        }
        return "";
    }

    //artificial inteligence
    private void AIAceitaGenerico(boolean isAceito) {
        int QTD_CONSTANTE = 3;

        int qtd_sim = SharedPref.getInt(getContext(), "aceitaGenerico");
        int qtd_n = SharedPref.getInt(getContext(), "n_aceitaGenerico");

        if (qtd_n == QTD_CONSTANTE && qtd_sim == QTD_CONSTANTE){
            qtd_n = 0;
            qtd_sim = 0;
        }

        if (isAceito) {
            if (qtd_sim == QTD_CONSTANTE) {
                SharedPref.setInt(getContext(), "aceitaGenerico", QTD_CONSTANTE);
            } else {
                SharedPref.setInt(getContext(), "aceitaGenerico", ++qtd_sim);
            }

        } else {
            if (qtd_n == QTD_CONSTANTE) {
                    SharedPref.setInt(getContext(), "n_aceitaGenerico", QTD_CONSTANTE);
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

    private void decrement(){
        Utils.hideSoftKeyBoard(getActivity());
        mQtd.requestFocus();
        if (!mQtd.getText().toString().isEmpty()) {
            int qtd = Integer.parseInt(mQtd.getText().toString());
            if (qtd <= 0) {
                mQtd.setText("10000");
            } else if (qtd > 10000) {
                mQtd.setText("0");
            } else {
                mQtd.setText(--qtd + "");
            }
        } else {
            mQtd.setText("10000");
        }
    }

    private void increment(){
        Utils.hideSoftKeyBoard(getActivity());
        mQtd.requestFocus();
        int qtd = 0;
        if (!mQtd.getText().toString().isEmpty()) {
            qtd = Integer.parseInt(mQtd.getText().toString());
            if (qtd >= 10000) {
                mQtd.setText("0");
            } else {
                mQtd.setText(++qtd + "");
            }
        } else {
            mQtd.setText("1");
        }
    }

    class RptUpdater implements Runnable {
        public void run() {
            if( mAutoIncrement ){
                increment();
                repeatUpdateHandler.postDelayed( new RptUpdater(), 50 );
            } else if( mAutoDecrement ){
                decrement();
                repeatUpdateHandler.postDelayed( new RptUpdater(), 50 );
            }
        }
    }
}
