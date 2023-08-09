package cz.tasks.sunshine.models.services;

import com.opencsv.CSVReader;
import cz.tasks.sunshine.data.SunShineData;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Třída poskytuje služby pro práci s naměřenými daty slunečního svitu.
 */
public class SunShineService {
    
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
     * Metoda vrací seznam dnů v týdnu.
     * @return Seznam dnů v týdnu.
     */
    private List<String> getDaysOfWeek() {
        String[] wd = new DateFormatSymbols().getShortWeekdays();
        return List.of(wd[0], wd[2], wd[3], wd[4], wd[5], wd[6], wd[7], wd[1]);
    }
    
    /**
     * Metoda vypočítá měsíc ze dne.
     * @param day Den, ze kterého se má získat měsíc.
     * @return Vypočítaný měsíc.
     */
    public static String dayToMonth(String day){
        try {
            return SunShineService.FORMAT_YYYYMM.format(SunShineService.FORMAT_YYYYMMDD.parse(day));
        } catch (ParseException ex) {
            return null;
        }
    }
    
    /**
     * Metoda pro načtení naměřených dat ze souboru.
     * @param from Datum, od kdy se data ukládají.
     * @param to Datum, do kdy se data ukládají.
     * @param dayOfWeek Den v týdnu, pro který se data ukládají.
     * @return Načtená data.
     * @throws IOException Chyba při načítání vstupního souboru.
     */
    public SunShineData loadData(Date from, Date to, int dayOfWeek) throws IOException{
        
        try (InputStream in = getClass().getResourceAsStream("/data/dataexport.csv")){
    
            CSVReader reader = new CSVReader(new InputStreamReader(in));
            
            List<String> daysOfWeek = getDaysOfWeek();
            
            SunShineData data = new SunShineData();
        
            for (String[] row : reader) {
                
                if(row.length == 2 && row[0] != null && row[1] != null && !row[0].isEmpty() && !row[1].isEmpty()){
                        
                    try {
                        Date date = new SimpleDateFormat("yyyyMMdd'T'HHmm").parse(row[0]);
                            
                        Double value = Double.valueOf(row[1]);
                          
                        if((from == null || to == null)
                                || (from.before(date) && to.after(date) 
                                    && (dayOfWeek == 0 || daysOfWeek.indexOf(FORMAT_EEE.format(date)) == dayOfWeek)))
                            save(data, date, value);
                                
                    } catch (ParseException ex) {
                            
                    }
                }
            }
            
            return data;
        }
    }
    
    /**
     * Metoda ukládá hodnotu do datového objektu.
     * @param data Datový objekt, kam se ukládají hodnoty.
     * @param date Datum, kdy bylo měření provedeno.
     * @param value Naměřená hodnota.
     */
    private void save(SunShineData data, Date date, Double value){
     
        String month = FORMAT_YYYYMM.format(date);
        String day = FORMAT_YYYYMMDD.format(date);
        
        data.setTotal(data.getTotal() + value);
        data.setQuantity(data.getQuantity() + 1);
        
        if(!data.getMonthTotalMap().containsKey(month)){
            data.getMonthTotalMap().put(month, value);
            data.getMonthQuantityMap().put(month, 1);
        }
        else{
            data.getMonthTotalMap().put(month, data.getMonthTotalMap().get(month) + value);
            data.getMonthQuantityMap().put(month, data.getMonthQuantityMap().get(month) + 1);
        }
        
        if(!data.getDayTotalMap().containsKey(day)){
            data.getDayTotalMap().put(day, value);
            data.getDayQuantityMap().put(day, 1);
        }
        else{
            data.getDayTotalMap().put(day, data.getDayTotalMap().get(day) + value);
            data.getDayQuantityMap().put(day, data.getDayQuantityMap().get(day) + 1);
        }
        
    }
}
