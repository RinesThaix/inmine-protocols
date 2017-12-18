package org.inmine.network.netty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xtrafrancyz
 */
class PacketHandleException extends Exception {
    PacketHandleException(String message, Throwable cause) {
        super(message, cause, false, false);
        StackTraceElement[] trace = cause.getStackTrace();
        List<StackTraceElement> trimmedTrace = new ArrayList<>();
        for (int i = 0; i < trace.length; i++) {
            if (trace[i].getClassName().contains("io.netty."))
                break;
            trimmedTrace.add(trace[i]);
        }
        cause.setStackTrace(trimmedTrace.toArray(new StackTraceElement[trimmedTrace.size()]));
    }
}
