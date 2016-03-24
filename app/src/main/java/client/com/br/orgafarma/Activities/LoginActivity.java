package client.com.br.orgafarma.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.ksoap2.SoapFault;

import BO.LoginBO;
import Constantes.SharePreferenceCons;
import Dominio.ValidacaoLogin;
import Util.MensagemUtil;
import application.OrgafarmaApplication;
import client.com.br.orgafarma.R;
import helperClass.Utils;

/**
 * Created by Felipe on 16/11/2015.
 */
public class LoginActivity extends BaseActivity {

    // --  primitives
    private String username, password;
    private Boolean saveLogin;

    // --  Objects
    private LoginBO loginBO;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;


    // --  Views
    private Button teste;
    private EditText edtlogin;
    private EditText edtSenha;
    private CheckBox saveLoginCheckBox;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void logar(View view) {
        if (Utils.checkConnection(this)) {
            new LodingAsync().execute();
        } else {
            Intent intent = new Intent(this, NoConnection.class);
            startActivity(intent);
            finish();
        }
    }

    private void initViews(){
        edtlogin = (EditText) findViewById(R.id.edt_usuario);
        edtSenha = (EditText) findViewById(R.id.edt_senha);

        saveLoginCheckBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin == true) {
            OrgafarmaApplication.TOKEN = loginPreferences.getString(SharePreferenceCons.Login.TOKEN, "");
            OrgafarmaApplication.REPRESENTANTE_ID = loginPreferences.getString(SharePreferenceCons.Login.REPRESENTANTE_ID, "");
            OrgafarmaApplication.REPRESENTANTE_CODIGO = loginPreferences.getString(SharePreferenceCons.Login.REPRESENTANTE_COD, "");
            OrgafarmaApplication.NOME_REPRESENTANTE = loginPreferences.getString(SharePreferenceCons.Login.NOME_REPRESENTANTE, "");
            OrgafarmaApplication.EMPRESA_NOME = loginPreferences.getString(SharePreferenceCons.Login.EMPRESA_NOME, "");
            OrgafarmaApplication.EMAIL = loginPreferences.getString(SharePreferenceCons.Login.EMAIL, "");

            Intent i = new Intent(LoginActivity.this, ActivityPrincipal.class);
            startActivity(i);
            finish();
        }
    }
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://client.com.br.orgafarma/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://client.com.br.orgafarma/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class LodingAsync extends AsyncTask<Void, Void, ValidacaoLogin> {
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        String usuario = "";
        String senha = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            usuario = edtlogin.getText().toString();
            senha = edtSenha.getText().toString();
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
                showMessageErro(findViewById(R.id.root), soapFault);
            } catch (RuntimeException e) {
                showMessageErro(findViewById(R.id.root), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ValidacaoLogin validacao) {
            progressDialog.dismiss();
            try {
                if (validacao.isValido()) {
                    if (saveLoginCheckBox.isChecked()) {
                        username = edtlogin.getText().toString();
                        password = edtSenha.getText().toString();
                        loginPrefsEditor.putBoolean(SharePreferenceCons.Login.SAVE_LOGIN, true);
                        loginPrefsEditor.putString(SharePreferenceCons.Login.LOGIN, username);
                        loginPrefsEditor.putString(SharePreferenceCons.Login.SENHA, password);
                        loginPrefsEditor.putString(SharePreferenceCons.Login.TOKEN, validacao.getToken());
                        loginPrefsEditor.putString(SharePreferenceCons.Login.REPRESENTANTE_ID, validacao.getIdRepresentante() + "");
                        loginPrefsEditor.putString(SharePreferenceCons.Login.REPRESENTANTE_COD, validacao.getCodRepresentante() + "");
                        loginPrefsEditor.putString(SharePreferenceCons.Login.NOME_REPRESENTANTE, validacao.getNomeRepresentate());
                        loginPrefsEditor.putString(SharePreferenceCons.Login.EMPRESA_NOME, validacao.getEmpresaNome());
                        loginPrefsEditor.putString(SharePreferenceCons.Login.EMAIL, validacao.getEmail());
                        OrgafarmaApplication.TOKEN = validacao.getToken();
                        loginPrefsEditor.commit();
                    } else {
                        OrgafarmaApplication.TOKEN = validacao.getToken();
                        OrgafarmaApplication.REPRESENTANTE_CODIGO = validacao.getCodRepresentante() + "";
                        OrgafarmaApplication.REPRESENTANTE_ID = validacao.getIdRepresentante() + "";
                        OrgafarmaApplication.NOME_REPRESENTANTE = validacao.getNomeRepresentate();
                        OrgafarmaApplication.EMPRESA_NOME = validacao.getEmpresaNome();
                        OrgafarmaApplication.EMAIL = validacao.getEmail();

                        loginPrefsEditor.putString(SharePreferenceCons.Login.REPRESENTANTE_ID, validacao.getIdRepresentante() + "");
                        loginPrefsEditor.putString(SharePreferenceCons.Login.REPRESENTANTE_COD, validacao.getCodRepresentante() + "");
                        loginPrefsEditor.putString(SharePreferenceCons.Login.NOME_REPRESENTANTE, validacao.getNomeRepresentate() + "");
                        loginPrefsEditor.putString(SharePreferenceCons.Login.EMPRESA_NOME, validacao.getEmpresaNome() + "");
                        loginPrefsEditor.putString(SharePreferenceCons.Login.EMAIL, validacao.getEmail() + "");

                        loginPrefsEditor.commit();
                    }
                    Intent i = new Intent(LoginActivity.this, ActivityPrincipal.class);
                    i.putExtra("msg", validacao.getMensagem());
                    startActivity(i);
                    finish();

                } else {
                    MensagemUtil.addMsg(LoginActivity.this, validacao.getMensagem());

                    if (usuario.isEmpty()) {
                        edtlogin.setError("Preencher o campo");
                    } else if (senha.isEmpty()) {
                        edtSenha.setError("Preencher o campo");
                    } else if (usuario.isEmpty() && senha.isEmpty()) {
                        edtlogin.setError("Preencher o campo");
                        edtSenha.setError("Preencher o campo");
                    }
                }
            } catch (NullPointerException ex) {
                showMessageErro(findViewById(R.id.root), ex);
            } catch (Exception ex) {
                showMessageErro(findViewById(R.id.root), ex);
            }
        }
    }
}
