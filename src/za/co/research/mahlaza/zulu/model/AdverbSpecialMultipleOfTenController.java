package za.co.research.mahlaza.zulu.model;

public class AdverbSpecialMultipleOfTenController implements SpecialMultipleOfTenControler {

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
        String prefix = "";
        if (includeAgreementMark) {
            prefix = "";
        }
        else {
            if (number == 10 || number == 100) {
                prefix = isPlural? "kanga" : "kali";
            }
            else if (number == 1000) {
                prefix = "";
            }
        }
        return prefix;
    }

    @Override
    public String getPrefix2(int number, boolean isPlural, boolean includeAgreementMark) throws Exception {
        String prefix = "";
        if (includeAgreementMark) {
            prefix = "";
        }
        else {
            if (number == 10 || number == 100) {
                prefix = isPlural? "ama" : "i";
            }
            else if (number == 1000) {
                prefix = isPlural? "izi" : "i";
            }
        }
        return prefix;
    }
}
