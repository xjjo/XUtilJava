package com.xcustom.commerce.claro.decidir;

import com.decidir.sdk.Decidir;
import com.decidir.sdk.dto.DecidirResponse;
import com.decidir.sdk.dto.payments.PaymentResponse;
import com.decidir.sdk.exceptions.DecidirException;

public class test {

    /**
     * @param args
     */
    public static void main(String[] args) {
	String privateApiKey = "d0561688c40f4f6faee1f6dfd5451f50";//Private API Key habilitada para operar en ambiente Sandbox
	String urlSandbox = "https://developers.decidir.com/api/v2/";
	int timeout = 10; // 10 segundos de timeout
	//Ejemplo para el ambiente Sandbox
	Decidir decidir = new Decidir(privateApiKey, urlSandbox, timeout);

	long idPago = 803673L; //ID devuelto por la operacion de pago (NO CONFUNDIR con site_transaction_id asignado por el comercio)
	try {
		DecidirResponse<PaymentResponse> pago = decidir.getPayment(idPago);
		System.out.println("wait");
		pago.getResult().getStatus().ordinal();
		// Procesamiento de respuesta de consulta de pago
		// ...codigo...
	} catch (DecidirException de) {
		// Manejo de excepcion  de Decidir
		 // ...codigo...
	} catch (Exception e) {
		 //Manejo de excepcion general
		// ...codigo...
	}
    }

}
