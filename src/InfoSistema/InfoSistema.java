package InfoSistema;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.SigarException;

public class InfoSistema {
    private Sigar sigar = new Sigar();
    
    public void imprimirInfo() throws SigarException {
        CpuInfo[] infos = null;
        CpuPerc[] cpus = null;
        try {
            infos = sigar.getCpuInfoList();
        } catch (SigarException e) {
            e.printStackTrace();
        }
        CpuInfo info = infos[0];
        OperatingSystem sys = OperatingSystem.getInstance();
        System.out.println("Sistema Operativo:\t" + sys.getDescription());
        System.out.println("Fabricante CPU:\t\t" + info.getVendor()+info.getModel());
        System.out.println("Total CPUs:\t\t" + info.getTotalCores());
        try {
            System.out.println("Consumo Total CPUs:\t" + CpuPerc.format(sigar.getCpuPerc().getUser()));
        } catch (SigarException e) {
            e.printStackTrace();
        }
        Mem memoria = sigar.getMem();
        Swap intercambio = sigar.getSwap();
        System.out.println("Memoria RAM Total:\t"+enMegaBytes(memoria.getTotal())+" MB");
        System.out.println("Memoria RAM Usada:\t"+enMegaBytes(memoria.getUsed())+" MB");
        System.out.println("Memoria SWAP Usada:\t"+enMegaBytes(intercambio.getUsed())+" MB");
    }
    private Long enMegaBytes(long valor) {
        return new Long(valor / (1024*1024));
    }
    
    public static void main(String[] args) {
        try {
            new InfoSistema().imprimirInfo();
        } catch (SigarException e) {
            e.printStackTrace();
        }
    }
}