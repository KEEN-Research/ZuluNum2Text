package za.co.research.mahlaza.zulu;

import org.apache.commons.cli.*;
import za.co.mahlaza.research.grammarengine.base.models.feature.NounClass;
import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import static za.co.research.mahlaza.zulu.NumCategory.*;

public class CmdSurveyTextGenerator {

    public static void main(String[] args) throws Exception {
        Random generator = new Random();

        Options cmdArgs = new Options();

        Option numberOpt = new Option("f", "filename", true, "The name of the csv file to contain the generated test strings");
        numberOpt.setRequired(true);
        cmdArgs.addOption(numberOpt);

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(cmdArgs, args);
            if (cmd.hasOption("filename")) {
                String outputFilename = cmd.getOptionValue("filename");

                //generating random numbers to be verbalised
                int[] unqstems = {1, 2, 3, 5, 6, 7, 8, 9, 10, 100, 1000};

                List<Integer> generatedNumbers = new LinkedList<>();
                while (generatedNumbers.size() != 5) {
                    //generating numbers with non-unique stem
                    int number = unqstems[generator.nextInt(unqstems.length)];
                    if (!generatedNumbers.stream().anyMatch(val -> val == number)) {
                        generatedNumbers.add(number);
                    }
                }

                while (generatedNumbers.size() != 10) {
                    //generating numbers with non-unique stem
                    int randomNum = ThreadLocalRandom.current().nextInt(10, 10000);
                    if (!Arrays.stream(unqstems).anyMatch(val -> val == randomNum)) {
                        generatedNumbers.add(randomNum);
                    }
                }

                IsiZuluNumberVerbaliser verbaliser = new IsiZuluNumberVerbaliser();

                File csvOutputFile = new File(outputFilename);
                try (PrintWriter writer = new PrintWriter(csvOutputFile)) {

                    writer.println("Numeral, Verbaliser number, Category, Include agreement marker, Noun Class");

                    //verbalising numbers with no agreement markers
                    for (Integer number : generatedNumbers) {
                        String cardinalNum = verbaliser.getText(number, Cardinal);
                        String ordinalNum = verbaliser.getText(number, Ordinal);
                        String setOfItemsNum = verbaliser.getText(number, SetOfItems);
                        String adverbNum = verbaliser.getText(number, Adverb);

                        writer.println(String.format("%s, %s, Cardinal, No, N/A", number, cardinalNum));
                        writer.println(String.format("%s, %s, Ordinal, No, N/A", number, ordinalNum));
                        writer.println(String.format("%s, %s, Set-of-items, No, N/A", number, setOfItemsNum));
                        writer.println(String.format("%s, %s, Adverb, No, N/A", number, adverbNum));
                    }

                    //verbalising numbers with agreement markers
                    for (Integer number : generatedNumbers) {
                        String cardinalNum = verbaliser.getText(number, NounClass.getNounClass("2"), Cardinal);
                        String ordinalNum = verbaliser.getText(number, NounClass.getNounClass("2"), Ordinal);
                        String setOfItemsNum = verbaliser.getText(number, NounClass.getNounClass("2"), SetOfItems);

                        writer.println(String.format("%s, %s, Cardinal, Yes, 2", number, cardinalNum));
                        writer.println(String.format("%s, %s, Ordinal, Yes, 2", number, ordinalNum));
                        writer.println(String.format("%s, %s, Set-of-items, Yes, 2", number, setOfItemsNum));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
