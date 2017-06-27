/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.guntram.mcmod.entitylogger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 *
 * @author gbl
 */
public class DailyLogWriter {
    
    private final String filenameTemplate;
    private PrintWriter writer;
    private String lastUsedDate;
    private boolean hadErrors;
    final SimpleDateFormat currentDateFormatter;
    
    
    public DailyLogWriter(String template) {
        filenameTemplate=template;
        writer=null;
        lastUsedDate="";
        hadErrors=false;
        currentDateFormatter=new SimpleDateFormat("yyyy-MM-dd");
    }

    public PrintWriter getWriter() {
        if (hadErrors)
            return null;
        String dateToUse=currentDateFormatter.format(Calendar.getInstance().getTime());
        System.out.println("getting writer for "+filenameTemplate+" on "+dateToUse);
        if (writer!=null && !dateToUse.equals(lastUsedDate)) {
            writer.close();
            writer=null;
        }
        if (writer==null) {
            lastUsedDate=dateToUse;
            String filename=filenameTemplate.replace("%d", dateToUse);
            try {
                writer=new PrintWriter(new FileWriter(filename));
            } catch (IOException ex) {
                hadErrors=true;
                System.err.println("Cannot write to "+filename);
                ex.printStackTrace(System.err);
                return null;
            }
        }
        return writer;
    }
    
    public void closeWriter() {
        if (writer!=null)
            writer.close();
        writer=null;
    }
}
