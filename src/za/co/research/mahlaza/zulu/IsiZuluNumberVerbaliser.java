package za.co.research.mahlaza.zulu;

import za.co.mahlaza.research.grammarengine.base.models.feature.ConcordType;
import za.co.mahlaza.research.grammarengine.base.models.feature.NounClass;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluConcordMapper;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluMorphophonoAlternator;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluNounClassPrefixResolver;
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
    private ZuluNounClassPrefixResolver zuluNounClassPrefixController = new ZuluNounClassPrefixResolver();


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

        if ((category == NumCategory.Cardinal || category == NumCategory.Ordinal) & number < 10) {
            numAstext = "isi" + getStem(number);
        }
        else if (category == NumCategory.Adverb && ifHasUniqueStem(number)) {
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
        }
        if (number > 9 && numAstext.isEmpty()) {
            int[] unqstems = {10, 100, 1000, 1000000};
            for (int i=0; i < unqstems.length; i++) {
                if (unqstems[i] > number) {
                    int nearest10s = unqstems[i-1];
                    int remainder = number % nearest10s;
                    int num10sVal = (number - remainder) / nearest10s;
                    boolean usePlural = num10sVal > 1;

                    String word1Prefix = getNearest10sPrefix(nearest10s, NumCategory.Adverb, usePlural);
                    String word1 = getNearest10sWord(nearest10s, NumCategory.Adverb, usePlural);

                    if (category == NumCategory.Adverb) {
                        numAstext = combine(word1Prefix , word1);
                    }
                    else {
                        numAstext = word1;
                    }


                    if (usePlural) {
                        String word2Prefix = getNumOf10sPrefix(nearest10s, num10sVal);
                        String word2Stem = "";
                        if (num10sVal < 6) {
                            word2Stem = getStem(num10sVal);
                        }
                        else if (num10sVal > 5 && num10sVal < 10) {
                            word2Stem = getNumberAsNoun(num10sVal);
                        }

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

        return numAstext;
    }

    @Override
    public String getText(int number, NounClass nounClass, NumCategory category) throws Exception {
        String numAstext = "";

        if (ifHasUniqueStem(number) && category == NumCategory.Cardinal && number < 10) {
            String stem = getStem(number);
            ConcordType concType = ConcordType.getConcordType("AdjectivalConcord");
            String concVal = concordMapper.getConcordValue(nounClass, concType);

            if (concVal.contains("/")) {
                String[] vals = concVal.split("/");
                concVal = vals[0]; //TODO when there are multiple values, how do you choose between them?
            }

            numAstext = combine(concVal, stem);
        }
        else if (ifHasUniqueStem(number) && category == NumCategory.Ordinal) {
            ConcordType concType = ConcordType.getConcordType("PossessiveConcord");
            String concVal = concordMapper.getConcordValue(nounClass, concType);
            String numAsNoun = getNumberAsNoun(number);
            if (number == 1) {
                numAstext = combine(concVal, "ukuqala");
            }
            else {
                numAstext = combine(concVal, numAsNoun);
            }
        }
        else  if (ifHasUniqueStem(number) && category == NumCategory.Collective  && number < 10) {
            String stem = getStem(number);
            if (number == 1) {
                throw new IllegalArgumentException("The "+category+" type does not support the the number = "+number);
            }

            String basicPref = zuluNounClassPrefixController.getBasicPrefix(nounClass.getNounClass());
            if (basicPref.contains("/")) {
                //TODO: if there are multiple basic prefixes, how do you chose between them?
                String[] prefVals = basicPref.split("/");
                basicPref = prefVals[0];
            }

            ConcordType concType = ConcordType.getConcordType("PossessiveConcord");
            String possConVal = concordMapper.getConcordValue(nounClass, concType);

            String unnasalisedBasicPref = removeNasals(basicPref);
            String unfinalisedPrefix = combine(possConVal, "o");
            String prefix = combine(unfinalisedPrefix, unnasalisedBasicPref);

            if (number > 5 && number < 10) {
                String modifiedPrefix = combine(prefix, "si");
                numAstext = combine(modifiedPrefix, stem);
            }
            else {
                numAstext = combine(prefix, stem);
            }
        }
        else {
            int[] unqstems = {10, 100, 1000, 1000000};
            for (int i=0; i < unqstems.length; i++) {
                if (unqstems[i] > number) {
                    int nearest10s = unqstems[i-1];
                    int remainder = number % nearest10s;
                    int num10sVal = (number - remainder) / nearest10s;
                    boolean usePlural = num10sVal > 1;

                    String lead = "";
                    if (category == NumCategory.Cardinal) {
                        ConcordType concType = ConcordType.getConcordType("AdjectivalConcord");
                        lead = concordMapper.getConcordValue(nounClass, concType);
                        if (lead.contains("/")) {
                            String[] vals = lead.split("/");
                            lead = vals[0]; //TODO when there are multiple values, how do you choose between them?
                        }
                    }
                    else if (category == NumCategory.Ordinal) {
                        ConcordType concType = ConcordType.getConcordType("PossessiveConcord");
                        lead = concordMapper.getConcordValue(nounClass, concType);
                    }
                    else if (category == NumCategory.Collective) {
                        ConcordType concType = ConcordType.getConcordType("AdjectivalConcord");
                        lead = concordMapper.getConcordValue(nounClass, concType);
                        if (lead.contains("/")) {
                            String[] leadVals = lead.split("/");
                            lead = leadVals[0];
                        }
                        lead = removeNasals(lead);
                    }
                    else {
                        throw new IllegalArgumentException("The getText(number, nounClass, category) method does not support the category = "+category);
                    }

                    String word1Prefix = getNearest10sPrefix(nearest10s, category, usePlural);
                    String word1Stem = getStem(nearest10s);
                    String word1 = combine(combine(lead, word1Prefix), word1Stem);
                    numAstext = word1;

                    if (usePlural) {
                        String word2Prefix = getNumOf10sPrefix(nearest10s, num10sVal);
                        String word2Stem = "";
                        if (num10sVal < 6) {
                            word2Stem = getStem(num10sVal);
                        }
                        else if (num10sVal > 5 && num10sVal < 10) {
                            word2Stem = getNumberAsNoun(num10sVal);
                        }

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

    private String combine(String lmorph, String rmorph) throws Exception {
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
        if (subjCon.length() > 1 && subjCon.matches("[aeiou].*")) {
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
            String prefix = usePlural ? "izi" : "i";
            word = prefix + stem;
        }
        else if (nearest10s == 10 || nearest10s == 100) {
            String prefix = usePlural ? "ama" : "i";
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

    public String getNumOf10sPrefix(int nearest, int num10sVal) {
        String prefix = "";
        if (nearest == 10 || nearest == 100) {
            if (num10sVal > 1 && num10sVal < 6) {
                prefix = "ama";
            }
            else if (num10sVal > 5 && num10sVal < 10) {
                prefix = "ayi";
            }
            else {
                throw new InvalidParameterException("getNumOf10sPrefix(number) does no support number = "+num10sVal);
            }
        }
        else if (nearest == 1000) {
            if (num10sVal == 3 || num10sVal == 5) {
                return "ezin";
            }
            else if (num10sVal > 1 && num10sVal < 6) {
                prefix = "ezim";
            }
            else if (num10sVal > 5 && num10sVal < 10) {
                prefix = "eziyi";
            }
            else {
                throw new InvalidParameterException("getNumOf10sPrefix(number) does no support number = "+num10sVal);
            }
        }
        return prefix;
    }

    public String getNumberAsNoun(int number) throws Exception {
        String stem = getStem(number);

        String prefix = "";
        if (number == 10) {
            prefix = "i";
        }
        else {
            prefix = "isi";
        }

        return prefix + stem;
    }
}
