package za.co.research.mahlaza.zulu.model;

public class CardinalSpecialMultipleOfTenControler implements SpecialMultipleOfTenControler {

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
    public String getPrefix1(int number, boolean isPlural, boolean includeAgreementMark) throws Exception {
        return "";
    }

    @Override
    public String getPrefix2(int number, boolean isPlural, boolean includeAgreementMark) {
        String prefix = "";
        if (includeAgreementMark) {
            if (number == 1000) {
                prefix = isPlural? "zi" : "yi";
            }
            else if (number == 10 || number == 100) {
                prefix = isPlural? "ma" : "yi";
            }
        }
        else {
            if (number == 1000) {
                prefix = isPlural? "izi" : "i";
            }
            else if (number == 10 || number == 100) {
                prefix = isPlural? "ama" : "i";
            }
        }
        return prefix;
    }

}
