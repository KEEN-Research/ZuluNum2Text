package za.co.research.mahlaza.zulu.model;

public class OrdinalSpecialMultipleOfTenControler implements SpecialMultipleOfTenControler {

    @Override
    public String getStem(int number) {
        String stem = "";
        if (number == 10) {
            stem = "shumi";
        }
        else if (number == 100) {
            stem = "khulu";
        }
        else if (number == 1000) {
            return "nkulungwane";
        }
        else {
            throw new IllegalArgumentException("The getStem method does not support the value "+number);
        }
        return stem;
    }

    @Override
    public String getPrefix(int number, boolean isPlural) {
        String prefix = "";
        if (number == 1000) {
            prefix = isPlural? "izi" : "i";
        }
        else if (number == 10 || number == 100) {
            prefix = isPlural? "ma" : "i";
        }
        else {
            throw new IllegalArgumentException("The getPrefix method does not support the value "+number);
        }
        return prefix;
    }

}
