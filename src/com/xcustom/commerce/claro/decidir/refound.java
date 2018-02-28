package com.xcustom.commerce.claro.decidir;

import com.decidir.sdk.Decidir;
import com.decidir.sdk.dto.DecidirResponse;
import com.decidir.sdk.dto.refunds.RefundPayment;
import com.decidir.sdk.dto.refunds.RefundPaymentResponse;
import com.decidir.sdk.exceptions.DecidirException;
import com.decidir.sdk.exceptions.responses.RefundException;

public class refound {

    /**
     * @param args
     */
    public static void main(String[] args) {
	String privateApiKey = "d0561688c40f4f6faee1f6dfd5451f50";//Private API Key habilitada para operar en ambiente Sandbox
	String urlSandbox = "http://localhost:8079/api/v1/";
	//String urlSandbox = "https://developers.decidir.com/api/v2/";
	int timeout = 10; // 10 segundos de timeout
	//Ejemplo para el ambiente Sandbox
	Decidir decidir = new Decidir(privateApiKey, urlSandbox, timeout);
	long idPago = 839835L; //ID devuelto por la operacion de pago (NO CONFUNDIR con site_transaction_id asignado por el comercio)
	String usuario = "usuario_que_realiza_la_accion"; //Usuario habilitado para realizar la anulacion/devolucion. Se utiliza para matener un registro de quien realiza la operacion
	RefundPayment refundPayment = new RefundPayment(); //Se instancia sin datos
	refundPayment.setAmount(100L);
	try {
		DecidirResponse<RefundPaymentResponse> devolucion = decidir.refundPayment(idPago, refundPayment, usuario);
		devolucion.getMessage();
		// Procesamiento de respuesta de la devolucion de pago
		// ...codigo...
	} catch (RefundException re) {
	    // Manejo de excepcion en devolucion
	    re.printStackTrace();
	} catch (DecidirException de) {
	    System.out.println(de.getMessage());
	    System.out.println(de.getStatus());
	    de.printStackTrace();
		// Manejo de excepcion  de Decidir
		// ...codigo...
	} catch (Exception e) {
	    e.printStackTrace();
		//Manejo de excepcion general
		// ...codigo...
	}

    }

}
