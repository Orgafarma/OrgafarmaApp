package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rodolfo.rezende on 01/03/2016.
 */
public class ListItemVerCotacaoEspecifico implements Serializable{
    private static final long serialVersionUID = 43L;

    @SerializedName("itensDacotacao")
    private List<ItemVerCotacaoEspecifico> mItens;

    private int indexOfCotacoes;

    public int getIndexOfCotacoes() {
        return indexOfCotacoes;
    }

    public void setIndexOfCotacoes(int indexOfCotacoes) {
        this.indexOfCotacoes = indexOfCotacoes;
    }

    public List<ItemVerCotacaoEspecifico> getmItens() {
        return mItens;
    }

    public void setmItens(List<ItemVerCotacaoEspecifico> mItens) {
        this.mItens = mItens;
    }
}
