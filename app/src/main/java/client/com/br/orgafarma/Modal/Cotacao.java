package client.com.br.orgafarma.Modal;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rodolfo.rezende on 29/01/2016.
 */
public class Cotacao implements Serializable{
    @SerializedName("Cotacao")
    private List<Produto> mProduto;

    public List<Produto> getmProduto() {
        return mProduto;
    }

    public void setmProduto(List<Produto> mProduto) {
        this.mProduto = mProduto;
    }
}
