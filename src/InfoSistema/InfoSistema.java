package InfoSistema;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class InfoSistema {
    private Sigar sigar = new Sigar();
    public String[] infoSOalt=new String[2];
    public String[] infoSO=new String[5];
    
    public String[] InfoSOalt() throws SigarException {
        Mem memoria = sigar.getMem();
        infoSOalt[0]=CpuPerc.format(sigar.getCpuPerc().getUser()); //Consumo CPUs %
        infoSOalt[0]=infoSOalt[0].substring(0, infoSOalt[0].length()-1); 
        infoSOalt[1]=enMegaBytes(memoria.getUsed()).toString(); //Consumo RAM (MB)
        return infoSOalt;
    }
    
    public String[] InfoSO() throws SigarException {
        CpuInfo[] infos = null;
        Mem memoria = sigar.getMem();
        OperatingSystem sys = OperatingSystem.getInstance();
        try {
            infos = sigar.getCpuInfoList();
        } catch (SigarException e) {
        }
        CpuInfo info = infos[0];
        infoSO[0]=sys.getDescription(); //Sistema Operativo
        infoSO[1]=info.getVendor()+" "+info.getModel(); //Info CPU
        infoSO[2]=info.getMhz()+""; //Velocidad CPU (MGz)
        infoSO[3]=info.getTotalCores()+""; //Cantidad CPU
        infoSO[4]=enMegaBytes(memoria.getTotal())+""; //RAM Total (MB)
        return infoSO;
    }
    
    private Long enMegaBytes(long valor) {
        return (valor / (1024*1024));
    }
//    
//    public static void main(String[] args) {
//        InfoSistema info=new InfoSistema();
//        try {
//            info.InfoSO();
//            info.InfoSOalt();
//        } catch (SigarException ex) {
//            Logger.getLogger(InfoSistema.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println(info.infoSOalt[0]+"\n"+info.infoSOalt[1]);
//        System.out.println(info.infoSO[0]+"\n"+info.infoSO[1]+"\n"+info.infoSO[2]+"\n"+info.infoSO[3]+"\n"+info.infoSO[4]);
//    }
}