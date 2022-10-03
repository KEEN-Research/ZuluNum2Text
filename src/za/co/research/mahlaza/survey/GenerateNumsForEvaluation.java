package za.co.research.mahlaza.survey;

import za.co.mahlaza.research.grammarengine.base.models.feature.NounClass;
import za.co.research.mahlaza.zulu.IsiZuluNumberVerbaliser;
import za.co.research.mahlaza.zulu.NumCategory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class GenerateNumsForEvaluation {

    public static void main(String[] args) {
        int minRange = 1;
        int maxRange = 9999;

        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 100, 1000};
        List<Integer> numsUniqueStems = Arrays.stream(nums).boxed().collect(Collectors.toList());

        //selecting  numbers with unique stems
        Collections.shuffle(numsUniqueStems);
        List<Integer> chosenNumsWithUniqueStems = numsUniqueStems.subList(0, 5);

        //selecting  numbers with unique stems
        Random rn = new Random();
        List<Integer> chosenNumsWithoutUniqueStems = new ArrayList<>();
        while (chosenNumsWithoutUniqueStems.size() < 5) {
            int randomNumber = minRange + rn.nextInt(maxRange);
            if (!numsUniqueStems.contains(randomNumber) && !chosenNumsWithoutUniqueStems.contains(randomNumber)) {
                chosenNumsWithoutUniqueStems.add(randomNumber);
            }
        }

        IsiZuluNumberVerbaliser verbaliser = new IsiZuluNumberVerbaliser();
        NounClass nounClass2 = NounClass.getNounClass("2");

        String csvOutputFilename = "surveydata.csv";
        File csvOutputFile = new File(csvOutputFilename);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println(String.format("Numeral, Number, Category, Has Agreement Marker, AgreementNounAndClass"));

            //verbalising numbers without agreement markers
            for (Integer num : chosenNumsWithUniqueStems) {
                try {
                    String cardinalNum1 = verbaliser.getText(num, NumCategory.Cardinal);
                    pw.println(String.format("%s, %s, Cardinal, No, N/A", num, cardinalNum1));
                    String ordinalNum1 = verbaliser.getText(num, NumCategory.Ordinal);
                    pw.println(String.format("%s, %s, Ordinal, No, N/A", num, ordinalNum1));
                    String adverbNum1 = verbaliser.getText(num, NumCategory.Adverb);
                    pw.println(String.format("%s, %s, Adverb, No, N/A", num, adverbNum1));
                    String collecNum1 = verbaliser.getText(num, NumCategory.Collective);
                    pw.println(String.format("%s, %s, Set-of-items, No, N/A", num, collecNum1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (Integer num : chosenNumsWithoutUniqueStems) {
                try {
                    String cardinalNum1 = verbaliser.getText(num, NumCategory.Cardinal);
                    pw.println(String.format("%s, %s, Cardinal, No, N/A", num, cardinalNum1));
                    String ordinalNum1 = verbaliser.getText(num, NumCategory.Ordinal);
                    pw.println(String.format("%s, %s, Ordinal, No, N/A", num, ordinalNum1));
                    String adverbNum1 = verbaliser.getText(num, NumCategory.Adverb);
                    pw.println(String.format("%s, %s, Adverb, No, N/A", num, adverbNum1));
                    String collecNum1 = verbaliser.getText(num, NumCategory.Collective);
                    pw.println(String.format("%s, %s, Set-of-items, No, N/A", num, collecNum1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //verbalising numbers with agreement markers
            for (Integer num : chosenNumsWithUniqueStems) {
                try {
                    String cardinalNum1 = verbaliser.getText(num, nounClass2, NumCategory.Cardinal);
                    pw.println(String.format("%s, %s, Cardinal, Yes, ababhali-2", num, cardinalNum1));
                    String ordinalNum1 = verbaliser.getText(num, nounClass2, NumCategory.Ordinal);
                    pw.println(String.format("%s, %s, Ordinal, Yes, ababhali-2", num, ordinalNum1));
                    String collNum1 = verbaliser.getText(num, nounClass2, NumCategory.Collective);
                    pw.println(String.format("%s, %s, Set-of-items, Yes, ababhali-2", num, collNum1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (Integer num : chosenNumsWithoutUniqueStems) {
                try {
                    String cardinalNum1 = verbaliser.getText(num, nounClass2, NumCategory.Cardinal);
                    pw.println(String.format("%s, %s, Cardinal, Yes, ababhali-2", num, cardinalNum1));
                    String ordinalNum1 = verbaliser.getText(num, nounClass2, NumCategory.Ordinal);
                    pw.println(String.format("%s, %s, Ordinal, Yes, ababhali-2", num, ordinalNum1));
                    String collNum1 = verbaliser.getText(num, nounClass2, NumCategory.Collective);
                    pw.println(String.format("%s, %s, Set-of-items, Yes, ababhali-2", num, collNum1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
