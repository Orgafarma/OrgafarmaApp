package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import client.com.br.orgafarma.Modal.ItemVerCotacaoEspecifico;
import client.com.br.orgafarma.R;
import helperClass.Utils;

/**
 * Created by rodolfo.rezende on 25/02/2016.
 */
public class VerCotacaoDetalheAdapter extends BaseAdapter {
    private List<ItemVerCotacaoEspecifico> mItens;
    private Context mCtx;
    private VerCotacao mVerCotacao;

    public interface VerCotacao{
        void Mais();
        void Delete();
    }

    public VerCotacaoDetalheAdapter(List<ItemVerCotacaoEspecifico> mItens, Context mCtx){
        this.mItens = mItens;
        this.mCtx = mCtx;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemVerCotacaoEspecifico item = mItens.get(position);

        View view = LayoutInflater.from(mCtx).inflate(R.layout.dialog_item_ver_cotacao, null);
        TextView codigoProduto = (TextView) view.findViewById(R.id.codigo_ver_item);
        TextView produto = (TextView) view.findViewById(R.id.produto_ver_item);
        TextView qtd = (TextView) view.findViewById(R.id.qtd_ver_item);
        TextView preco = (TextView) view.findViewById(R.id.preco_ver_item);
        TextView total = (TextView) view.findViewById(R.id.Total);

        codigoProduto.setText(item.getCodProduto());
        produto.setText(item.getProduto());
        qtd.setText(item.getQtd());
        preco.setText(Utils.formatarDecimal(item.getPrecoSugerido(), 1));

        double totalSum = Double.parseDouble(preco.getText().toString()) * Double.parseDouble(qtd.getText().toString());
        total.setText(Utils.formatarDecimal(totalSum, 1));

        return view;
    }
}

