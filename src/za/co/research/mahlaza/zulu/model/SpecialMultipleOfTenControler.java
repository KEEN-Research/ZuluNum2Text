package za.co.research.mahlaza.zulu.model;

public interface SpecialMultipleOfTenControler {
    String getStem(int number) throws Exception;
    String getPrefix1(int number, boolean isPlural, boolean includeAgreementMark) throws Exception;
    String getPrefix2(int number, boolean isPlural, boolean includeAgreementMark) throws Exception;
}
