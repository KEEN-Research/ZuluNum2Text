package za.co.research.mahlaza.zulu;

import za.co.mahlaza.research.grammarengine.base.models.feature.ConcordType;
import za.co.mahlaza.research.grammarengine.base.models.feature.NounClass;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluConcordMapper;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluMorphophonoAlternator;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluNounClassPrefixResolver;
import za.co.research.mahlaza.NumberVerbaliser;
import za.co.research.mahlaza.zulu.model.AdverbSpecialMultipleOfTenController;
import za.co.research.mahlaza.zulu.model.CardinalSpecialMultipleOfTenControler;
import za.co.research.mahlaza.zulu.model.SetOfItemsSpecialMultipleOfTenController;
import za.co.research.mahlaza.zulu.model.OrdinalSpecialMultipleOfTenControler;
import java.security.InvalidParameterException;
import java.util.*;

public class IsiZuluNumberVerbaliser implements NumberVerbaliser {

    private HashMap<Integer, String> defaultStems;
    private ZuluMorphophonoAlternator phonConditioner = new ZuluMorphophonoAlternator();
    private ZuluConcordMapper concordMapper = new ZuluConcordMapper();

    private CardinalSpecialMultipleOfTenControler cardinal10sController = new CardinalSpecialMultipleOfTenControler();
    private OrdinalSpecialMultipleOfTenControler ordinal10sController = new OrdinalSpecialMultipleOfTenControler();
    private SetOfItemsSpecialMultipleOfTenController collective10sController = new SetOfItemsSpecialMultipleOfTenController();
    private AdverbSpecialMultipleOfTenController adverb10sController = new AdverbSpecialMultipleOfTenController();
    private ZuluNounClassPrefixResolver zuluNounClassPrefixController = new ZuluNounClassPrefixResolver();

    private Queue<String> morphemeListForDebugging = new LinkedList<>();

    public IsiZuluNumberVerbaliser() {
        defaultStems = new HashMap<>();

        defaultStems.put(1, "nye");
        defaultStems.put(2, "bili");
        defaultStems.put(3, "thathu");
        defaultStems.put(4, "ne");
        defaultStems.put(5, "hlanu");
        defaultStems.put(6, "thupha");
        defaultStems.put(7, "khombisa");
        defaultStems.put(8, "shiyagalombini");
        defaultStems.put(9, "shiyagalolunye");
        defaultStems.put(10, "shumi");
        defaultStems.put(100, "khulu");
        defaultStems.put(1000, "nkulungwane");
    }

    protected Queue<String> getDebugMorphemes() {
        return morphemeListForDebugging;
    }

    @Override
    public String getText(int number, NumCategory category) throws Exception {
        return getText(number, category, true, true);
    }

    @Override
    public String getText(int number, NounClass nounClass, NumCategory category) throws Exception {
        return getText(number, nounClass, category, true, true);
    }

    @Override
    public String getText(int number, NumCategory category, boolean debugMode, boolean firstFuncCall) throws Exception {
        String numAstext = "";

        if ((category == NumCategory.Cardinal || category == NumCategory.Ordinal || category == NumCategory.SetOfItems) & number < 10) {
            numAstext = "isi" + getStem(number);
            if (debugMode) {
                morphemeListForDebugging.add("isi");
                morphemeListForDebugging.add(getStem(number));
            }
        }
        else if (category == NumCategory.Adverb && ifHasUniqueStem(number)) {
            if (number > 0 && number < 6) {
                numAstext = "ka" + getStem(number);
                if (debugMode) {
                    morphemeListForDebugging.add("ka");
                    morphemeListForDebugging.add(getStem(number));
                }
            }
            else if (number > 5 && number < 10) {
                numAstext = "kasi" + getStem(number);
                if (debugMode) {
                    morphemeListForDebugging.add("kasi");
                    morphemeListForDebugging.add(getStem(number));
                }
            }
            else if (number == 10 || number == 100) {
                if (firstFuncCall) {
                    numAstext =  numAstext + "ka";
                    if (debugMode) {
                        morphemeListForDebugging.add("ka");
                    }
                }
                numAstext = numAstext + "li" + getStem(number);
                if (debugMode) {
                    morphemeListForDebugging.add("li");
                    morphemeListForDebugging.add(getStem(number));
                }
            }
            else if (number == 1000) {
                numAstext = "kayi" + getStem(number);
                if (debugMode) {
                    morphemeListForDebugging.add("kayi");
                    morphemeListForDebugging.add(getStem(number));
                }
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

                    if (category == NumCategory.Adverb) {
                        String segment1 = getNearest10sPrefix(1, nearest10s, NumCategory.Adverb, usePlural, false);
                        String segment2 = getNearest10sPrefix(2, nearest10s, NumCategory.Adverb, usePlural, false);

                        numAstext = combine(segment1, segment2);
                        String stem = getStem(nearest10s);
                        numAstext = combine(numAstext, stem);

                        if (debugMode) {
                            morphemeListForDebugging.add(segment1);
                            morphemeListForDebugging.add(segment2);
                            morphemeListForDebugging.add(stem);
                        }
                    }
                    else {
                        String segment2 = getNearest10sPrefix(2, nearest10s, NumCategory.Adverb, usePlural, false);
                        String stem = getStem(nearest10s);
                        numAstext = combine(segment2, stem);

                        if (debugMode) {
                            morphemeListForDebugging.add(stem);
                            morphemeListForDebugging.add(segment2);
                        }
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
                        if (debugMode) {
                            morphemeListForDebugging.add(" ");
                            morphemeListForDebugging.add(word2Prefix);
                            morphemeListForDebugging.add(word2Stem);
                        }
                    }

                    if (remainder > 0) {
                        if (debugMode) {
                            morphemeListForDebugging.add(" ");
                            if (firstFuncCall) {
                                morphemeListForDebugging.add("na");
                            }
                        }
                        String word3Remainder = "";
                        if (remainder < 6) {
                            word3Remainder = getStem(remainder);
                            if (debugMode) {
                                morphemeListForDebugging.add(word3Remainder);
                            }
                        }
                        else if (remainder > 5 && remainder < 10) {
                            word3Remainder = getNumberAsNoun(remainder);
                            if (debugMode) {
                                morphemeListForDebugging.add(word3Remainder);
                            }
                        }
                        else {
                            word3Remainder = getText(remainder, category, debugMode, false);
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
    public String getText(int number, NounClass nounClass, NumCategory category, boolean debugMode, boolean firstFunctionCall) throws Exception {
        String numAstext = "";

        if (ifHasUniqueStem(number) && category == NumCategory.Cardinal && number < 10) {
            String stem = getStem(number);
            ConcordType concType = ConcordType.getConcordType("AdjectivalConcord");
            String concVal = concordMapper.getConcordValue(nounClass, concType);

            if (concVal.contains("/")) {
                String[] vals = concVal.split("/");
                concVal = vals[0]; //TODO when there are multiple values, how do you choose between them?
            }

            if (number > 5  && number < 10) {
                concVal = combine(concVal, "ayisi");
            }

            numAstext = combine(concVal, stem);
            if (debugMode) {
                morphemeListForDebugging.add(concVal);
                morphemeListForDebugging.add(stem);
            }
        }
        else if (ifHasUniqueStem(number) && category == NumCategory.Ordinal) {
            ConcordType concType = ConcordType.getConcordType("PossessiveConcord");
            String concVal = concordMapper.getConcordValue(nounClass, concType);
            if (!firstFunctionCall) {
                concVal = "";
            }
            String numAsNoun = getNumberAsNoun(number);
            if (number == 1) {
                numAstext = combine(concVal, "ukuqala");
                if (debugMode) {
                    morphemeListForDebugging.add(concVal);
                    morphemeListForDebugging.add("ukuqala");
                }
            }
            else {
                numAstext = combine(concVal, numAsNoun);
                if (debugMode) {
                    morphemeListForDebugging.add(concVal);
                    morphemeListForDebugging.add(numAsNoun);
                }
            }
        }
        else  if (ifHasUniqueStem(number) && category == NumCategory.SetOfItems && number < 10) {
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
                if (debugMode) {
                    morphemeListForDebugging.add(prefix);
                    morphemeListForDebugging.add("si");
                    morphemeListForDebugging.add(stem);
                }
            }
            else {
                numAstext = combine(prefix, stem);
                if (debugMode) {
                    morphemeListForDebugging.add(prefix);
                    morphemeListForDebugging.add(stem);
                }
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
                    else if (category == NumCategory.SetOfItems) {
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

                    if (!firstFunctionCall) {
                        lead = "";
                    }

                    String word1Prefix = getNearest10sPrefix(2, nearest10s, category, usePlural, true);
                    String word1Stem = getStem(nearest10s);
                    String x = combine(lead, word1Prefix);
                    String word1 = combine(x, word1Stem);
                    numAstext = word1;
                    if (debugMode) {
                        morphemeListForDebugging.add(lead);
                        morphemeListForDebugging.add(word1Prefix);
                        morphemeListForDebugging.add(word1Stem);
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

                        String word2 = combine(word2Prefix, word2Stem);
                        numAstext = numAstext + " " + word2;
                        if (debugMode) {
                            morphemeListForDebugging.add(" ");
                            morphemeListForDebugging.add(word2Prefix);
                            morphemeListForDebugging.add(word2Stem);
                        }
                    }

                    if (remainder > 0) {
                        if (debugMode) {
                            morphemeListForDebugging.add(" ");
                            morphemeListForDebugging.add("na");
                        }
                        String word3Remainder = "";
                        if (remainder < 6) {
                            word3Remainder = getStem(remainder);
                            if (debugMode) {
                                morphemeListForDebugging.add(word3Remainder);
                            }
                        }
                        else if (remainder > 5 && remainder < 10) {
                            word3Remainder = getNumberAsNoun(remainder);
                            if (debugMode) {
                                morphemeListForDebugging.add(word3Remainder);
                            }
                        }
                        else {
                            word3Remainder = getText(remainder, nounClass, category, debugMode, false);
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

    public String getNearest10sPrefix(int prefixCount, int nearest10s, NumCategory category, boolean usePlural, boolean useAgreement) throws Exception {
        String prefix = "";
        if (prefixCount == 1) {
            switch (category) {
                case Cardinal: {
                    prefix = cardinal10sController.getPrefix1(nearest10s, usePlural, useAgreement);
                    break;
                }
                case Ordinal: {
                    prefix = ordinal10sController.getPrefix1(nearest10s, usePlural, useAgreement);
                    break;
                }
                case SetOfItems: {
                    prefix = collective10sController.getPrefix1(nearest10s, usePlural, useAgreement);
                    break;
                }
                case Adverb: {
                    prefix = adverb10sController.getPrefix1(nearest10s, usePlural, useAgreement);
                    break;
                }
            }
        }
        else {
            switch (category) {
                case Cardinal: {
                    prefix = cardinal10sController.getPrefix2(nearest10s, usePlural, useAgreement);
                    break;
                }
                case Ordinal: {
                    prefix = ordinal10sController.getPrefix2(nearest10s, usePlural, useAgreement);
                    break;
                }
                case SetOfItems: {
                    prefix = collective10sController.getPrefix2(nearest10s, usePlural, useAgreement);
                    break;
                }
                case Adverb: {
                    prefix = adverb10sController.getPrefix2(nearest10s, usePlural, useAgreement);
                    break;
                }
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
