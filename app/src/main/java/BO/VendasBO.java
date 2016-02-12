package BO;

import android.content.Context;

import Util.WebServiceUtil;

/**
 * Created by Felipe on 20/11/2015.
 */
public class VendasBO {

    public static String listaPrevisaoVenda(Context ctx){
        return WebServiceUtil.listaPrevisaoVenda(ctx);
    }

    public static String listaVendaVendedorTelevendas(Context ctx){
        return WebServiceUtil.listaVendaVendedorTelevendas(ctx);
    }

    public static String listaProdutoCotacao(){
        return WebServiceUtil.getProdutoCotacao();
    }
}
