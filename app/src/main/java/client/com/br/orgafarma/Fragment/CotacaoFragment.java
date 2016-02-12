package client.com.br.orgafarma.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rey.material.widget.CheckBox;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import BO.VendasBO;
import Constantes.Constants;
import adapter.CotacaoAdapter;
import client.com.br.orgafarma.Modal.Cotacao;
import client.com.br.orgafarma.Modal.CotacaoEnvio;
import client.com.br.orgafarma.R;
import helperClass.ExpandableListView;

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
    private View mView;
    private Button mAddItem;
    private Button mConcluir;
    private TextView mTotalValor;
    private TextView mTotalQtd;
    private CheckBox mAceitaGenerico;

    // --- Adapters
    private CotacaoAdapter mAdapterProduto;
    private CotacaoAdapter mAdapterQtd;
    private CotacaoAdapter mAdapterValor;

    // --- Fields
    private Cotacao mCotacao;
    private List<CotacaoEnvio> mCotacoesEnvio = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cotacao, container, false);
        new LoadingAsync(getActivity(), mView).execute();
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
        mDescricao = (AutoCompleteTextView) mView.findViewById(R.id.descricao_edit);
        mDescricao.setThreshold(1);
        mDescricao.setDropDownBackgroundResource(R.drawable.white_background);
        addValuesToDescricao();
        mDescricao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mAdapterProduto.contains(s.toString())){
                    mValor.setEnabled(false);
                } else {
                    mValor.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
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
        mConcluir = (Button) mView.findViewById(R.id.concluir_btn);
        mConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                concluir();
            }
        });
    }

    private void deleteItemOnLists(int index){
        mCotacoesEnvio.remove(index);
        CotacaoEnvio excluido = CotacaoAdapter.deleteItem(index);
        int qtd = excluido.getmQtd();
        int valor = excluido.getmValor();
        int valorTotal = qtd * valor;

        int qtdPassada = Integer.parseInt(mTotalQtd.getText().toString());
        int valorPassado = Integer.parseInt(mTotalValor.getText().toString());

        int qtdAtual = qtdPassada - qtd;
        int valorAtual = valorPassado - valorTotal;

        mTotalQtd.setText(qtdAtual + "");
        mTotalValor.setText(valorAtual + "");
        notifyDataSetChangedAll();
    }

    private void AddItem(){
        if (!mDescricao.getText().toString().isEmpty()) {
            String produto = mDescricao.getText().toString();
            int qtd = (mQtd.getText().toString().isEmpty()) ? 0 : Integer.parseInt(mQtd.getText().toString());
            int valor = (mValor.getText().toString().isEmpty()) ? 0 : Integer.parseInt(mValor.getText().toString());
            boolean isGenerico = mAceitaGenerico.isChecked();
            CotacaoEnvio envio = new CotacaoEnvio(produto, valor, qtd, isGenerico);

            if (!mAdapterProduto.contains(envio)) {
                CotacaoAdapter.addItemParaTodos(envio);
                sumTotal(mTotalQtd, qtd);
                sumTotal(mTotalValor, valor * qtd);
                mCotacoesEnvio.add(0, envio);
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
                    .make(mView, getContext().getResources().getText(R.string.campo_vazio), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private void sumTotal(TextView view, int vlr){
        int current = Integer.parseInt(view.getText().toString());
        int total = current + vlr;
        view.setText(total + "");
    }

    private void addValuesToDescricao(){
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, getProdutos());
        mDescricao.setAdapter(adapter);
    }

    private List<String> getProdutos(){
        List<String> produtos = new ArrayList<>();
        for (int i = 0; i < mCotacao.getmProduto().size(); i++){
            produtos.add(mCotacao.getmProduto().get(i).getmDescricao());
        }
        return produtos;
    }


    private class LoadingAsync extends AsyncTask<Void, Void, Cotacao> {

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
        protected Cotacao doInBackground(Void... params) {
            Gson gson = new Gson();
            try {
                mCotacao = gson.fromJson( new JSONObject(VendasBO.listaProdutoCotacao()).toString(), Cotacao.class );
            } catch (JSONException e) {
                Log.i("ERRO", e.getMessage());
                return  null;
            }
            return mCotacao;
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
        mCotacoesEnvio.get(0);
    }
}
