package dataStructure.enumStudy;

public class EnumStructure {
    public static void main(String[] args) {
        Day day = Day.MONDAY;

        Meal[] meal = Meal.values();
        for (Meal meal1 : meal) {
            for (Food value : meal1.getValues()) {
                System.out.println(value);
            }
            for (Meal value : meal1.values()) {
                System.out.println(value);
            }
        }

        common.common.intervalLine2();

        Food[] food = Food.Appetizer.class.getEnumConstants();
        for (Food food1 : food) {
            System.out.println(food1);
        }
    }
}


enum Day {
    MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY, FRIDAY, SATURDAY, SUNDAY
}