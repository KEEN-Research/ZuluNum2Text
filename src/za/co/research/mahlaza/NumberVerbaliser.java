package za.co.research.mahlaza;

import za.co.mahlaza.research.grammarengine.base.models.feature.NounClass;
import za.co.research.mahlaza.zulu.NumCategory;

public interface NumberVerbaliser {
    String getText(int number, NumCategory category) throws Exception;
    String getText(int number, NounClass nounClass, NumCategory category) throws Exception;
}
