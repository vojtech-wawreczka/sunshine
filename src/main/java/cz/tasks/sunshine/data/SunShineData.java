package cz.tasks.sunshine.data;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Třída reprezentuje data načtená ze souboru s hodnotami měření.
 */
public class SunShineData {
    
    /**
     * Součty hodnot po měsících.
     */
    private final SortedMap<String, Double> monthTotalMap;
   
    /**
     * Součty hodnot po dnech.
     */
    private final SortedMap<String, Double> dayTotalMap;
    
    /**
     * Počet hodnot po měsících.
     */
    private final SortedMap<String, Integer> monthQuantityMap;
    
    /**
     * Počet hodnot po dnech.
     */
    private final SortedMap<String, Integer> dayQuantityMap;
    
    /**
     * Celkový součet hodnot.
     */
    private Double total;
    
    /**
     * Celkový počet hodnot.
     */
    private Integer quantity;
    
    
    /**
     * Základní konstruktor inicializuje vlastnosti objektu.
     */
    public SunShineData(){
        total = 0.0;
        quantity = 0;
        monthTotalMap = new TreeMap<>();
        dayTotalMap = new TreeMap<>();
        monthQuantityMap = new TreeMap<>();
        dayQuantityMap = new TreeMap<>();
    }

    /**
     * Metoda pro vrácení celkových hodnot po měsících.
     * @return Mapa celkových hodnot po měsících.
     */
    public SortedMap<String, Double> getMonthTotalMap() {
        return monthTotalMap;
    }

    /**
     * Metoda pro vrácení celkových hodnot po dnech.
     * @return Mapa celkových hodnot po dnech.
     */
    public SortedMap<String, Double> getDayTotalMap() {
        return dayTotalMap;
    }

    /**
     * Metoda pro vrácení počtu měření po měsících.
     * @return Mapa počtu měření po měsících
     */
    public SortedMap<String, Integer> getMonthQuantityMap() {
        return monthQuantityMap;
    }

    /**
     * Metoda pro vrácení počtu měření po dnech.
     * @return Mapa počtu měření po dnech
     */
    public SortedMap<String, Integer> getDayQuantityMap() {
        return dayQuantityMap;
    }

    /**
     * Metoda pro vrácení celkové hodnoty všech měření.
     * @return Celková hodnota všech měření.
     */
    public Double getTotal() {
        return total;
    }

    /**
     * Metoda pro vrácení počtu všech měření.
     * @return Počet všech měření.
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Metoda pro nastavení celkové hodnoty všech měření
     * @param total Celková hodnota všech měření.
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * Metoda pro nastavení počtu všech měření.
     * @param quantity Počet všech měření.
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    /**
     * Metoda pro vrácení průměrné hodnoty všech měření.
     * @return Průměrná hodnota všech měření.
     */
    public double getTotalAverage(){
       return quantity == 0 ? 0 : total/quantity;
    }
    
    /**
     * Metoda pro vrácení průměrné hodnoty měření v daném měsíci
     * @param month Měsíc, pro který se vrací průměrná hodnota.
     * @return Průměrná hodnota všech měření v daném měsíci.
     */
    public double getMonthAverage(String month){
        double value = 0.0;
                
        if(monthTotalMap.containsKey(month) && monthQuantityMap.containsKey(month) && monthQuantityMap.get(month) != 0)
            value = monthTotalMap.get(month)/monthQuantityMap.get(month);
                
        return value;
    }
}
