package za.co.research.mahlaza.zulu.model;

public interface SpecialMultipleOfTenControler {
    String getStem(int number) throws Exception;
    String getPrefix(int number, boolean isPlural) throws Exception;
}
