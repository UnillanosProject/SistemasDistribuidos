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
    private boolean procesado;
    private int inI,inJ;

    public Asignacion(int inI, int inJ) {
        this.inI = inI;
        this.inJ = inJ;
        procesado=false;
    }

    public boolean isProcesado() {
        return procesado;
    }

    public void setProcesado(boolean procesado) {
        this.procesado = procesado;
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
