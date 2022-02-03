package za.co.research.mahlaza.zulu;

import za.co.mahlaza.research.grammarengine.base.models.feature.ConcordType;
import za.co.mahlaza.research.grammarengine.base.models.feature.NounClass;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluConcordMapper;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluMorphophonoAlternator;
import za.co.research.mahlaza.NumberVerbaliser;
import za.co.research.mahlaza.zulu.model.AdverbSpecialMultipleOfTenController;
import za.co.research.mahlaza.zulu.model.CardinalSpecialMultipleOfTenControler;
import za.co.research.mahlaza.zulu.model.CollectiveSpecialMultipleOfTenController;
import za.co.research.mahlaza.zulu.model.OrdinalSpecialMultipleOfTenControler;
import java.security.InvalidParameterException;
import java.util.HashMap;

public class IsiZuluNumberVerbaliser implements NumberVerbaliser {

    private HashMap<Integer, String> defaultStems;
    private ZuluMorphophonoAlternator phonConditioner = new ZuluMorphophonoAlternator();
    private ZuluConcordMapper concordMapper = new ZuluConcordMapper();

    private CardinalSpecialMultipleOfTenControler cardinal10sController = new CardinalSpecialMultipleOfTenControler();
    private OrdinalSpecialMultipleOfTenControler ordinal10sController = new OrdinalSpecialMultipleOfTenControler();
    private CollectiveSpecialMultipleOfTenController collective10sController = new CollectiveSpecialMultipleOfTenController();
    private AdverbSpecialMultipleOfTenController adverb10sController = new AdverbSpecialMultipleOfTenController();

    public IsiZuluNumberVerbaliser() {
        defaultStems = new HashMap<>();

        defaultStems.put(1, "nye");
        defaultStems.put(2, "bili");
        defaultStems.put(3, "thathu");
        defaultStems.put(4, "ne");
        defaultStems.put(5, "hlanu");
        defaultStems.put(6, "thupha");
        defaultStems.put(7, "khombisa");
        defaultStems.put(8, "shiyangalombini");
        defaultStems.put(9, "shiyangalolunye");
        defaultStems.put(10, "shumi");
        defaultStems.put(100, "khulu");
        defaultStems.put(1000, "nkulungwane");
    }

    @Override
    public String getText(int number, NumCategory category) throws Exception {
        String numAstext = "";

        if (category == NumCategory.Cardinal || category == NumCategory.Ordinal) {
            numAstext = "isi" + getStem(number);
        }
        else if (category == NumCategory.Adverb) {
            if (number > 0 && number < 6) {
                numAstext = "ka" + getStem(number);
            }
            else if (number > 5 && number < 10) {
                numAstext = "kasi" + getStem(number);
            }
            else if (number == 10 || number == 100) {
                numAstext = "kali" + getStem(number);
            }
            else if (number == 1000) {
                numAstext = "kayi" + getStem(number);
            }
            else {
                int[] unqstems = {10, 100, 1000};
                for (int i=0; i < unqstems.length; i++) {
                        if (unqstems[i] > number) {
                            int nearest10s = unqstems[i-1];
                            int remainder = number % nearest10s;
                            int num10sVal = (number - remainder) / nearest10s;
                            boolean usePlural = num10sVal > 1;

                            String word1Prefix = getNearest10sPrefix(nearest10s, NumCategory.Adverb, usePlural);
                            String word1 = getNearest10sWord(nearest10s, NumCategory.Adverb, usePlural);
                            numAstext = combine(word1Prefix , word1);


                            if (usePlural) {
                                String word2Prefix = getNumOf10sPrefix(num10sVal);
                                String word2Stem = getStem(num10sVal);
                                numAstext = numAstext + " "+combine(word2Prefix, word2Stem);
                            }

                            if (remainder > 0) {
                                String word3Remainder = "";
                                if (remainder < 6) {
                                    word3Remainder = getStem(remainder);
                                }
                                else if (remainder > 5 && remainder < 10) {
                                    word3Remainder = getNumberAsNoun(remainder);
                                }
                                else {
                                    word3Remainder = getText(remainder, NumCategory.Adverb);
                                }
                                numAstext = numAstext + " " + combine("na", word3Remainder);
                            }
                            break;
                        }
                }
            }
        }
        else {
            throw new InvalidParameterException("getText(num, category) does not support the category = "+category);
        }

        return numAstext;
    }

    @Override
    public String getText(int number, NounClass nounClass, NumCategory category) throws Exception {
        String numAstext = "";

        if (ifHasUniqueStem(number)) {
            String stem = getStem(number);
            if (category == NumCategory.Cardinal) {
                ConcordType concType = ConcordType.getConcordType("AdjectivalConcord");
                String concVal = concordMapper.getConcordValue(nounClass, concType);
                numAstext = combine(concVal, stem);
            }
            else if (category == NumCategory.Ordinal) {
                ConcordType concType = ConcordType.getConcordType("PossessiveConcord");
                String concVal = concordMapper.getConcordValue(nounClass, concType);
                numAstext = combine(concVal, stem);
            }
            else if (category == NumCategory.Collective) {
                String basicPref = getBasicPrefix(nounClass);
                String unnasalisedBasicPref = removeNasals(basicPref);
                String unfinalisedPrefix = combine(unnasalisedBasicPref, "o");
                String prefix = combine(unfinalisedPrefix, basicPref);
                numAstext = combine(prefix, stem);
            }
            else {
                throw new IllegalArgumentException("The getText(number, nounClass, category) method does not support the category = "+category);
            }
        }
        else {
            int[] unqstems = {10, 100, 1000};
            for (int i=0; i < unqstems.length; i++) {
                if (unqstems[i] > number) {
                    int nearest10s = unqstems[i-1];
                    int remainder = number % nearest10s;
                    int num10sVal = (number - remainder) / nearest10s;
                    boolean usePlural = num10sVal > 1;

                    String lead = "";
                    if (category == NumCategory.Cardinal) {
                        ConcordType concType = ConcordType.getConcordType("SubjectivalConcord");
                        lead = concordMapper.getConcordValue(nounClass, concType);
                    }
                    else if (category == NumCategory.Ordinal) {
                        ConcordType concType = ConcordType.getConcordType("PossessiveConcord");
                        lead = concordMapper.getConcordValue(nounClass, concType);
                    }
                    else if (category == NumCategory.Collective) {
                        ConcordType concType = ConcordType.getConcordType("AdjectivalConcord");
                        lead = concordMapper.getConcordValue(nounClass, concType);
                    }
                    else {
                        throw new IllegalArgumentException("The getText(number, nounClass, category) method does not support the category = "+category);
                    }

                    String word1Prefix = getNearest10sPrefix(nearest10s, category, usePlural);
                    String word1Stem = getStem(nearest10s);
                    String word1 = combine(combine(lead, word1Prefix), word1Stem);
                    numAstext = word1;

                    if (usePlural) {
                        String word2Prefix = getNumOf10sPrefix(num10sVal);
                        String word2Stem = getStem(num10sVal);
                        String word2 = combine(word2Prefix, word2Stem);
                        numAstext = numAstext + " " + word2;
                    }

                    if (remainder > 0) {
                        String word3Remainder = "";
                        if (remainder < 6) {
                            word3Remainder = getStem(remainder);
                        }
                        else if (remainder > 5 && remainder < 10) {
                            word3Remainder = getNumberAsNoun(remainder);
                        }
                        else {
                            word3Remainder = getText(remainder, nounClass, category);
                        }
                        numAstext = numAstext + " " + combine("na", word3Remainder);
                    }
                    break;
                }
            }
        }

        return numAstext;
    }

    private String combine(String lmorph, String rmorph) {
        String newValue = phonConditioner.joinMorpheme(lmorph, rmorph);
        return newValue;
    }

    public boolean ifHasUniqueStem(int number) {
        return defaultStems.keySet().contains(number);
    }

    public String getStem(int number) throws Exception {
        String stem = "";
        if (!ifHasUniqueStem(number)) {
            throw new InvalidParameterException(number + " does not have a unique stem, hence you cannot call getStem()");
        } else {
            stem = defaultStems.get(number);
        }
        return stem;
    }

    public String getBasicPrefix(NounClass nounClass) {
        String basicPrefix = "";

        ConcordType sbjConc = ConcordType.getConcordType("SubjectivalConcord");
        String subjCon = concordMapper.getConcordValue(nounClass, sbjConc);
        if (subjCon.length() > 1) {
            basicPrefix = subjCon.substring(1);
        } else {
            //TODO: this is not correct. Classes with subjc with one letter have other concords, use them and remove the leading letter.
            basicPrefix = subjCon;
        }
        return basicPrefix;
    }

    public String removeNasals(String morpheme) {
        String unnasalisedMorpheme = morpheme;
        if (morpheme.endsWith("n")) {
            unnasalisedMorpheme = morpheme.substring(0, morpheme.length()-1);
        }
        else if (morpheme.endsWith("m")) {
            unnasalisedMorpheme = morpheme.substring(0, morpheme.length()-1);
        }
        return unnasalisedMorpheme;
    }

    public String getNearest10sWord(int nearest10s, NumCategory category, boolean usePlural) throws Exception {
        String word = "";

        String stem = getStem(nearest10s);
        if (nearest10s == 1000) {
            String prefix = usePlural ? "izi" : "";
            word = prefix + stem;
        }
        else if (nearest10s == 10 || nearest10s == 100) {
            String prefix = usePlural ? "ama" : "";
            word = prefix + stem;
        }
        else {
            throw new IllegalArgumentException("The getPrefix method does not support the value "+nearest10s);
        }

        return word;
    }

    //called getPrefix in paper. Line 24
    public String getNearest10sPrefix(int nearest10s, NumCategory category, boolean usePlural) throws Exception {
        String prefix = "";
        switch (category) {
            case Cardinal: {
                prefix = cardinal10sController.getPrefix(nearest10s, usePlural);
                break;
            }
            case Ordinal: {
                prefix = ordinal10sController.getPrefix(nearest10s, usePlural);
                break;
            }
            case Collective: {
                prefix = collective10sController.getPrefix(nearest10s, usePlural);
                break;
            }
            case Adverb: {
                prefix = adverb10sController.getPrefix(nearest10s, usePlural);
                break;
            }
        }
        return prefix;
    }

    public String getNumOf10sPrefix(int num10sVal) {
        String prefix = "";
        if (num10sVal > 1 && num10sVal < 6) {
            prefix = "ama";
        }
        else if (num10sVal > 5 && num10sVal < 10) {
            prefix = "ayi";
        }
        else {
            throw new InvalidParameterException("getNumOf10sPrefix(number) does no support number = "+num10sVal);
        }
        return prefix;
    }

    public String getNumberAsNoun(int number) throws Exception {
        String stem = getStem(number);
        String prefix = "isi";
        return prefix + stem;
    }
}
