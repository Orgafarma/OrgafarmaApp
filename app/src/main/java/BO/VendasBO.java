package BO;

import Util.WebServiceUtil;

/**
 * Created by Felipe on 20/11/2015.
 */
public class VendasBO {

    public static String listaPrevisaoVenda(){
        return WebServiceUtil.listaPrevisaoVenda();
    }

    public static String listaVendaVendedorTelevendas(){
        return WebServiceUtil.listaVendaVendedorTelevendas();
    }
}
