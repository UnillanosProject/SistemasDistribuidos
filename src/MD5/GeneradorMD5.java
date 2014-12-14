package MD5;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneradorMD5 {
    
    private String md5(String clave) {
        MessageDigest claveMd5 = null;
        try {
            claveMd5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(GeneradorMD5.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public static void main(String[] args) {
        GeneradorMD5 obj=new GeneradorMD5();
        System.out.println(obj.md5("ju94"));
    }
}
