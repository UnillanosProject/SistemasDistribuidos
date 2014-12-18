/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package MD5;

import Cliente.Cliente;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JOptionPane;

/**
 *
 * @author Arango Abello
 */
public class Md5 {
    
    private final String[] caracteres;
    private String claveMd5;
    private Cliente cliente;
    
    public Md5() {
        caracteres=llenarCaracteres();
    }
    public Md5(Cliente cliente) {
        caracteres=llenarCaracteres();
        this.cliente=cliente;
    }
    
    private String[] llenarCaracteres(){
        String[] caracteresAux=new String[37];
        caracteresAux[0]="";
        for (int i = 1; i < caracteresAux.length; i++) {
            if (i>26) {
                caracteresAux[i]=""+(char)('0'+i-27);
            }
            else{
                caracteresAux[i]=""+(char)('a'+i-1);
            }
//            System.out.println(caracteresAux[i]);
            
        }
        return caracteresAux;
    }
    
    
    public void recorrer(int inI,int inJ){
        int inK=0,inL=0,inM=0;
        int procesados=0;
        String actual;
        for (int i = inI; i < inI+1; i++) {
            if (i==1) {
                inJ=1;
            }
            for (int j = inJ; j < inJ+1; j++) {
                if (j==1) {
                inK=1;
                }
                for (int k = inK; k < caracteres.length; k++) {
                    if (k==1) {
                    inL=1;
                    }
                    for (int l = inL; l < caracteres.length; l++) {
                        if (l==1) {
                        inM=1;
                        }
                        for (int m = inM; m < caracteres.length; m++) {
                            for (int n = 1; n < caracteres.length; n++) {
                                actual=caracteres[i]+caracteres[j]+caracteres[k]+caracteres[l]+caracteres[m]+caracteres[n];
                                if (md5(actual).equals(claveMd5)) {
                                    cliente.getHiloConex().avisarClaveEncontrada(actual);
//                                    System.out.println("Clave "+actual);
                                    return;
                                }
                                procesados++;
                                cliente.setProcesados(procesados,actual);
//                                System.out.println("Procesados:"+procesados);
//                                System.out.println(caracteres[i]+caracteres[j]+caracteres[k]+caracteres[l]+caracteres[m]+caracteres[n]);
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    private String md5(String clave) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
        }
        byte[] b = md5.digest(clave.getBytes());

        StringBuilder h = new StringBuilder(b.length);
        for (int i = 0; i < b.length; i++) {
            int u = b[i] & 255;
                if (u < 16) {
                    h.append("0").append(Integer.toHexString(u));
                } 
                else {
                    h.append(Integer.toHexString(u));
                }
        }
        return h.toString();
    }

    public String getClaveMd5() {
        return claveMd5;
    }

    public void setClaveMd5(String claveMd5) {
        this.claveMd5 = claveMd5;
    }

    public String[] getCaracteres() {
        return caracteres;
    }
    
    
}
