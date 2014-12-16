/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graficas;

import JFreeChart.DialDemo2;
import JFreeChart.DialDemo3;
import JFreeChart.LineChartDemo6;
import JFreeChart.PieChart3DDemo1;
import JFreeChart.TimeSeriesDemo1;

/**
 *
 * @author Juan
 */
public class Prueba {
    public static void main(String[] args) {
        //LineChartDemo6 demo=new LineChartDemo6("Gráfica");
        //PieChart3DDemo1 demo = new PieChart3DDemo1("Gráfica");
        //TimeSeriesDemo1 demo = new TimeSeriesDemo1("Gráfica");
        DialDemo2 demo = new DialDemo2("Gráfica");
        //DialDemo3 demo = new DialDemo3("Gráfica");
        demo.pack();
        demo.setVisible(true);
    }
}
