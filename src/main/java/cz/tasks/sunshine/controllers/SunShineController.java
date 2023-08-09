package cz.tasks.sunshine.controllers;

import cz.tasks.sunshine.AppMain;
import cz.tasks.sunshine.data.SunShineData;
import cz.tasks.sunshine.models.services.SunShineService;
import cz.tasks.sunshine.views.SunShineView;
import java.io.IOException;
import java.util.Date;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * Třída reprezentuje hlavní kontroler aplikace.
 */
public class SunShineController {
    
    private static final String INPUT_FILE_ERROR_MESSAGE = "Loading input file failed.";
    
    /**
     * Logger pro výpis na konzoli.
     */
    private static final Logger logger = LogManager.getLogger(SunShineController.class);
    
    /**
     * Poskytované služby.
     */
    private final SunShineService service;
    
    /**
     * Pohled.
     */
    private final SunShineView view;
    
    /**
     * Základní konstruktor, inicializuje vlastnosti třídy.
     */
    public SunShineController(){
        
        Configurator.setLevel(AppMain.class, Level.INFO);
        
        service = new SunShineService();
        view = new SunShineView();
    }

    /**
     * Metoda vytiskne na výstup celkovou hodnotu měření po dnech pro daný
     * den v týdnu a dané období, za každý měsíc navíc zobrazí i průměr hodnot.
     * @param from Datum, od kdy se sledují hodnoty měření.
     * @param to Datum, do kdy se sledují hodnoty měření.
     * @param dayOfWeek Den v týdnu, jehož hodnoty se sledují.
     */
    public void printDaysOfWeek(Date from, Date to, int dayOfWeek) {
        
        try {
            SunShineData data = service.loadData(from, to, dayOfWeek);
            view.printDaysOfWeek(data);
        } catch (IOException ex) {
            logger.error(INPUT_FILE_ERROR_MESSAGE);
        }
    }

    /**
     * Metoda vytiskne na výstup celkovou a případně průměrnou hodnotu měření
     * pro dané období celkově i po měsících.
     * @param from Datum, od kdy se sledují hodnoty měření.
     * @param to Datum, do kdy se sledují hodnoty měření.
     * @param average Informace, zda se zobrazí i průměry.
     */
    public void printMonths(Date from, Date to, boolean average) {
   
        try {
            SunShineData data = service.loadData(from, to, 0);
            view.printMonths(data, average);
        } catch (IOException ex) {
            logger.error(INPUT_FILE_ERROR_MESSAGE);
        }
    }

    /**
     * Metoda vytiskne na výstup celkovou a případně průměrnou hodnotu všech měření.
     * @param average Informace, zda se zobrazí i průměr.
     */
    public void printTotal(boolean average) {
        
        try {
            SunShineData data = service.loadData(null, null, 0);
            view.printTotal(data, average);
        } catch (IOException ex) {
            logger.error(INPUT_FILE_ERROR_MESSAGE);
        }
    }
}
