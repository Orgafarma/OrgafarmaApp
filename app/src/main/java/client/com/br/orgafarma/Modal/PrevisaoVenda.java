package client.com.br.orgafarma.Modal;

import java.util.List;

/**
 * Created by Felipe on 20/11/2015.
 */
public class PrevisaoVenda {

    private Vendedor vendedor;

    private List<VendaMes> listaVendaMes;

    private List<VendaMes> listaVendaMesLogistica;

    private Positivacao positivacao;

    public Positivacao getPositivacao() {
        return positivacao;
    }

    public void setPositivacao(Positivacao positivacao) {
        this.positivacao = positivacao;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public List<VendaMes> getListaVendaMes() {
        return listaVendaMes;
    }

    public void setListaVendaMes(List<VendaMes> listaVendaMes) {
        this.listaVendaMes = listaVendaMes;
    }

    public List<VendaMes> getListaVendaMesLogistica() {
        return listaVendaMesLogistica;
    }

    public void setListaVendaMesLogistica(List<VendaMes> listaVendaMesLogistica) {
        this.listaVendaMesLogistica = listaVendaMesLogistica;
    }
}
