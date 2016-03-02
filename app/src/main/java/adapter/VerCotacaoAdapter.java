package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import client.com.br.orgafarma.Modal.ItemVerCotacao;
import client.com.br.orgafarma.R;

/**
 * Created by rodolfo.rezende on 22/02/2016.
 */
public class VerCotacaoAdapter extends BaseAdapter {
    private List<ItemVerCotacao> mItens;
    private Context mCtx;
    private VerCotacao mVerCotacao;

    public interface VerCotacao{
        void Mais(ItemVerCotacao Cotacao, int index);
        void Delete();
    }

    public VerCotacaoAdapter(List<ItemVerCotacao> mItens, Context mCtx, VerCotacao mVerCotacao){
        this.mItens = mItens;
        this.mCtx = mCtx;
        this.mVerCotacao = mVerCotacao;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int PENDENTE = 1;
        final int FINALIZADO = 2;

        final ItemVerCotacao item = mItens.get(position);
        View view = LayoutInflater.from(mCtx).inflate(R.layout.item_ver_cotacao, null);
        TextView data = (TextView) view.findViewById(R.id.data);
        TextView qtdItens = (TextView) view.findViewById(R.id.qtdItens);
        TextView vlrTotal = (TextView) view.findViewById(R.id.vlrTotal);
        TextView status = (TextView) view.findViewById(R.id.status);
        ImageView mais = (ImageView) view.findViewById(R.id.mais);

        data.setText(item.getData());
        qtdItens.setText(item.getQtdItens());
        vlrTotal.setText(item.getVlrTotal());

        switch (Integer.parseInt(item.getStatus())){
            case PENDENTE:
                status.setText(mCtx.getResources().getText(R.string.pendente));
                break;
            case FINALIZADO:
                status.setText(mCtx.getResources().getText(R.string.finalizados));
                break;
            default:
                status.setText(mCtx.getResources().getText(R.string.indefined));
                break;
        }

        if (mVerCotacao == null){
            mais.setVisibility(View.GONE);
        } else {
            mais.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mVerCotacao.Mais(item, position);
                }
            });
        }
        return view;
    }
}
