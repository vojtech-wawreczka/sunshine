package cz.tasks.sunshine.views;

import cz.tasks.sunshine.data.SunShineData;
import cz.tasks.sunshine.models.services.SunShineService;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * Třída poskytující metody pro tisk na výstup.
 */
public class SunShineView {
    
    /**
     * Logger pro tisk na výstup.
     */
    private static final Logger logger = LogManager.getLogger(SunShineView.class);
    
    /**
     * Desetinný formát pro výpis čísel.
     */
    private final DecimalFormat decimalFormat;

    /**
     * Základní konstruktor, inicializuje vlastnosti instance třídy.
     */
    public SunShineView(){
        
        Configurator.setLevel(SunShineView.class, Level.INFO);
        
        decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();

        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);
        decimalFormat.setMaximumFractionDigits(1);
        decimalFormat.setMinimumFractionDigits(1);
        decimalFormat.setPositiveSuffix(" W/m2");
    }
    
    
    /**
     * Metoda vytiskne na standardní výstup součet, popř.průměr hodnot všech hodnot.
     * @param data Datový objekt s uloženými hodnotami.
     * @param average Pokud je TRUE, tiskne se i průměr hodnot, jinak ne.
     */
    public void printTotal(SunShineData data, boolean average){
        logger.info(String.format("%-27s %20s", "Total: ", decimalFormat.format(data.getTotal())));
        
        if(average)
            logger.info(String.format("%-27s %20s", "Average: ", decimalFormat.format(data.getTotalAverage())));
        
    }
    
    /**
     * Metoda vytiskne na standardní výstup celkový součet hodnot i součet hodnot po měsících.
     * @param data Datový objekt s uloženými hodnotami.
     * @param average Pokud je TRUE, tiskne se i průměr hodnot, jinak ne.
     */
    public void printMonths(SunShineData data, boolean average){
        
        printTotal(data, average);
        
        for(Map.Entry<String, Double> entry : data.getMonthTotalMap().entrySet()){
            logger.info(String.format("%-27s %20s", "Total in month " + entry.getKey() + ": ", decimalFormat.format(entry.getValue())));
            
            if(average)
                logger.info(String.format("%-27s %20s", "Average in month " + entry.getKey() + ": ", decimalFormat.format(data.getMonthAverage(entry.getKey()))));
                
        }
        
    }
    
    /**
     * Metoda vytiskne na standardní výstup celkový součet hodnot pro každý den podle
     * zadaného parametru dne v týdnu, tiskne i průměrnou hodnotu v měsíci.
     * @param data Datový objekt s uloženými hodnotami.
     */
    public void printDaysOfWeek(SunShineData data){
        
        String actualMonth = null;
        
        for(Map.Entry<String, Double> entry : data.getDayTotalMap().entrySet()){
            
            String month = SunShineService.dayToMonth(entry.getKey());
            
            if(month == null)
                continue;
                
            if(!month.equals(actualMonth)){
                if(actualMonth != null)
                    logger.info(String.format("%-27s %20s", "Average in month " + actualMonth + ": ", decimalFormat.format(data.getMonthAverage(actualMonth))));
                   
                actualMonth = month;
            }
            
            logger.info(String.format("%-27s %20s", "Total in day " + entry.getKey() + ": ", decimalFormat.format(entry.getValue())));
            
        }
        
        logger.info(String.format("%-27s %20s", "Average in month " + actualMonth + ": ", decimalFormat.format(data.getMonthAverage(actualMonth))));
    }
    
}
