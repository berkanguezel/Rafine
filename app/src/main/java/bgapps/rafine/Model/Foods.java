package bgapps.rafine.Model;

public class Foods {

    private String foodName, foodCal;

    public Foods() {
    }

    public Foods(String foodName, String foodCal) {
        this.foodName = foodName;
        this.foodCal = foodCal;
    }


    public String getFoodName() {
        return foodName;
    }

    public String getFoodCal() {
        return foodCal;
    }


    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setFoodCal(String foodCal) {
        this.foodCal = foodCal;
    }
}
