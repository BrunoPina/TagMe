/*
 * Created on 22/12/2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package br.com.tagme.commons.utils;


/**
 * @author root
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ValidadorCpfCnpj {
    private String message;
    private String number;
    private int    typenumber; // 1 - CPF 2 - CNPJ
    
    public static final int CPF = 1;
    public static final int CNPJ = 2;

    public ValidadorCpfCnpj(){}

    public ValidadorCpfCnpj(String value){
    	this();
    	number = value;
    	if(number != null){
    		typenumber = number.length() == 14 ? CNPJ : CPF;
    	}
    }
    	
    public String getMessage() {
        return message;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setTypenumber(int type) {
        typenumber = type;
    }

    public int getTypenumber() {
        return typenumber;
    }

    public boolean isValid() {
        int soma = 0;

        message = "";

        try {
            Long.parseLong(number);
        } catch(Exception e) {
            message = "Somente numeros s�o permitidos";

            return false;
        }

        if(typenumber == 1) { // CPF

            if(number.length() == 11) {
                for(int i = 0; i < 9; i++)
                    soma += ((10 - i) * (number.charAt(i) - '0'));

                soma = 11 - (soma % 11);

                if(soma > 9) {
                    soma = 0;
                }

                if(soma == (number.charAt(9) - '0')) {
                    soma = 0;

                    for(int i = 0; i < 10; i++)
                        soma += ((11 - i) * (number.charAt(i) - '0'));

                    soma = 11 - (soma % 11);

                    if(soma > 9) {
                        soma = 0;
                    }

                    if(soma == (number.charAt(10) - '0')) {
                        message = "CPF V�lido";

                        return true;
                    }
                }
            }

            message = "CPF Inválido";
        } else if(typenumber == 2) { // CNPJ

            if(number.length() == 14) {
                for(int i = 0, j = 5; i < 12; i++) {
                    soma += (j-- * (number.charAt(i) - '0'));

                    if(j < 2) {
                        j = 9;
                    }
                }

                soma = 11 - (soma % 11);

                if(soma > 9) {
                    soma = 0;
                }

                if(soma == (number.charAt(12) - '0')) {
                    soma = 0;

                    for(int i = 0, j = 6; i < 13; i++) {
                        soma += (j-- * (number.charAt(i) - '0'));

                        if(j < 2) {
                            j = 9;
                        }
                    }

                    soma = 11 - (soma % 11);

                    if(soma > 9) {
                        soma = 0;
                    }

                    if(soma == (number.charAt(13) - '0')) {
                        message = "CNPJ V�lido";

                        return true;
                    }
                }
            }

            message = "CNPJ Inválido";
        }

        return false;
    }

}
