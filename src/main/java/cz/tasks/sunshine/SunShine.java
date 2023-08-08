package cz.tasks.sunshine;

import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Třída pro zpracování hodnot slunečního svitu ze vstupního souboru vytiskne
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
public class SunShine {
    
    /**
     * Datový formát pro měsíc a rok.
     */
    private static final DateFormat FORMAT_YYYYMM = new SimpleDateFormat("yyyy-MM");
    
    /**
     * Datový formát pro den, měsíc a rok.
     */
    private static final DateFormat FORMAT_YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
    
    /**
     * Datový formát pro den v týdnu.
     */
    private static final DateFormat FORMAT_EEE = new SimpleDateFormat("EEE");

    /**
     * Seznam dnů v týdnů.
     */
    private final List<String> daysOfWeek;
            
    /**
     * Součty hodnot po měsících.
     */
    private final TreeMap<String, Double> monthTotalMap;
   
    /**
     * Součty hodnot po dnech.
     */
    private final TreeMap<String, Double> dayTotalMap;
    
    /**
     * Počet hodnot po měsících.
     */
    private final TreeMap<String, Integer> monthQuantityMap;
    
    /**
     * Počet hodnot po dnech.
     */
    private final TreeMap<String, Integer> dayQuantityMap;
    
    /**
     * Celkový součet hodnot.
     */
    private Double total;
    
    /**
     * Celkový počet hodnot.
     */
    private Integer quantity;
    
    /**
     * Základní konstruktor, inicializuje vlastnosti instance třídy.
     */
    public SunShine(){
        
        total = 0.0;
        quantity = 0;
        monthTotalMap = new TreeMap<>();
        dayTotalMap = new TreeMap<>();
        monthQuantityMap = new TreeMap<>();
        dayQuantityMap = new TreeMap<>();
        
        String[] wd = new DateFormatSymbols().getShortWeekdays();
        daysOfWeek = List.of(wd[0], wd[2], wd[3], wd[4], wd[5], wd[6], wd[7], wd[1]);
    }
    
    /**
     * Metoda ukládá hodnotu do datových struktur pro pozdější zpracování.
     * @param date Datum, kdy bylo měření provedeno.
     * @param value Naměřená hodnota.
     */
    private void save(Date date, Double value){
     
        String month = FORMAT_YYYYMM.format(date);
        String day = FORMAT_YYYYMMDD.format(date);
        
        total += value;
        quantity++;
        
        if(!monthTotalMap.containsKey(month)){
            monthTotalMap.put(month, value);
            monthQuantityMap.put(month, 1);
        }
        else{
            monthTotalMap.put(month, monthTotalMap.get(month) + value);
            monthQuantityMap.put(month, monthQuantityMap.get(month) + 1);
        }
        
        if(!dayTotalMap.containsKey(day)){
            dayTotalMap.put(day, value);
            dayQuantityMap.put(day, 1);
        }
        else{
            dayTotalMap.put(day, dayTotalMap.get(day) + value);
            dayQuantityMap.put(day, dayQuantityMap.get(day) + 1);
        }
        
    }
        
    /**
     * Metoda načítá data ze vstupního souboru a potřebná data ukládá do 
     * datových struktur.
     * @param from Počáteční datum, od kdy se hodnoty ukládají.
     * @param to Konečné datum, do kdy se hodnoty ukládají.
     * @param dayOfWeek Den v týdnu, pro který se hodnoty ukládají.
     * @throws IOException Chyba při načítání vstupního souboru.
     */
    public void loadData(Date from, Date to, int dayOfWeek) throws IOException{
        
        try (InputStream in = getClass().getResourceAsStream("/data/dataexport.csv")){
    
            CSVReader reader = new CSVReader(new InputStreamReader(in));
        
            for (String[] row : reader) {
                
                if(row.length == 2 && row[0] != null && row[1] != null && !row[0].isEmpty() && !row[1].isEmpty()){
                        
                    try {
                        Date date = new SimpleDateFormat("yyyyMMdd'T'HHmm").parse(row[0]);
                            
                        Double value = Double.valueOf(row[1]);
                                
                        if(from != null && to != null){
                                    
                            if(from.after(date) || to.before(date))
                                continue;
                                    
                            if(dayOfWeek == 0){
                                save(date, value);
                            }
                            else if(daysOfWeek.indexOf(FORMAT_EEE.format(date)) == dayOfWeek){
                                save(date, value);
                            }
                        }
                        else{
                            save(date, value);
                        }
                                
                    } catch (ParseException ex) {
                            
                    }
                }
            }
        }
    }
        
    /**
     * Metoda vytiskne na standardní výstup součet, popř. průměr hodnot všech hodnot.
     * @param average Pokud je TRUE, tiskne se i průměr hodnot, jinak ne.
     */
    public void printTotal(boolean average){
        System.out.println("Total: " + total + " W/m2");
        
        if(average)
            System.out.println("Average: " + (quantity == 0 ? 0 : total/quantity) + " W/m2");
        
    }
    
    /**
     * Metoda vytiskne na standardní výstup celkový součet hodnot i součet hodnot po měsících.
     * @param average Pokud je TRUE, tiskne se i průměr hodnot, jinak ne.
     */
    public void printMonths(boolean average){
        
        printTotal(average);
        
        for(Entry<String, Double> entry : monthTotalMap.entrySet()){
            System.out.println("Total in month " + entry.getKey() + ": " + entry.getValue() + " W/m2");
            
            if(average)
                System.out.println("Average in month " + entry.getKey() + ": " + (monthQuantityMap.containsKey(entry.getKey()) && monthQuantityMap.get(entry.getKey()) != 0 ? entry.getValue()/monthQuantityMap.get(entry.getKey()) : 0) + " W/m2");
        }
        
    }
    
    /**
     * Metoda vytiskne na standardní výstup celkový součet hodnot pro každý den podle
     * zadaného parametru dne v týdnu, tiskne i průměrnou hodnotu v měsíci.
     */
    public void printDaysOfWeek(){
        
        String actualMonth = null;
        
        for(Entry<String, Double> entry : dayTotalMap.entrySet()){
            
            try {
                String month = FORMAT_YYYYMM.format(FORMAT_YYYYMMDD.parse(entry.getKey()));
            
                if(month == null)
                    continue;
                
                if(!month.equals(actualMonth)){
                    if(actualMonth != null)
                        System.out.println("Average in month " + actualMonth + ": " + (monthTotalMap.containsKey(actualMonth) && monthQuantityMap.containsKey(actualMonth) && monthQuantityMap.get(actualMonth) != 0 ? monthTotalMap.get(actualMonth)/monthQuantityMap.get(actualMonth) : 0) + " W/m2");
                    
                    actualMonth = month;
                }
            
                System.out.println("Total in day " + entry.getKey() + ": " + entry.getValue() + " W/m2");
            
            } catch (ParseException ex) {
            
            }
        }
        
        System.out.println("Average in month " + actualMonth + ": " + (monthTotalMap.containsKey(actualMonth) && monthQuantityMap.containsKey(actualMonth) && monthQuantityMap.get(actualMonth) != 0 ? monthTotalMap.get(actualMonth)/monthQuantityMap.get(actualMonth) : 0) + " W/m2");
    }
    
    /**
     * Hlavní metoda aplikace, načítá argumenty a řídí chod programu.
     * @param args Argumenty zadané při spuštění programu.
     */
    public static void main(String[] args) {
    
        List<String> arguments = Arrays.asList(args);
        
        Date from = null;
        Date to = null;
        int dayOfWeek = 0;
        boolean average = false;
        
        if(arguments.size() >= 2){
            
            try{
            
                from = new SimpleDateFormat("yyyyMM").parse(arguments.get(0));
                to = new SimpleDateFormat("yyyyMM").parse(arguments.get(1));
            
                Calendar c = Calendar.getInstance(); 
                c.setTime(to); 
                c.add(Calendar.MONTH, 1);
                c.add(Calendar.SECOND, -1);
                to = c.getTime();
            
            } catch(ParseException exp){
                from = null;
                to = null;
            }
        }
        
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
        
        if(arguments.contains("-a")){
            average = true;
        }
        
        try{
            
            SunShine sunshine = new SunShine();
            sunshine.loadData(from, to, dayOfWeek);
            
            if(from != null && to != null){
                if(dayOfWeek != 0){
                    sunshine.printDaysOfWeek();
                }
                else{
                    sunshine.printMonths(average);
                }
            }
            else{
                sunshine.printTotal(average);
            }
            
        } catch (IOException ex) {
            System.out.println("Loading input file failed.");
        } 
        
    }
}
