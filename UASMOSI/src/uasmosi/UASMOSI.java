/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uasmosi;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 * @author Adam
 */
public class UASMOSI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println(" MOSI STOCK MARKET PREDICTION ");
        double alpha = 0.01;
        int hn = 5; //hidden neuron
        double[] y = new double[hn]; // summing function neuron
        double y2; //hasil summing function output
        double[] yy = new double[hn]; //fungsi aktifasi
        double[] b = new double[hn]; //bias hidden
        double output = 0; //output 
        double boutput; //bias output
        int baris = 420;
        //1210
        //847
        //604
        int kolom = 3;
        int baris_tes = 60;
        int kolom_tes = 3;
        double error = 0;
        double error2 = 0;
        double MSE = 0;
        double max1, max2, max3, min1, min2, min3;
        double[][] data = new double[kolom][baris];
        String[][] train = new String[kolom][baris];
        String[][] testing = new String[kolom_tes][baris_tes];
        double[][] test = new double[kolom_tes][baris_tes];
        int maxEpoch = 500;
        int epoch = 0;
        double x1, x2, target; //buat input summing function    
        double x1_input, x2_input, x3_input;
        double d2, db2;
        double[] d1 = new double[hn]; //menghitung d1

        try {
            //data training

            Workbook w = Workbook.getWorkbook(new File("D:\\don't open\\semester 6\\mosi\\tubes mosi new\\table 2003 fix.xls"));
            Sheet sh = w.getSheet(1);

            for (int i = 0; i < kolom; i++) {
                for (int j = 0; j < baris; j++) {
                    Cell c = sh.getCell(i, j);
                    String isi = c.getContents();
                    data[i][j] = Double.parseDouble(isi);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(UASMOSI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(UASMOSI.class.getName()).log(Level.SEVERE, null, ex);
        }

        //find max baris 1
        max1 = data[0][0];
        for (int i = 0; i < baris; i++) {
            if (data[0][i] > max1) {
                max1 = data[0][i];
            }

        }
        //find min baris 1
        min1 = data[0][0];
        for (int i = 0; i < baris; i++) {
            if (data[0][i] < min1) {
                min1 = data[0][i];
            }

        }

        //find max baris 2
        max2 = data[1][0];
        for (int i = 0; i < baris; i++) {
            if (data[1][i] > max2) {
                max2 = data[1][i];
            }

        }
        //find min baris 2
        min2 = data[1][0];
        for (int i = 0; i < baris; i++) {
            if (data[1][i] < min2) {
                min2 = data[1][i];
            }

        }

        //find max baris 3
        max3 = data[2][0];
        for (int i = 0; i < baris; i++) {
            if (data[2][i] > max3) {
                max3 = data[2][i];
            }

        }
        //find min baris 3
        min3 = data[2][0];
        for (int i = 0; i < baris; i++) {
            if (data[2][i] < min3) {
                min3 = data[2][i];
            }

        }
        System.out.println("max 1 " + max1);
        System.out.println("max 2 " + max2);
        System.out.println("max 3 " + max3);
        System.out.println("min 1 " + min1);
        System.out.println("min 2 " + min2);
        System.out.println("min 3 " + min3);

        //normalisasi baris 1
        for (int i = 0; i < baris; i++) {
            data[0][i] = (2 * data[0][i] - (max1 + min1)) / (max1 + min1);
        }
        //normalisasi baris 2
        for (int i = 0; i < baris; i++) {
            data[1][i] = (2 * data[1][i] - (max2 + min2)) / (max2 + min2);
        }
        //normalisasi baris 2
        for (int i = 0; i < baris; i++) {
            data[2][i] = (2 * data[2][i] - (max3 + min3)) / (max3 + min3);
        }

        //output
        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                System.out.print(data[j][i] + " ");
            }
            System.out.println(" ");
        }

        //bobot ke hidden
        double[] w1 = new double[hn];  //dari input 1
        double[] w2 = new double[hn];  //dari input 2
        //bobot ke output
        double[] Woutput = new double[hn];

        //update bobot
        double[] dw1 = new double[hn];  //new bobot  1
        double[] dw2 = new double[hn];  //new bobot  2
        double[] dwoutput = new double[hn];
        double[] db = new double[hn];
        
        
        //random bobot
        Random r = new Random();
        for (int i = 0; i < hn; i++) {
            w1[i] = r.nextDouble();
            w2[i] = r.nextDouble();
            Woutput[i] = r.nextDouble();
            b[i] = r.nextDouble();
        }
        boutput = r.nextDouble();

        //training
        while (epoch < maxEpoch) {

            for (int i = 0; i < baris; i++) {
                x1 = data[0][i];
                x2 = data[1][i];
                target = data[2][i];

                //hitung maju
                double temp = 0;

                for (int j = 0; j < hn; j++) {
                    y[j] = x1 * w1[j] + x2 * w2[j] + b[j];
                    yy[j] = 1 / (1 + (Math.exp(-y[j]))); //aktifasi sigmoid hidden
                    temp = yy[j] * Woutput[j] + temp;
                }

                y2 = temp + boutput; //summing function output
                //output = 1 / (1 + (Math.exp(-y2))); //aktifasi sigmoid output
//                error = target - output;
//                error2 = error2 + Math.pow(error, 2);
//                if (output <= 0.5) {
//                    output = 0;
//                }
//                if (output > 0.5) {
//                    output = 1;
//                }
//                if (target <= 0.5) {
//                    target = 0;
//                }
//                if (target > 0.5) {
//                    target = 1;
//                }
                error = target - output;
                error2 = error2 + Math.pow(error, 2);
               // System.out.println("target "+target+" output "+output);
                    for (int j = 0; j < hn; j++) { //hidden dw, bias , woutput , bias output
                    dw1[j]=error*(1-(Math.pow(output, 2))) * Woutput[j] * (1-(Math.pow(yy[j],2)))*x1*alpha; // error * output^2 * bobot hidden ke ouput neuron i * output hidden neuron ke i ^ 2 * input * alpha
                    dw2[j]=error*(1-(Math.pow(output, 2))) * Woutput[j] * (1-(Math.pow(yy[j],2)))*x2*alpha;
                    dwoutput[j]=error*(1-(Math.pow(output, 2)))*Woutput[j]*alpha;
                    db[j]=error*(1-(Math.pow(output, 2))) * Woutput[j] * (1-(Math.pow(yy[j],2)))*alpha;
                }
                 db2 = error*(1-(Math.pow(output, 2)))*alpha;
                 
                 //update bobot
                 for (int j = 0; j < hn; j++) {
                    w1[j]=dw1[j]+w1[j];
                    w2[j]=dw2[j]+w2[j];
                    Woutput[j]=dwoutput[j]+Woutput[j];
                    b[j]=db[j]+b[j];
                    
                }
                    boutput= db2 + boutput;
                    
                    
            }
            
            MSE = error2/baris;
            System.out.println((epoch+1)+" MSE : "+MSE);
            epoch ++;
            MSE =0;
            error2=0;
        }
        
        
        //testing
        try {
            //data training

            Workbook w = Workbook.getWorkbook(new File("D:\\don't open\\semester 6\\mosi\\tubes mosi new\\table 2003 fix.xls"));
            Sheet sh = w.getSheet(2);

            for (int i = 0; i < kolom_tes; i++) {
                for (int j = 0; j < baris_tes; j++) {
                    Cell c = sh.getCell(i, j);
                    String isi = c.getContents();
                    test[i][j] = Double.parseDouble(isi);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(UASMOSI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(UASMOSI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //find max baris 1
        max1 = test[0][0];
        for (int i = 0; i < baris_tes; i++) {
            if (test[0][i] > max1) {
                max1 = test[0][i];
            }

        }
        //find min baris 1
        min1 = test[0][0];
        for (int i = 0; i < baris_tes; i++) {
            if (test[0][i] < min1) {
                min1 = test[0][i];
            }

        }

        //find max baris 2
        max2 = test[1][0];
        for (int i = 0; i < baris_tes; i++) {
            if (test[1][i] > max2) {
                max2 = test[1][i];
            }

        }
        //find min baris 2
        min2 = test[1][0];
        for (int i = 0; i < baris_tes; i++) {
            if (test[1][i] < min2) {
                min2 = test[1][i];
            }

        }

        //find max baris 3
        max3 = test[2][0];
        for (int i = 0; i < baris_tes; i++) {
            if (test[2][i] > max3) {
                max3 = test[2][i];
            }

        }
        //find min baris 3
        min3 = test[2][0];
        for (int i = 0; i < baris_tes; i++) {
            if (test[2][i] < min3) {
                min3 = test[2][i];
            }

        }
        
        //normalisasi baris 1
        for (int i = 0; i < baris_tes; i++) {
            test[0][i] = (2 * test[0][i] - (max1 + min1)) / (max1 + min1);
        }
        //normalisasi baris 2
        for (int i = 0; i < baris_tes; i++) {
            test[1][i] = (2 * test[1][i] - (max2 + min2)) / (max2 + min2);
        }
        //normalisasi baris 2
        for (int i = 0; i < baris_tes; i++) {
            test[2][i] = (2 * test[2][i] - (max3 + min3)) / (max3 + min3);
        }
        
        double benar=0; 
        for (int i = 0; i < baris_tes; i++) {
            x1_input = test[0][i];
            x2_input = test[1][i];
            target = test[2][i];
            
            double temp = 0;
            for (int j = 0; j < hn; j++) {
                y[j] = x1_input * w1[j] + x2_input * w2[j] + b[j]; //summing function hidden
                yy[j] = 1 / (1 + (Math.exp(-y[j]))); //aktifasi sigmoid hidden
                temp = yy[j] * Woutput[j] + temp;  //summing ke output 
            }
            
            y2 = temp + boutput; //summing function output
            //output = 1 / (1 + (Math.exp(-y2))); //aktifasi sigmoid output
            if(output<=0.5){
                output=0;
            }if(output>0.5){
                output=1;
            }
            if(target<=0.5){
                target=0;
            }if(target>0.5){
                target=1;
            }
            
            error = Math.abs(target - output);
            error2=error2 + Math.pow(error, 2);
            System.out.println(" target "+target+" output "+output);
            if(target==output){
                benar++;
            }
        }
            MSE=error2/baris_tes;
            System.out.println("benar "+benar);
            double akurasi = (benar/baris_tes)*100;
            double akurasi2 = (1-Math.sqrt(MSE))*100;
            System.out.println(" akurasi "+akurasi);
        
    }

}
