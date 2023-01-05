package za.co.research.mahlaza.zulu;

import org.apache.commons.cli.*;
import za.co.mahlaza.research.grammarengine.base.models.feature.NounClass;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

public class CmdLineZuluNumPluraliser {
    public static void main(String[] args) throws Exception {
        Options cmdArgs = new Options();

        Option numberOpt = new Option("n", "number", true, "The number to be verbalised");
        numberOpt.setRequired(true);
        cmdArgs.addOption(numberOpt);

        Option nounClassOpt = new Option("nc", "nounClass", true, "The noun class of the noun with which the number must agree");
        cmdArgs.addOption(nounClassOpt);

        Option categoryOpt = new Option("c", "category", true, "The category of the number to be verbalised (Ca = Cardinal, A = Adverb, O = Ordinal, Co = SoI = Collective/Set-of-items)");
        categoryOpt.setRequired(true);
        cmdArgs.addOption(categoryOpt);

        Option debugOpt = new Option("d", "debug", false, "Print debug information");
        cmdArgs.addOption(debugOpt);

        Option helpOpt = new Option("h", "help", false, "Print help information");
        cmdArgs.addOption(helpOpt);

        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(cmdArgs, args);

            if (cmd.hasOption("h")) {
                formatter.printHelp("Number verbaliser", cmdArgs);
                System.exit(1);
            }

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
                case "Co":
                case "SoI": {
                    currNumCategory = NumCategory.SetOfItems;
                    break;
                }
            }


            String verbalisedNumber = "";
            if (nounClass == null) {
                verbalisedNumber = verbaliser.getText(number, currNumCategory);
            }
            else {
                verbalisedNumber = verbaliser.getText(number, nounClass, currNumCategory);
            }
            String result = "Input = %s\nVerbalised number = %s";
            System.out.println(String.format(result, number, verbalisedNumber));
            if (debug) {
                String anotherResult = "Verbalised number with clear morphemes = %s";
                Queue<String> morphemeListForDebugging = verbaliser.getDebugMorphemes();
                AtomicReference<String> debubMorphemes = new AtomicReference<>("");
                morphemeListForDebugging.forEach(morpheme -> {
                    if (morpheme.isBlank()) {
                        debubMorphemes.set( debubMorphemes.get() + morpheme);
                    }
                    else {
                        debubMorphemes.set( debubMorphemes.get() + "[" + morpheme + "]");
                    }
                });
                System.out.println(String.format(anotherResult, debubMorphemes.get()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            formatter.printHelp("Number verbaliser", cmdArgs);
            System.exit(1);
        }
    }
}
