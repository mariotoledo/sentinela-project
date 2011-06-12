/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sentinela;

/**
 *
 * @author xD
 */
public interface Constants {
    byte DEFAULT_ADDRESS = 0x02;
    byte DIRECT_COMMAND_NOREPLY = (byte)0x80;
    byte SET_OUTPUT_STATE = 0x04;
    byte BRAKE = 0x02;
    byte REGULATED = 0x04;
    byte MOTORON = 0x01;
    int mode = BRAKE + REGULATED + MOTORON;
    byte REGULATION_MODE_MOTOR_SPEED = 0x01;
    int turnRatio = 0;
    byte MOTOR_RUN_STATE_IDLE = 0x00;
    int runState = MOTOR_RUN_STATE_IDLE;
    int regulationMode = REGULATION_MODE_MOTOR_SPEED;
    int tachoLimit = 0;  
    
    byte COMMAND_STATE = 0x41; // Command or reply length = 1
    byte BYTE0 = 0x42;
    
    byte LS_WRITE = 0x0F;
    byte DIRECT_COMMAND_REPLY = 0x00;
    byte LS_READ = 0x10;

    //Ultrasonic Sensor
    public static byte OFF = 0x00;
    public static byte SINGLE_SHOT = 0x01;
    public static byte CONTINUOUS_MEASUREMENT = 0x02;
}
