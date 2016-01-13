package client.com.br.orgafarma.Modal;

import java.util.List;

/**
 * Created by Felipe on 24/11/2015.
 */
public class PrevisaoVendaVendedorTelevendas {

    private String valorLogistica;

    private List<VendaVendedorTelevendas> listaPrevisaoVendaVendedorTelevendas;

    private PrazoMedio prazoMedio;

    public PrazoMedio getPrazoMedio() {
        return prazoMedio;
    }

    public void setPrazoMedio(PrazoMedio prazoMedio) {
        this.prazoMedio = prazoMedio;
    }

    public String getValorLogistica() {
        return valorLogistica;
    }

    public void setValorLogistica(String valorLogistica) {
        this.valorLogistica = valorLogistica;
    }

    public List<VendaVendedorTelevendas> getListaPrevisaoVendaVendedorTelevendas() {
        return listaPrevisaoVendaVendedorTelevendas;
    }

    public void setListaPrevisaoVendaVendedorTelevendas(List<VendaVendedorTelevendas> listaPrevisaoVendaVendedorTelevendas) {
        this.listaPrevisaoVendaVendedorTelevendas = listaPrevisaoVendaVendedorTelevendas;
    }
}
