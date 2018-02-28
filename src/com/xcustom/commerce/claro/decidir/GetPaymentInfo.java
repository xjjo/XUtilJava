package com.xcustom.commerce.claro.decidir;

import com.decidir.sdk.Decidir;
import com.decidir.sdk.dto.DecidirResponse;
import com.decidir.sdk.dto.payments.PaymentResponse;
import com.decidir.sdk.exceptions.DecidirException;

public class GetPaymentInfo {

    /**
     * @param args
     */
    public static void main(String[] args) {
	// ...codigo...
	String privateApiKey = "92b71cf711ca41f78362a7134f87ff65";//Private API Key habilitada para operar en ambiente Sandbox
	//String urlSandbox = "https://developers.decidir.com/api/v2/";
	String urlSandbox = "http://localhost:8079/api/v2/info/";
	int timeout = 10; // 10 segundos de timeout
	//Ejemplo para el ambiente Sandbox
	Decidir decidir = new Decidir(privateApiKey, urlSandbox, timeout);

	long idPago = 000123L; //ID devuelto por la operacion de pago (NO CONFUNDIR con site_transaction_id asignado por el comercio)
	try {
		DecidirResponse<PaymentResponse> pago = decidir.getPayment(idPago);
		
	} catch (DecidirException de) {
	    de.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	// ...codigo...

    }

}
///api/v2/info/