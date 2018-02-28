package com.xcustom.commerce.claro.decidir;

import java.util.ArrayList;
import java.util.List;

import com.decidir.sdk.Decidir;
import com.decidir.sdk.dto.DecidirResponse;
import com.decidir.sdk.dto.payments.Currency;
import com.decidir.sdk.dto.payments.Customer;
import com.decidir.sdk.dto.payments.PaymentRequest;
import com.decidir.sdk.dto.payments.PaymentResponse;
import com.decidir.sdk.dto.payments.PaymentType;
import com.decidir.sdk.dto.payments.SubPayment;
import com.decidir.sdk.exceptions.DecidirException;
import com.decidir.sdk.exceptions.responses.PaymentException;

public class executePayment {

    /**
     * @param args
     */
    public static void main(String[] args) {
	// ...codigo...
	String privateApiKey = "92b71cf711ca41f78362a7134f87ff65";//Private API Key habilitada para operar en ambiente Sandbox
	String urlSandbox = "http://localhost:8079/api/v2/";
	//https://developers.decidir.com/api/v2/
	int timeout = 10; // 10 segundos de timeout
	//Ejemplo para el ambiente Sandbox
	Decidir decidir = new Decidir(privateApiKey, urlSandbox, timeout);
	Customer customer = new Customer();
	customer.setId("test"); // user_id
	customer.setEmail("test@decidir.com"); // user_email

	PaymentRequest paymentRequest = new PaymentRequest();
	paymentRequest.setToken("ae9fc3e5-ff41-4de2-9c91-81030be1c4a6"); // token de pago
	paymentRequest.setSite_transaction_id("TX00001234"); //ID de transaccion asignada por el comercio, no puede repetirse
	paymentRequest.setCustomer(customer);
	paymentRequest.setPayment_method_id(1); //VISA
	paymentRequest.setBin("450799");
	paymentRequest.setAmount(23250L);//Valor en centavos: $232.50
	paymentRequest.setCurrency(Currency.ARS);
	paymentRequest.setInstallments(1);
	paymentRequest.setPayment_type(PaymentType.SINGLE); //Tipo de pago simple
	paymentRequest.setFirst_installment_expiration_date("2018-05-15"); // Llenar en caso de una compra con tarjeta NacionPyme
	List<SubPayment> sub_payments = new ArrayList<SubPayment>(); // Llenar en caso de transaccion distribuida por monto
	paymentRequest.setSub_payments(sub_payments); //Debe enviarse una lista vacia

	try {
		DecidirResponse<PaymentResponse> paymentResponse = decidir.payment(paymentRequest);
		PaymentResponse pr  = paymentResponse.getResult();
		pr.getStatus();
	} catch (PaymentException pe) {
	    pe.getResponse();
	    pe.printStackTrace();
		 // Manejo de pago rechazado
		 // ...codigo...
	} catch (DecidirException de) {
	    de.printStackTrace();
		// Manejo de excepcion  de Decidir
		 // ...codigo...
	} catch (Exception e) {
	    e.printStackTrace();
		 //Manejo de excepcion general
		// ...codigo...
	}
	// ...codigo...

    }

}
