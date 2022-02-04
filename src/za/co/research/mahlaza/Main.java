package za.co.research.mahlaza;

import za.co.mahlaza.research.grammarengine.base.models.feature.NounClass;
import za.co.research.mahlaza.zulu.IsiZuluNumberVerbaliser;
import za.co.research.mahlaza.zulu.NumCategory;

public class Main {
    public static void main(String[] args) {
        NounClass class4 = NounClass.getNounClass("6");
        IsiZuluNumberVerbaliser verbaliser = new IsiZuluNumberVerbaliser();

        for (int i = 1; i < 30; i++) {
            try {
                //System.out.println(i + " - " + verbaliser.getText(i, NumCategory.Adverb));
                System.out.println("NC = " + class4 + ", " + i + " - " + verbaliser.getText(i, class4, NumCategory.Cardinal));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
