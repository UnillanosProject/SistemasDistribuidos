/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Servidor;

/**
 *
 * @author Arango Abello
 */
public class Asignacion {
    private boolean asignado;
    private int inI,inJ;

    public Asignacion(int inI, int inJ) {
        this.inI = inI;
        this.inJ = inJ;
        asignado=false;
    }

    public boolean isAsignado() {
        return asignado;
    }

    public void setAsignado(boolean asignado) {
        this.asignado = asignado;
    }
    
    public int getInI() {
        return inI;
    }

    public void setInI(int inI) {
        this.inI = inI;
    }

    public int getInJ() {
        return inJ;
    }

    public void setInJ(int inJ) {
        this.inJ = inJ;
    }
    
    
}
