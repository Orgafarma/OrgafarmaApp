package Util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import Constantes.Constants;
import Constantes.SharePreferenceCons;
import Dominio.ValidacaoLogin;
import application.OrgafarmaApplication;
import helperClass.SharedPref;

/**
 * Created by Felipe on 19/11/2015.
 */
public class WebServiceUtil {

    private static final String SOAP_ACTION = "urn:hellowsdl#hello";
    private static final String NAMESPACE = "urn:hellowsdl";
    private static final String URL = "http://10.1.0.136/webservice.php?wsdl";
    private static String METHOD_NAME;

    public static ValidacaoLogin loginWebService(String usuario, String senha) throws SoapFault {

        ValidacaoLogin validacao = new ValidacaoLogin();

        PropertyInfo user = new PropertyInfo();
        PropertyInfo password = new PropertyInfo();

        user.setName("usuario");
        user.setValue(usuario);
        user.setType(String.class);

        password.setName("senha");
        password.setValue(senha);
        password.setType(String.class);

        METHOD_NAME = "loginWebservice";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty(user);
        request.addProperty(password);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call(SOAP_ACTION, envelope);
            Gson gson = new Gson();
            validacao =  gson.fromJson(envelope.getResponse().toString(), ValidacaoLogin.class);
        } catch (IOException e) {
            Gson gson = new Gson();
            validacao =  gson.fromJson(envelope.getResponse().toString(), ValidacaoLogin.class);
            return validacao;

        } catch (XmlPullParserException e) {
            Gson gson = new Gson();
            validacao =  gson.fromJson(envelope.getResponse().toString(), ValidacaoLogin.class);
            return validacao;
        }

        return validacao;
    }

    public static String listaPrevisaoVenda(Context ctx){
        METHOD_NAME = "previsaoVenda";

        PropertyInfo infToken = createProperty(SharePreferenceCons.Login.TOKEN, OrgafarmaApplication.TOKEN);
        PropertyInfo infInicioDate = createProperty(Constants.DATA_INICIO, getInicioMes());
        PropertyInfo infFimDate = createProperty(Constants.DATA_FINAL, getFimMesAtual());
        PropertyInfo infRepresentId = createProperty(SharePreferenceCons.Login.REPRESENTANTE_ID, OrgafarmaApplication.REPRESENTANTE_ID);
        PropertyInfo infRepresentCod = createProperty(SharePreferenceCons.Login.REPRESENTANTE_COD, OrgafarmaApplication.REPRESENTANTE_CODIGO);

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty(infToken);
        request.addProperty(infInicioDate);
        request.addProperty(infFimDate);
        request.addProperty(infRepresentId);
        request.addProperty(infRepresentCod);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object response = envelope.getResponse();
            return response.toString();
        }

        catch (IOException e) {
            return null;
        }

        catch (XmlPullParserException e) {
            return null;
        }
    }

    public static String listaVendaVendedorTelevendas(Context ctx) {
        METHOD_NAME = "vendaVendedorTelevendas";
        PropertyInfo infToken = createProperty(SharePreferenceCons.Login.TOKEN, OrgafarmaApplication.TOKEN);
        PropertyInfo infInicioDate = createProperty(Constants.DATA_INICIO, getInicioMes());
        PropertyInfo infFimDate = createProperty(Constants.DATA_FINAL, getFimMesAtual());
        PropertyInfo infRepresentId = createProperty(SharePreferenceCons.Login.REPRESENTANTE_ID, OrgafarmaApplication.REPRESENTANTE_ID);
        PropertyInfo infRepresentCod = createProperty(SharePreferenceCons.Login.REPRESENTANTE_COD, OrgafarmaApplication.REPRESENTANTE_CODIGO);

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty(infToken);
        request.addProperty(infInicioDate);
        request.addProperty(infFimDate);
        request.addProperty(infRepresentId);
        request.addProperty(infRepresentCod);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object response = envelope.getResponse();
            return response.toString();
        }

        catch (IOException e) {
            return null;
        }

        catch (XmlPullParserException e) {
            return null;
        }
    }

    public static String getProdutoCotacao(){
        METHOD_NAME = "consultaProdutos";
        PropertyInfo infToken = new PropertyInfo();
        infToken.setName(SharePreferenceCons.Login.TOKEN);
        infToken.setValue(OrgafarmaApplication.TOKEN);
        infToken.setType(String.class);

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty(infToken);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object response = envelope.getResponse();
            return response.toString();
        }

        catch (IOException e) {
            return null;
        }

        catch (XmlPullParserException e) {
            return null;
        }
    }

    public static String sendCotacao(String jsonCotacoes){
        METHOD_NAME = "registraCotacao";
        PropertyInfo infToken = createProperty("Cotacoes", jsonCotacoes);

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty(infToken);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object response = envelope.getResponse();
            return response.toString();
        } catch (IOException e) {
            return null;
        } catch (XmlPullParserException e) {
            return null;
        }
    }

    public static String buscarCotacoes(String jsonCotacoes){
        METHOD_NAME = "buscarCotacoes";
        PropertyInfo infToken = createProperty("BuscaCotacoes", jsonCotacoes);

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty(infToken);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object response = envelope.getResponse();
            return response.toString();
        } catch (IOException e) {
            return null;
        } catch (XmlPullParserException e) {
            return null;
        }

    }

    public static String buscarClientes(String token, String representanteCod){
        METHOD_NAME = "buscarClientes";

        PropertyInfo infToken = createProperty("token", token);
        PropertyInfo infRepreCod = createProperty("representante_cod", representanteCod);

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty(infToken);
        request.addProperty(infRepreCod);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object response = envelope.getResponse();
            return response.toString();
        } catch (IOException e) {
            return null;
        } catch (XmlPullParserException e) {
            return null;
        }
    }

    public static String buscarCotacaoEspecifico(String token, String codCotacao){
        METHOD_NAME = "buscarCotacaoEspecifico";

        PropertyInfo infToken = createProperty("token", token);
        PropertyInfo infRepreCod = createProperty("codigo_cotacao", codCotacao);

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty(infToken);
        request.addProperty(infRepreCod);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object response = envelope.getResponse();
            return response.toString();
        } catch (IOException e) {
            return null;
        } catch (XmlPullParserException e) {
            return null;
        }
    }

    private static PropertyInfo createProperty(String NAME, String value){
        PropertyInfo propertyInf = new PropertyInfo();
        propertyInf.setName(NAME);
        propertyInf.setValue(value);
        propertyInf.setType(String.class);
        return propertyInf;
    }

    private static String getFimMesAtual(){
        Calendar c = Calendar.getInstance();
        int firstDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        return getDate(c.getTime(), firstDay + "");
    }

    private static String getInicioMes(){
        return getDate(new Date(), "1");
    }

    private static String getDate(Date today, String replacer){
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String aux = simpleDate.format(today);
        String[] aux2 = aux.split("-");
        aux = aux2[0] + "-" + aux2[1] + "-" + replacer;
        return aux;
    }
}
