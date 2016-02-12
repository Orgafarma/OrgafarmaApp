package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import Constantes.Constants;
import client.com.br.orgafarma.Modal.Cotacao;
import client.com.br.orgafarma.Modal.CotacaoEnvio;
import client.com.br.orgafarma.R;

/**
 * Created by rodolfo.rezende on 20/01/2016.
 */
public class CotacaoAdapter extends BaseAdapter{
    private String TYPE;
    private static List<CotacaoEnvio> mItens;
    private Context mCtx;

    private deleteInterface mDeleteInterface;

    public interface deleteInterface{
        void deleteItem(int index);
    }

    public CotacaoAdapter(Context mCtx, List<CotacaoEnvio> mItens){
        this.mCtx = mCtx;
        this.mItens = mItens;
    }

    public CotacaoAdapter(Context mCtx, List<CotacaoEnvio> mItens, deleteInterface mDeleteInterface, String TYPE){
        this.mCtx = mCtx;
        this.mItens = mItens;
        this.TYPE = TYPE;
        this.mDeleteInterface = mDeleteInterface;
    }

    public CotacaoAdapter(Context mCtx, List<CotacaoEnvio> mItens, String TYPE){
        this.mCtx = mCtx;
        this.mItens = mItens;
        this.TYPE = TYPE;
    }


    public void addItem(CotacaoEnvio item){
        mItens.add(0, item);
        notifyDataSetChanged();
    }

    public static CotacaoEnvio deleteItem(int index){
        CotacaoEnvio returner = mItens.remove(index);
        return returner;
    }

    public int sumQtd(int index, int value){
        int currentValue = mItens.get(index).getmQtd();
        int sum = currentValue + value;
        mItens.get(index).setmQtd(sum);
        notifyDataSetChanged();
        return sum;
    }

    public boolean contains(CotacaoEnvio produto){
        return mItens.contains(produto);
    }

    public boolean contains(String produto){
        for (CotacaoEnvio i : mItens){
            if (i.getmProduto().equals(produto)){
                return true;
            }
        }
        return false;
    }

    public int getItemIndex(CotacaoEnvio item){
        if (mItens.contains(item)){
            return mItens.indexOf(item);
        }
        return -1;
    }

    @Override
    public int getCount() {
        return mItens.size();
    }

    @Override
    public Object getItem(int position) {
        return mItens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(mCtx).inflate(R.layout.itens_cotacao_adapter, null);
        TextView itemName = (TextView) view.findViewById(R.id.item_name);

        if (TYPE.equals(Constants.PRODUTO)){
            itemName.setText(mItens.get(position).getmProduto());
            itemName.setGravity(3);
        } else if (TYPE.equals(Constants.QTD)){
            itemName.setText(mItens.get(position).getmQtd() + "");
            view.findViewById(R.id.delete).setVisibility(View.VISIBLE);
            view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDeleteInterface.deleteItem(position);
                }
            });

            if (mItens.get(position).ismIsGenerico()){
                view.findViewById(R.id.generico).setVisibility(View.VISIBLE);
            }
        } else if (TYPE.equals(Constants.VALOR)){
            itemName.setText(mItens.get(position).getmValor() + "");
        }
        return view;
    }

    public static void addItemParaTodos(CotacaoEnvio envio){
        mItens.add(envio);
    }
}
