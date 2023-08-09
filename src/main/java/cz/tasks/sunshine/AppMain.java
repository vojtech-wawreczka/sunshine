package cz.tasks.sunshine;

import cz.tasks.sunshine.controllers.SunShineController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
 
/**
 * Aplikace pro zpracování hodnot slunečního svitu ze vstupního souboru vytiskne
 * na standardní výstup požadované součty či průměry hodnot podle zadaných argumentů.
 * 
 * Možné použití:
 * 
 * Argumenty: [-a]
 * Vytiskne na výstup součet všech hodnot ve vstupním souboru. Je-li zadán argument
 * -a, vytiskne se i průměr všech hodnot.
 * 
 * Argumenty: <from> <to> [-a]
 * Vytiskne na výstup součet hodnot jednotlivě po měsících i celkově za období dané
 * argumenty FROM a TO. Je-li zadán parametr -a, tisknou se i průměry hodnot.
 * 
 * Argumenty: <from> <to> <day>
 * Vytiskne na výstup součet hodnot v jednotlivé dny pro den v týdnu zadaný argumentem
 * DAY a současně průměrné hodnoty v jednotlivých měsících daného období, opět podle
 * zadaného dne v týdnu.
 */
public class AppMain {
    
    /**
     * Metoda pro zpracování argumentů FROM a TO.
     * @param arguments Argumenty, se kterými byl spuštěn program.
     * @return Hodnoty argumentů FROM a TO.
     */
    public static Date[] parseFromTo(List<String> arguments){
        
        Date[] dates = {null, null};
        
        if(arguments.size() >= 2){
            
            try{
            
                dates[0] = new SimpleDateFormat("yyyyMM").parse(arguments.get(0));
                dates[1] = new SimpleDateFormat("yyyyMM").parse(arguments.get(1));
            
                Calendar c = Calendar.getInstance(); 
                c.setTime(dates[1]); 
                c.add(Calendar.MONTH, 1);
                c.add(Calendar.SECOND, -1);
                dates[1] = c.getTime();
            
            } catch(ParseException exp){
                dates[0] = null;
                dates[1] = null;
            }
        }
        
        return dates;
    }
    
    /**
     * Metoda pro zpracování argumentu DAY.
     * @param arguments Argumenty, se kterými byl spuštěn program.
     * @return Hodnota argumentu DAY.
     */
    public static int parseDayOfWeek(List<String> arguments){
        
        int dayOfWeek = 0;
        
        if(arguments.size() >= 3){
            try{
                dayOfWeek = Integer.parseInt(arguments.get(2));
                
                if(dayOfWeek < 1 || dayOfWeek > 7){
                    throw new NumberFormatException("Not in range 1-7.");
                }
            } catch(NumberFormatException exp){
                dayOfWeek = 0;
            } 
        }
        
        return dayOfWeek;
    }
    
    /**
     * Metoda pro zpracování argumentu AVERAGE.
     * @param arguments Argumenty, se kterými byl spuštěn program.
     * @return Hodnota argumentu AVERAGE.
     */
    public static boolean parseAverage(List<String> arguments){
        
        boolean average = false;
        
        if(arguments.contains("-a")){
            average = true;
        }
        
        return average;
    }
    
    /**
     * Hlavní metoda aplikace, načítá argumenty a podle nich volá kontroler.
     * @param args Argumenty zadané při spuštění programu.
     */
    public static void main(String[] args) {
    
        List<String> arguments = Arrays.asList(args);
        
        Date[] dates = parseFromTo(arguments);
        Date from = dates[0];
        Date to = dates[1];
        
        int dayOfWeek = parseDayOfWeek(arguments);
        
        boolean average = parseAverage(arguments);
        
        SunShineController sunshine = new SunShineController();
        
        if(from != null && to != null){
            if(dayOfWeek != 0){
                sunshine.printDaysOfWeek(from, to, dayOfWeek);
            }
            else{
                sunshine.printMonths(from, to, average);
            }
        }
        else{
            sunshine.printTotal(average);
        }
    }
}
