package org.inmine.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by RINES on 17.11.17.
 */
public class OwnLogger extends Logger {
    private final BlockingQueue<LogRecord> queue = new LinkedBlockingQueue<>();
    
    public OwnLogger(String applicationName) {
        super(null, null);
        setLevel(Level.ALL);
        FileHandler fileHandler;
        try {
            ConciseFormatter formatter = new ConciseFormatter();
            fileHandler = new FileHandler(applicationName + "-%g.log", 5_000_000, 5, true);
            fileHandler.setFormatter(formatter);
            addHandler(fileHandler);
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(formatter);
            addHandler(consoleHandler);
        } catch (IOException ex) {
            System.err.println("Could not register logger!");
            ex.printStackTrace();
            return;
        }
        System.setErr(new PrintStream(new LoggingOutputStream(this, Level.SEVERE), true));
        System.setOut(new PrintStream(new LoggingOutputStream(this, Level.INFO), true));
        Thread t = new Thread(this::logDispatcher);
        t.setDaemon(true);
        t.setName("Log Dispatcher");
        t.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            t.interrupt();
            for (LogRecord record : queue)
                realLog(record);
            fileHandler.close();
        }));
    }
    
    private void logDispatcher() {
        Thread thread = Thread.currentThread();
        while (!thread.isInterrupted()) {
            LogRecord record;
            try {
                record = queue.take();
            } catch (InterruptedException ex) {
                continue;
            }
            realLog(record);
        }
    }
    
    private void realLog(LogRecord record) {
        super.log(record);
    }
    
    @Override
    public void log(LogRecord record) {
        queue.add(record);
    }
    
    public Logger deriveLogger(String name) {
        Logger logger = new Logger(name, null) {
            @Override
            public void log(LogRecord record) {
                queue.add(record);
            }
        };
        logger.setUseParentHandlers(false);
        return logger;
    }
    
    private static class ConciseFormatter extends Formatter {
        private final DateFormat date = new SimpleDateFormat("HH:mm:ss");
        
        @Override
        @SuppressWarnings("ThrowableResultIgnored")
        public String format(LogRecord record) {
            StringBuilder formatted = new StringBuilder();
            
            formatted.append(date.format(record.getMillis()));
            formatted.append(" [");
            formatted.append(record.getLevel().getLocalizedName());
            formatted.append("] ");
            if (record.getLoggerName() != null) {
                formatted.append("[");
                formatted.append(record.getLoggerName());
                formatted.append("] ");
            }
            formatted.append(formatMessage(record));
            formatted.append('\n');
            if (record.getThrown() != null) {
                StringWriter writer = new StringWriter();
                record.getThrown().printStackTrace(new PrintWriter(writer));
                formatted.append(writer);
            }
            
            return formatted.toString();
        }
    }
    
    public class LoggingOutputStream extends ByteArrayOutputStream {
        private final String separator = System.getProperty("line.separator");
        private final Logger logger;
        private final Level level;
        
        public LoggingOutputStream(Logger logger, Level level) {
            this.logger = logger;
            this.level = level;
        }
        
        @Override
        public void flush() throws IOException {
            String contents = toString("UTF-8");
            super.reset();
            if (!contents.isEmpty() && !contents.equals(separator)) {
                logger.logp(level, "", "", contents);
            }
        }
    }
}
