package za.co.research.mahlaza.zulu;

import org.apache.commons.cli.*;
import za.co.mahlaza.research.grammarengine.base.models.feature.NounClass;

public class CmdLineZuluNumPluraliser {
    public static void main(String[] args) throws Exception {
        Options cmdArgs = new Options();

        Option numberOpt = new Option("n", "number", true, "The number to be verbalised");
        numberOpt.setRequired(true);
        cmdArgs.addOption(numberOpt);

        Option nounClassOpt = new Option("nc", "nounClass", true, "The noun class of the noun with which the number must agree");
        cmdArgs.addOption(nounClassOpt);

        Option categoryOpt = new Option("c", "category", true, "The category of the number to be verbalised (Ca = Cardinal, A = Adverb, O = Ordinal, Co = Collective)");
        categoryOpt.setRequired(true);
        cmdArgs.addOption(categoryOpt);

        Option debugOpt = new Option("d", "debug", false, "Print debug information");
        cmdArgs.addOption(debugOpt);

        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(cmdArgs, args);

            Integer number = Integer.valueOf(cmd.getOptionValue("number"));
            NounClass nounClass = null;
            if (cmd.hasOption("nounClass")) {
                nounClass = NounClass.getNounClass(cmd.getOptionValue("nounClass"));
            }
            String category = cmd.getOptionValue("category");
            boolean debug = cmd.hasOption("debug");

                IsiZuluNumberVerbaliser verbaliser = new IsiZuluNumberVerbaliser();

            NumCategory currNumCategory = null;
            switch (category) {
                case "Ca": {
                    currNumCategory = NumCategory.Cardinal;
                    break;
                }
                case "A": {
                    currNumCategory = NumCategory.Adverb;
                    break;
                }
                case "O": {
                    currNumCategory = NumCategory.Ordinal;
                    break;
                }
                case "Co": {
                    currNumCategory = NumCategory.Collective;
                    break;
                }
            }

            String verbalisedNumber = "";
            String verbalisedNumberNoPhonCond = "";
            if (nounClass == null) {
                verbalisedNumber = verbaliser.getText(number, currNumCategory);
                verbalisedNumberNoPhonCond = verbaliser.getText(number, currNumCategory, false);
            }
            else {
                verbalisedNumber = verbaliser.getText(number, nounClass, currNumCategory);
                verbalisedNumberNoPhonCond = verbaliser.getText(number, nounClass, currNumCategory, false);
            }
            String result = "Input = %s\nVerbalised number = %s";
            System.out.println(String.format(result, number, verbalisedNumber));
            if (debug) {
                String anotherResult = "Verbalised number with no phon. conditioning = %s";
                System.out.println(String.format(anotherResult, verbalisedNumberNoPhonCond));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            formatter.printHelp("utility-name", cmdArgs);
            System.exit(1);
        }
    }
}
