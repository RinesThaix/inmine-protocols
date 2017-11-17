package org.inmine.network;

/**
 * Created by RINES on 16.11.17.
 */
public abstract class Packet implements Cloneable {
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
