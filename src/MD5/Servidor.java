package MD5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JOptionPane;

public class Servidor extends javax.swing.JFrame {

    private String[] caracteres;
    private String clave;
    
    public Servidor() {
        initComponents();
        this.setLocationRelativeTo(null);
        caracteres=llenarCaracteres();
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
            System.out.println(caracteresAux[i]);
            
        }
        return caracteresAux;
    }
    
    
    public String recorrer(){
        int inJ=0,inK=0,inL=0,inM=0;
        int procesados=0;
        String actual="";
        for (int i = 0; i < caracteres.length; i++) {
            if (i==1) {
                inJ=1;
            }
            for (int j = inJ; j < caracteres.length; j++) {
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
                                if (md5(actual).equals(clave)) {
                                    JOptionPane.showMessageDialog(this, "La clave es: "+actual, "Se ha encontrado la clave", JOptionPane.INFORMATION_MESSAGE);
                                    return actual;
                                }
                                procesados++;
                                System.out.println("Procesados:"+procesados);
//                                System.out.println(caracteres[i]+caracteres[j]+caracteres[k]+caracteres[l]+caracteres[m]+caracteres[n]);
                            }
                        }
                    }
                }
            }
        }
        return actual;
    }
    
    private String md5(String clave) {
        MessageDigest claveMd5 = null;
        try {
            claveMd5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
        }
        byte[] b = claveMd5.digest(clave.getBytes());

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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 409, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 314, Short.MAX_VALUE)
        );

        pack();
    }
}
