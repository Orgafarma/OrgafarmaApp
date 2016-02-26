package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rodolfo.rezende on 26/02/2016.
 */
public class TodosItemVerCotacao implements Serializable {
    @SerializedName("cotacoes")
    private List<ItemVerCotacao> itens;

    public List<ItemVerCotacao> getItens() {
        return itens;
    }

    public void setItens(List<ItemVerCotacao> itens) {
        this.itens = itens;
    }
}
