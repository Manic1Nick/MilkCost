package ua.nick.milkcost.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.YearMonth;
import java.util.Map;

@Entity
@Table(name = "litres")
public class Milk {

    private YearMonth period;
    private int cows;
    private double litresProduced;
    private double litresSold;

    //private Map<String, Double> beginStructure;

    public Milk() {}

    public Milk(int litresProduced, int litresSold) {
        this.litresProduced = litresProduced;
        this.litresSold = litresSold;
    }

    public Milk(Map<String, Double> quantityMap) {
        for (String key : quantityMap.keySet()) {
            if (key.contains("PRODUCE"))
                this.litresProduced = quantityMap.get(key);

            if (key.contains("SOLD"))
                this.litresSold = quantityMap.get(key);

            if (key.contains("ANIMALS"))
                this.cows = quantityMap.get(key).intValue();
        }
    }

    @Id
    public YearMonth getPeriod() {
        return period;
    }

    public void setPeriod(YearMonth period) {
        this.period = period;
    }

    public int getCows() {
        return cows;
    }

    public void setCows(int cows) {
        this.cows = cows;
    }

    public double getLitresProduced() {
        return litresProduced;
    }

    public void setLitresProduced(double litresProduced) {
        this.litresProduced = litresProduced;
    }

    public double getLitresSold() {
        return litresSold;
    }

    public void setLitresSold(double litresSold) {
        this.litresSold = litresSold;
    }

    public double getMilkProducedByCow() {
        return litresProduced / cows ;
    }

    public double getMilkProducedByCowPerDay() {
        //if can't get days from monthYear
        //int daysInPeriod = getDaysFromPeriod();
        return litresProduced / cows / period.lengthOfMonth();
    }

    public double getMilkSoldPerDay() {
        //if can't get days from monthYear
        //int daysInPeriod = getDaysFromPeriod();
        return litresSold / period.lengthOfMonth();
    }

	/*
	private int getDaysFromPeriod() {
		int days;
		List<String> months30 = new ArrayList<>("04", "06", "09", "11");
		List<String> months31 = new ArrayList<>("01", "03", "05, "07", "08", "10", "12");
		if (month30.contains(period.getMonth()))
			days = 30;
		else if (month31.contains(period.getMonth())
			days = 31;
		else
			days = 28;

		return days;
	}
	*/

/*
    public double getCostMilkUnitProduceFull() {
        return (cost.getCostDirect() + cost.getCostOverhead() +
                cost.getCostAdditional()) / quantityProduce;
    }

    public double getCostMilkUnitProduceWithoutAdmin() {
        return (cost.getCostDirect() + cost.getCostOverhead()) / quantityProduce;
    }

    public double getCostMilkUnitProduceDirect() {
        return cost.getCostDirect() / quantityProduce;
    }

    public double getCostMilkUnitSoldWithoutAdmin() {
        return (cost.getCostTotal() - cost.getCostAdmin() +
                beginStructure.get("SUMM_BEGIN")) /
                (quantityProduce + beginStructure.get("QUANT_BEGIN"));
    }

    public double getCostMilkUnitSoldFull() {
        return (cost.getCostTotal() + beginStructure.get("SUMM_BEGIN")) /
                (quantityProduce + beginStructure.get("QUANT_BEGIN"));
    }

    public double getPriceMilkUnit() {
        return revenue.getRevenue() / quantitySold;
    }

    public void setBeginStructure(Map<String, Double> beginStructure) {
        this.beginStructure = beginStructure;
    }

    public double getCostSoldWithoutAdmin() {
        return getCostMilkUnitSoldWithoutAdmin() * quantitySold;
    }
*/
}
