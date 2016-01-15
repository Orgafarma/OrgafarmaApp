package Util;

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

import Dominio.ValidacaoLogin;

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

    public static String listaPrevisaoVenda(){

        METHOD_NAME = "previsaoVenda";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
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

    public static String listaVendaVendedorTelevendas(){

        METHOD_NAME = "vendaVendedorTelevendas";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
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
}
