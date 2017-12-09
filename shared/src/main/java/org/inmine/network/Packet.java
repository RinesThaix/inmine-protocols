package org.inmine.network;

/**
 * Created by RINES on 16.11.17.
 */
public abstract class Packet implements Cloneable {
    protected static final int NICKNAME_SIZE = 40;
    protected static final int ADDRESS_SIZE = 50;
    
    public abstract int getId();
    
    public abstract void write(Buffer buffer);
    
    public abstract void read(Buffer buffer);
    
    @Override
    public Packet clone() {
        try {
            return (Packet) super.clone();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
}
