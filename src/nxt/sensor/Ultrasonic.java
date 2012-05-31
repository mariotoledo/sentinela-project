/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nxt.sensor;

import org.sentinela.Tools;
import org.sentinela.Constants;
import nxt.sentinela.*;

/**
 *
 * @author xD
 */
public class Ultrasonic implements Constants {
    private Sentinela robo;
    private SentinelaComunicator sc;
    
    private int port;
    private int delay = 300;
    
    public boolean isStop = false;
    
    //Controle
    private boolean flag_distance = false;

    public Ultrasonic(Sentinela robo, SentinelaComunicator sc, int port) {
        this.robo = robo;
        this.sc = sc;
        this.port = port;
    }
    
    public int getDistance() {
        int length = 1;
        byte[] txData2 = {DEFAULT_ADDRESS, BYTE0};
        sc.LSWrite((byte)port, txData2, (byte)length);

        byte[] val = sc.LSRead((byte)port);
        Tools.delay(delay);
        return 0xFF & val[0];
    }
    
    public void setMode(byte mode) {
        byte[] txData = {DEFAULT_ADDRESS, COMMAND_STATE, mode};
        int portId = 0;
        sc.LSWrite((byte)portId, txData, (byte)0);
        Tools.delay(delay);
    }
    
    public void checkDistance() {
        int distancia = getDistance();
        System.out.println("distancia: " + distancia);
        isStop = false;
        if(distancia <= 21) {robo.stop(); isStop = true;}
        else if (distancia <= 31) {robo.setSpeed(20);}
        else if (distancia <= 41) {robo.setSpeed(30);}
        else if (distancia <= 61) {robo.setSpeed(50);}
        else {robo.setSpeed(70);}
    }
    
    public void checkDistance(int verifica) {
        if(!flag_distance) {
            flag_distance = true;
            int[] distancia = new int[verifica];
            int janela = 12;

            int i = 0;
            while(i < distancia.length) {
                distancia[i] = getDistance();
                System.out.println(distancia[i]);
                if(i > 0 && distancia[i] != 255 && (distancia[i-1] <= (distancia[i] + janela) && distancia[i-1] >= (distancia[i] - janela))) {
                    if(distancia[i] <= 21) {robo.stop();}
                    else if (distancia[i] <= 31) {robo.setSpeed(20); robo.forward();}
                    else if (distancia[i] <= 41) {robo.setSpeed(30); robo.forward();}
                    else if (distancia[i] <= 61) {robo.setSpeed(50); robo.forward();}
                    else {robo.setSpeed(70); robo.forward();}
                }
                i++;
            }
            flag_distance = false;
        }
        return;
    }
}
