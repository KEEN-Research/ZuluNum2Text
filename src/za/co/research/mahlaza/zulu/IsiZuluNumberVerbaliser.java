package za.co.research.mahlaza.zulu;

import za.co.mahlaza.research.grammarengine.base.models.feature.ConcordType;
import za.co.mahlaza.research.grammarengine.base.models.feature.NounClass;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluConcordMapper;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluMorphophonoAlternator;
import za.co.research.mahlaza.NumberVerbaliser;

import java.security.InvalidParameterException;
import java.util.HashMap;

public class IsiZuluNumberVerbaliser implements NumberVerbaliser {

    private HashMap<Integer, String> defaultStems;
    private ZuluMorphophonoAlternator phonConditioner;
    private ZuluConcordMapper concordMapper;

    public IsiZuluNumberVerbaliser() {
        concordMapper = new ZuluConcordMapper();
        phonConditioner = new ZuluMorphophonoAlternator();
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
    public String getText(int number, NumCategory category) {
        String numAstext = "";
        //TODO: implement this
        return numAstext;
    }

    @Override
    public String getText(int number, NounClass nounClass, NumCategory category) {
        String numAstext = "";
        //TODO: implement this
        return numAstext;
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
        //TODO: implement this
        return basicPrefix;
    }

    public String removeNasals(String morpheme) {
        String unnasalisedMorpheme = "";
        //TODO: implement this
        return unnasalisedMorpheme;
    }

    public String getNearest10sWord(int nearest10s, boolean usePlural) {
        String word = "";
        //TODO: implement this
        return word;
    }

    public String getNearest10sPrefix(int nearest10s, NumCategory category, boolean usePlural) {
        String prefix = "";

        return prefix;
    }

    public String getConjuctiveMorpheme(String followingMorpheme) {
        String conjuction = "";
        //TODO: Implement this
        return conjuction;
    }

}
