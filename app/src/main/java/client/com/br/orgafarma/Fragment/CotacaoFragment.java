package client.com.br.orgafarma.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import adapter.CotacaoAdapter;
import application.OrgafarmaApplication;
import client.com.br.orgafarma.R;
import helperClass.ExpandableListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CotacaoFragment extends Fragment {
    private ExpandableListView mListProduto;
    private ExpandableListView mListValor;
    private ExpandableListView mListQtd;
    private EditText mQtd;
    private EditText mValor;
    private EditText mDescricao;

    private View mView;

    public CotacaoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_cotacao, container, false);
        initViews();
        return mView;
    }

    private void initViews(){
        mListProduto = (ExpandableListView) mView.findViewById(R.id.list_produto);
        mListValor = (ExpandableListView) mView.findViewById(R.id.list_valor);
        mListQtd = (ExpandableListView) mView.findViewById(R.id.list_qtd);
        setLists(mListProduto);
        setLists(mListValor);
        setLists(mListQtd);

        mQtd = (EditText) mView.findViewById(R.id.quantidade_edit);
        mValor = (EditText) mView.findViewById(R.id.valor_edit);
        mDescricao = (EditText) mView.findViewById(R.id.descricao_edit);

        View add = mView.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mQtd.getText().toString().isEmpty()){
                    int qtd = Integer.parseInt(mQtd.getText().toString());
                    mQtd.setText(++qtd + "");
                } else {
                    mQtd.setText("1");
                }
            }
        });
        View remove = mView.findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mQtd.getText().toString().isEmpty()){
                    int qtd = Integer.parseInt(mQtd.getText().toString());
                    mQtd.setText(--qtd + "");
                } else {
                    mQtd.setText("1000");
                }
            }
        });
    }

    private void setLists(ExpandableListView list){
        list.setExpanded(true);
        list.setDividerHeight(0);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.cotacao_header_list, null);
        list.addHeaderView(view);

        ArrayList<String> array = new ArrayList();
        array.add("Dizepan");
        array.add("penicilina");
        array.add("Benzemol");
        array.add("Bicabornato");
        array.add("Dizepan");
        array.add("penicilina");
        array.add("Benzemol");
        array.add("Bicabornato");
        array.add("Dizepan");
        array.add("penicilina");
        array.add("Benzemol");
        array.add("Bicabornato");
        array.add("Dizepan");
        array.add("penicilina");
        array.add("Benzemol");
        array.add("Bicabornato");
        array.add("Dizepan");
        array.add("penicilina");
        array.add("Benzemol");
        array.add("Bicabornato");
        array.add("Dizepan");
        array.add("penicilina");
        array.add("Benzemol");
        array.add("Bicabornato");
        array.add("Dizepan");
        array.add("penicilina");
        array.add("Benzemol");
        array.add("Bicabornato");
        array.add("Dizepan");
        array.add("penicilina");
        array.add("Benzemol");
        array.add("Bicabornato");
        array.add("Dizepan");
        array.add("penicilina");
        array.add("Benzemol");
        array.add("Bicabornato");
        CotacaoAdapter adapter = new CotacaoAdapter(getContext(), array);
        list.setAdapter(adapter);
    }





}
