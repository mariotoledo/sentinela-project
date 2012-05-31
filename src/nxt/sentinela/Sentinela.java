package nxt.sentinela;

import android.R;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.*;
import java.util.UUID;
import java.util.Vector;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import nxt.sensor.Ultrasonic;

public class Sentinela extends Activity
{
    //Stream
    SentinelaComunicator sc;
    
    //Ports
    public int motA = 0;
    public int motB = 1;
    public int S1 = 0;
    
    //Controle
    public int speed = 50;
    public boolean isMoving = false; 
    
    public Sentinela(SentinelaComunicator sc) {
        this.sc = sc;
    }
    
    public void setSpeed(int _speed) {
        System.out.println("setSpeed: "+_speed);
        speed = _speed;
    }
    public void addSpeed(int _speed) {
        speed += _speed;
        System.out.println("setSpeed: " + speed);
    }
    public int getSpeed() {
        return speed;
    }
    public void forward(int motor, int speed) {
        System.out.println("forward");
        sc.motor(motor,(byte)speed, true, true);
        isMoving = true;
    }
    public void forward(int speed) {
        forward(this.motA, speed);
        forward(this.motB, speed);
    }
    public void forward() {
        forward(speed);
    }
    public void backward(int motor, int speed) {
        sc.motor(motor,(byte)-speed, true, true);
    }
    public void backward(int speed) {
        backward(this.motA, speed);
        backward(this.motB, speed);
    }
    public void backward() {
        backward(speed);
    }
    public void stop(int motor) {
        System.out.println("stop");
        sc.motor(motor,(byte)0, true, true);
        isMoving = false;
    }
    public void stop() {
        stop(this.motA);
        stop(this.motB);
    }
    
    public void right(int speed) {
        forward(this.motA, speed);
        backward(this.motB, speed);
    }
    public void right() {
        forward(this.motA, speed);
        backward(this.motB, speed);
    }
    public void left(int speed) {
        forward(this.motB, speed);
        backward(this.motA, speed);
    }
    public void left() {
        forward(this.motB, speed);
        backward(this.motA, speed);
    }
    
    public void exit() {
        byte REGULATION_MODE_IDLE = 0x00;
        byte MOTOR_RUN_STATE_IDLE = 0x00;
        
        byte ALL_MOTORS = (byte)0xFF;
        sc.setOutputState(ALL_MOTORS, (byte)0, 0x00,
        REGULATION_MODE_IDLE, 0,
        MOTOR_RUN_STATE_IDLE, 0);

    }

    public Ultrasonic addPart(int port) {
        return new Ultrasonic(this, sc, port);
    }
}