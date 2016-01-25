package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import client.com.br.orgafarma.R;

/**
 * Created by rodolfo.rezende on 20/01/2016.
 */
public class CotacaoAdapter extends BaseAdapter{
    private List<String> mItens;
    private Context mCtx;

    public CotacaoAdapter(Context mCtx, List<String> mItens){
        this.mCtx = mCtx;
        this.mItens = mItens;
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
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(mCtx).inflate(R.layout.itens_cotacao_adapter, null);
        TextView itemName = (TextView) view.findViewById(R.id.item_name);
        itemName.setText(mItens.get(position));
        return view;
    }
}
