package client.com.br.orgafarma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapFault;

import BO.LoginBO;
import Constantes.SharePreferenceCons;
import Dominio.ValidacaoLogin;
import Util.MensagemUtil;

/**
 * Created by Felipe on 16/11/2015.
 */
public class LoginActivity extends AppCompatActivity {

    private LoginBO loginBO;
    private EditText edtlogin;
    private EditText edtSenha;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private String username,password;
    private Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtlogin = (EditText) findViewById(R.id.edt_usuario);
        edtSenha = (EditText) findViewById(R.id.edt_senha);

        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if(saveLogin == true){
            Intent i = new Intent(LoginActivity.this, ActivityPrincipal.class);
            i.putExtra("msg", "Logado com Sucesso");
            startActivity(i);
            finish();
        }

    }

    public void logar(View view) {
        new LodingAsync().execute();
    }


    private class LodingAsync extends AsyncTask<Void, Void, ValidacaoLogin> {

        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        String usuario = edtlogin.getText().toString();
        String senha = edtSenha.getText().toString();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progressDialog.setMessage("Carregando...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected ValidacaoLogin doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
                loginBO = new LoginBO(LoginActivity.this);
                return loginBO.validarLogin(usuario, senha);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ValidacaoLogin validacao) {

            progressDialog.dismiss();

            if (validacao.isValido()) {
                if(saveLoginCheckBox.isChecked()){
                    username = edtlogin.getText().toString();
                    password = edtSenha.getText().toString();
                    loginPrefsEditor.putBoolean(SharePreferenceCons.Login.SAVE_LOGIN, true);
                    loginPrefsEditor.putString(SharePreferenceCons.Login.LOGIN, username);
                    loginPrefsEditor.putString(SharePreferenceCons.Login.SENHA, password);
                    loginPrefsEditor.putString(SharePreferenceCons.Login.TOKEN, validacao.getToken());
                    loginPrefsEditor.commit();
                }
                else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }

                Intent i = new Intent(LoginActivity.this, ActivityPrincipal.class);
                i.putExtra("msg", validacao.getMensagem());
                startActivity(i);
                finish();

            } else {
                MensagemUtil.addMsg(LoginActivity.this, validacao.getMensagem());

                if(usuario.isEmpty()){
                    edtlogin.setError("Preencher o campo");
                }

                else if(senha.isEmpty()){
                    edtSenha.setError("Preencher o campo");
                }

                else if(usuario.isEmpty() && senha.isEmpty()){
                    edtlogin.setError("Preencher o campo");
                    edtSenha.setError("Preencher o campo");
                }

            }
        }

    }
}
