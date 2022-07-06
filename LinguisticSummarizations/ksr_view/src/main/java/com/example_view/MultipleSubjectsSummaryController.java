package com.example_view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.fuzzy.ClassicSet;
import org.example.fuzzy.LinguisticVariableValue;
import org.example.fuzzy.Quantifier;
import org.example.fuzzy.Summary;
import org.example.logic.DatabaseQueries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MultipleSubjectsSummaryController {
    public List<Double> specifyDomain(String summarizer, String specifyQueryWithSubject) {
        DatabaseQueries databaseQueries = new DatabaseQueries();
        if (summarizer.isEmpty()) return null;
        return switch (summarizer) {
            case "old sale date", "new sale date", "sale date" -> databaseQueries.selectDateOfSale(specifyQueryWithSubject);
            case "occasional price", "cheap price", "normal price", "expensive price", "luxury price", "price" -> databaseQueries.selectPrice(specifyQueryWithSubject);
            case "very small living space", "small living space",
                    "large living space", "huge living space", "living space" -> databaseQueries.selectSqftLiving(specifyQueryWithSubject);
            case "very small lot", "small lot", "large lot", "huge lot", "lot" -> databaseQueries.selectSqftLot(specifyQueryWithSubject);
            case "very small basement", "small basement", "large basement", "huge basement", "basement" -> databaseQueries.selectSqftBasement(specifyQueryWithSubject);
            case "young building", "old building", "very old building", "year of built" -> databaseQueries.selectYearBuilt(specifyQueryWithSubject);
            case "never renovated", "recently renovated", "long ago renovated", "year of renovation" -> databaseQueries.selectYearRenovated(specifyQueryWithSubject);
            default -> null;
        };
    }

    String specifyQueryForSubject(String subject) {
        String specifyQueryToHouse;
        switch (subject) {
            case "bungalows" -> specifyQueryToHouse = " where floors==1";
            case "storeys" -> specifyQueryToHouse = " where floors <> 1 and floors <> 2";
            case "two-storeys" -> specifyQueryToHouse = " where floors == 2";
            default -> specifyQueryToHouse = "";
        }
        return specifyQueryToHouse;
    }

    public void onGenerateMultipleSummaryButtonClicked(PrimaryController primaryController) {
        primaryController.generateMultipleSum.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<Quantifier> quantifierList = primaryController.primaryControllerSetup.retrieveQuantifiersFromButton(primaryController);

                List<Quantifier> customQuantifierList = primaryController.getSelectedCustomQuantifiers();
                quantifierList.addAll(customQuantifierList);

                for (Quantifier quantifier:
                     quantifierList) {
                    if (quantifier.isAbsolute()) {
                        Alert a = new Alert(Alert.AlertType.ERROR, "Cannot create multiple subjects summary with absolute quantifier", ButtonType.APPLY);
                        a.show();
                        throw new RuntimeException("Cannot create multiple subjects summary with absolute quantifier");
                    }
                }

                List<LinguisticVariableValue> summarizerList = primaryController.primaryControllerSetup.retrieveSummarizersFromButton(primaryController);
                var customSummarizersList = primaryController.getSelectedCustomSummarizers();
                summarizerList.addAll(customSummarizersList);

                List<Summary> summaries = new ArrayList<>();
                List<String> summariesSentences = new ArrayList<>();

                // 1 forma z jednym sumaryzatorem
                // kwantyfikator subject1 compared to subject2 have + summarizer / summarizer1 and summarizer2
                // kwantyfikator subject2 compared to subject1 have + summarizer / summarizer1 and summarizer2
                //new Summary(aboutHalf, null, List.of(summarizer13), List.of(new ClassicSet(parter), new ClassicSet(storey)));

                if (primaryController.typeOfMultipleSummary.getSelectionModel().isSelected(0)) {
                    // "First form"
                    for (Quantifier quantifier : quantifierList
                    ) {
                        for (String subject1 : primaryController.subjects.getCheckModel().getCheckedItems()
                        ) {
                            for (String subject2 : primaryController.subjects.getCheckModel().getCheckedItems()
                            ) {
                                for (LinguisticVariableValue summarizer : summarizerList
                                ) {
                                    if (subject1.equals(subject2)) continue;
                                    var sentence = quantifier.getLabel() + " " + subject1 + " compared to " + subject2 + " " + primaryController.primaryControllerSetup.pickLinkingWord(summarizer.getName()) + " " + summarizer.getName() + ".";
                                    summariesSentences.add(sentence);

                                    ClassicSet classicSet1 = new ClassicSet(getDomain(primaryController,summarizer.getName(),subject1), 0, 0, false);
                                    ClassicSet classicSet2 = new ClassicSet(getDomain(primaryController,summarizer.getName(),subject2), 0, 0, false);

                                    summaries.add(new Summary(quantifier, null, List.of(summarizer), List.of(classicSet1, classicSet2)));
                                }
                            }
                        }
                    }
                    primaryController.primaryControllerSetup.addSummariesToTable(summaries, summariesSentences, null, 1, primaryController);

                }
                if (primaryController.typeOfMultipleSummary.getSelectionModel().isSelected(1)) {
                    // 2 forma
                    // quantifier subject1 compared to subject2, which have qualifier (summarizer1), have summarizer2
                    List<Summary> summariesTmp = new ArrayList<>();
                    for (Quantifier quantifier : quantifierList
                    ) {
                        for (String subject1 : primaryController.subjects.getCheckModel().getCheckedItems()
                        ) {
                            for (String subject2 : primaryController.subjects.getCheckModel().getCheckedItems()
                            ) {
                                for (LinguisticVariableValue summarizer1 : summarizerList
                                ) {
                                    for (LinguisticVariableValue summarizer2 : summarizerList
                                    ) {
                                        if (Objects.equals(summarizer1.getName(), summarizer2.getName())) continue;
                                        if (subject1.equals(subject2)) continue;
                                        var sentence = quantifier.getLabel() + " " + subject1 + " compared to " + subject2 + ", which " + primaryController.primaryControllerSetup.pickLinkingWord(summarizer1.getName()) + " " + summarizer1.getName() + ", " + primaryController.primaryControllerSetup.pickLinkingWord(summarizer1.getName()) + " " + summarizer2.getName() + ".";
                                        summariesSentences.add(sentence);

                                        ClassicSet classicSet1 = new ClassicSet(getDomain(primaryController,summarizer1.getName(),subject1), 0, 0, false);
                                        ClassicSet classicSet2 = new ClassicSet(getDomain(primaryController,summarizer1.getName(),subject2), 0, 0, false);
                                        ClassicSet classicSet3 = new ClassicSet(getDomain(primaryController,summarizer2.getName(),subject1), 0, 0, false);
                                        ClassicSet classicSet4 = new ClassicSet(getDomain(primaryController,summarizer2.getName(),subject2), 0, 0, false);
                                        summariesTmp.add(new Summary(quantifier, null, List.of(summarizer1, summarizer2), List.of(classicSet1, classicSet2, classicSet3, classicSet4)));
                                    }
                                }
                                summaries.addAll(summariesTmp);
                                summariesTmp.clear();
                            }
                        }
                    }
                    primaryController.primaryControllerSetup.addSummariesToTable(summaries, summariesSentences, null, 2, primaryController);
                }
                if (primaryController.typeOfMultipleSummary.getSelectionModel().isSelected(2)) {
                    // 3 forma
                    // quantifier subject1, which have summarizer1, compared to subject2, have summarizer2
                    List<Summary> summariesTmp = new ArrayList<>();
                    for (Quantifier quantifier : quantifierList
                    ) {
                        for (String subject1 : primaryController.subjects.getCheckModel().getCheckedItems()
                        ) {
                            for (String subject2 : primaryController.subjects.getCheckModel().getCheckedItems()
                            ) {
                                for (LinguisticVariableValue summarizer1 : summarizerList
                                ) {
                                    for (LinguisticVariableValue summarizer2 : summarizerList
                                    ) {
                                        if (Objects.equals(summarizer1.getName(), summarizer2.getName())) continue;
                                        if (subject1.equals(subject2)) continue;
                                        var sentence = quantifier.getLabel() + " " + subject1 + ", which " + primaryController.primaryControllerSetup.pickLinkingWord(summarizer1.getName()) + " " + summarizer1.getName() + ", compared to " + subject2 + ", " + primaryController.primaryControllerSetup.pickLinkingWord(summarizer1.getName()) + " " + summarizer2.getName() + ".";
                                        summariesSentences.add(sentence);

                                        ClassicSet classicSet1 = new ClassicSet(getDomain(primaryController,summarizer1.getName(),subject1), 0, 0, false);
                                        ClassicSet classicSet2 = new ClassicSet(getDomain(primaryController,summarizer1.getName(),subject2), 0, 0, false);
                                        ClassicSet classicSet3 = new ClassicSet(getDomain(primaryController,summarizer2.getName(),subject1), 0, 0, false);
                                        ClassicSet classicSet4 = new ClassicSet(getDomain(primaryController,summarizer2.getName(),subject2), 0, 0, false);
                                        summariesTmp.add(new Summary(quantifier, null, List.of(summarizer1, summarizer2), List.of(classicSet1, classicSet2, classicSet3, classicSet4)));
                                    }
                                }
                                summaries.addAll(summariesTmp);
                                summariesTmp.clear();
                            }
                        }
                    }
                    primaryController.primaryControllerSetup.addSummariesToTable(summaries, summariesSentences, null, 3, primaryController);
                }

                if (primaryController.typeOfMultipleSummary.getSelectionModel().isSelected(3)) {
                    // 4 forma
                    // quantifier subject1, which have summarizer1, compared to subject2, have summarizer2
                    // More subject1 than subject2 summarizer1
                    for (String subject1 : primaryController.subjects.getCheckModel().getCheckedItems()
                    ) {
                        for (String subject2 : primaryController.subjects.getCheckModel().getCheckedItems()
                        ) {
                            for (LinguisticVariableValue summarizer : summarizerList
                            ) {
                                if (subject1.equals(subject2)) continue;
                                var sentence = "More " + subject1 + " than " + subject2 + " " + primaryController.primaryControllerSetup.pickLinkingWord(summarizer.getName()) + " " + summarizer.getName() + ".";
                                summariesSentences.add(sentence);

                                ClassicSet classicSet1 = new ClassicSet(getDomain(primaryController,summarizer.getName(),subject1), 0, 0, false);
                                ClassicSet classicSet2 = new ClassicSet(getDomain(primaryController,summarizer.getName(),subject2), 0, 0, false);
                                summaries.add(new Summary(null, null, List.of(summarizer), List.of(classicSet1, classicSet2)));
                            }
                        }
                    }
                    primaryController.primaryControllerSetup.addSummariesToTable(summaries, summariesSentences, null, 4, primaryController);
                }
            }
        });
    }

    public List<Double> getDomain(PrimaryController primaryController, String summarizer, String subject) {
        List<Double> domain = specifyDomain(summarizer, specifyQueryForSubject(subject));
        if (domain == null) {
            // check index of summarizers in custom
            var index = 0;

            for (int i = 0; i < primaryController.getCustomSummarizers().size(); i++) {
                if (primaryController.getCustomSummarizers().get(i).getName().equals(summarizer)) {
                    index = i;
                    break;
                }
            }

            domain = specifyDomain(primaryController.getCustomSummarizersType().get(index), specifyQueryForSubject(subject));
        }
        return domain;
    }



}
