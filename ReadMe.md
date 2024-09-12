# IsiZulu number verbaliser

This is the Java source code for the isiZulu number verbaliser. A first version was initially created as part of the Digital Assistant For Financial Transactions Honours project with Junior Moraba and Amy Solomons (https://projects.cs.uct.ac.za/honsproj/cgi-bin/view/2021/moraba_solomons.zip/), which underwent two more iterations of improvements to the current release version. 

# Usage via commandline

There is a packaged jar file that can be run via the command line. It can be found under out/artifacts/ZuluNum2TextCMD_jar/

```
java -jar ZuluNum2TextCMD.jar -n <insert number> -c <insert category>
```

The available categories are `Ca` = Cardinal, `A` = Adverb, `O` = Ordinal, and `SoI` = Set-of-items. It is possible to generate the number without applying phonological conditioning rules by adding the debug option `-d`.

In the event that one wants to add a noun class marker for the verbalised number, use the option `-nc`. For instance, to verbalise the cardinal number 2 when it is in agreement with nouns from class 2 (i.e., ababili), you can use the following command:

```
java -jar ZuluNum2TextCMD.jar -n 2 -c Ca -nc 2
```

# Usage via commandline to generate evaluation/survey data

There is a packaged jar file that can be run via the command line to generate evaluation/survey data. It can be found under out/artifacts/ZuluNum2TextSurveyData_jar/

```
java -jar ZuluNum2TextSurveyData.jar -f <filename>
```

The filename must be a csv file (e.g., output.csv). Please note that one of the candidate numbers randomly sampled might be 1 and that will result in an error in the case Set-of-items numbers. You will get the following error:

```
java.lang.IllegalArgumentException: The SetOfItems type does not support the the number = 1
```

In the event that you get the error, re-run the jar file such that one of the sampled numbers is not 1.

# Usage as library

The verbaliser can be included as part of some other Java-based code. You can use it to generate the final number for a specific category with or without the application of the phonological conditioning rules as shown below:

```
IsiZuluNumberVerbaliser verbaliser = new IsiZuluNumberVerbaliser();

NumCategory currNumCategory = ...

String verbalisedNumber = verbaliser.getText(number, currNumCategory);
String verbalisedNumberNoPhonCond = verbaliser.getText(number, currNumCategory, false);

String verbalisedNumber = verbaliser.getText(number, nounClass, currNumCategory);
String verbalisedNumberNoPhonCond = verbaliser.getText(number, nounClass, currNumCategory, false);

```

# Citing the isiZulu number generator

Mahlaza, Z., Magwenzi, T., Keet, C.M., Khumalo, L. Automatically Generating IsiZulu Words From Indo-Arabic Numerals. 17th International Natural Language Generation Conference (INLG'24), Tokyo, Japan, September 23-27, 2024. ACL. (in print)

# Funding

This work was funded in part by the National Research Foundation (NRF) of South Africa (Grant Numbers 120852 and CPRR23040389063).
