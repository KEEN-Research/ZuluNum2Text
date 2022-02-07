package za.co.research.mahlaza;

import za.co.mahlaza.research.grammarengine.base.models.feature.NounClass;
import za.co.research.mahlaza.zulu.IsiZuluNumberVerbaliser;
import za.co.research.mahlaza.zulu.NumCategory;

public class Main {
    public static void main(String[] args) {

        String[] ZuluClasses = {"1", "1a", "2", "2a", "3", "4", "5", "6", "7", "8", "9", "10", "11", "14", "15"};
        for (String zuNC : ZuluClasses) {
            NounClass class4 = NounClass.getNounClass(zuNC);
            IsiZuluNumberVerbaliser verbaliser = new IsiZuluNumberVerbaliser();
            for (int i = 1; i < 30; i++) {
                try {
                    //System.out.println(i + " - " + verbaliser.getText(i, NumCategory.Adverb));
                    System.out.println("NC = " + class4 + ", " + i + " - " + verbaliser.getText(i, class4, NumCategory.Collective));
                }
                catch (Exception e) {
                    //e.printStackTrace();
                }
            }
            System.out.println("================================\n");
        }
    }
}
