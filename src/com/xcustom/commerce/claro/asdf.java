package com.xcustom.commerce.claro;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class asdf {

    /**
     * @param args
     */
    public static void main(String[] args) {
	System.out.println(Long.valueOf(("3025.0".replace(".", "")))); 
	//System.out.println(Long.valueOf((""+Float.parseFloat("3025.0"))*100));
	
	Double num = new Double(151.81701168189804);
	
	    String FORMAT =  "#####.00";
		String monthlyInterest;
		 DecimalFormat decimalFormat;
		
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
		formatSymbols.setDecimalSeparator('.');
		formatSymbols.setGroupingSeparator(','); 
		decimalFormat =  new DecimalFormat(FORMAT, formatSymbols);
		
		
		
		System.out.println(decimalFormat.format(Float.parseFloat("302121235013.")).replace(".", ""));
		
		
    }

}
