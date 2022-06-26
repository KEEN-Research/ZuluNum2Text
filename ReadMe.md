# IsiZulu number verbaliser

This is the Java source code for the isiZulu number verbaliser. It was initialially created as part of the Digital Assitant For Financial Transactions project, with Junior Moraba and Amy Solomons (https://projects.cs.uct.ac.za/honsproj/cgi-bin/view/2021/moraba_solomons.zip/)

# Usage via commandline

There is a packaged jar file that can be run via the command line. It can be found under out/artifacts/ZuluNum2TextCMD/

```
java -jar ZuluNum2TextCMD.jar -n <insert number> -c <insert category>
```

The available categories are `Ca` = Cardinal, `A` = Adverb, `O` = Ordinal, and `Co` = Collective. It is possible to generate the number without applying phonological conditioning rules by adding the debug option `-d`. In the future, more data will returned to help visualise/debug the generated number.

In the event that one wants to add a noun class marker for the verbalised number, use the option `-nc`

# Usage as library

The verbaliser can be include as part of some other Java-based code. You can use it to generate the final number for a specific category with or without the application of the phonological conditioning rules as shown below:

```
IsiZuluNumberVerbaliser verbaliser = new IsiZuluNumberVerbaliser();

NumCategory currNumCategory = ...

String verbalisedNumber = verbaliser.getText(number, currNumCategory);
String verbalisedNumberNoPhonCond = verbaliser.getText(number, currNumCategory, false);

String verbalisedNumber = verbaliser.getText(number, nounClass, currNumCategory);
String verbalisedNumberNoPhonCond = verbaliser.getText(number, nounClass, currNumCategory, false);


```

# Citation

Related paper with the theory, design and evaluation of the current verbaliser: Mahlaza, Z., Moraba, V.J., Keet, C.M., Khumalo, L. Automatically generating isiZulu words from Indo-Arabic numerals. Annual Conference of the South African Institute of Computer Scientists and Information Technologists (SAICSIT'22). EPiC Series in Computing, (in print). July 18-21, 2022, Cape Town, South Africa.
