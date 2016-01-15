package BO;

import android.content.Context;

import org.ksoap2.SoapFault;

import Dominio.ValidacaoLogin;
import Util.WebServiceUtil;
import client.com.br.orgafarma.R;

/**
 * Created by Felipe on 19/11/2015.
 */
public class LoginBO {

    private Context context;

    public LoginBO(Context context) {
        this.context = context;
    }

    public ValidacaoLogin validarLogin(String login, String senha) throws SoapFault {
        ValidacaoLogin validacao = new ValidacaoLogin();

        if ((login == null || login.equals("")) && (senha == null || senha.equals(""))) {
            validacao.setValido(false);
            validacao.setMensagem(context.getString(R.string.msg_login_senha_obg));
        } else if (login == null || login.equals("")) {
            validacao.setValido(false);
            validacao.setMensagem(context.getString(R.string.msg_login_obg));
        } else if (senha == null || senha.equals("")) {
            validacao.setValido(false);
            validacao.setMensagem(context.getString(R.string.msg_senha_obg));

        } else if ((validacao = WebServiceUtil.loginWebService(login, senha)) != null && validacao.isValido()) {
            validacao.setValido(true);
            validacao.setMensagem(context.getString(R.string.msg_login_sucesso));
        } else {
            validacao.setValido(false);
            if (validacao.isErro()) {
                validacao.setMensagem(validacao.getMensagem());
            } else {
                validacao.setMensagem(context.getString(R.string.msg_login_invalido));
            }
        }

        return validacao;
    }

}

