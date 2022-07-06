package com.example_view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.example.fuzzy.LinguisticVariableValue;
import org.example.fuzzy.Quantifier;
import org.example.fuzzy.Summary;
import org.example.logic.QualityMeasures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OneSubjectSummaryController {
    public static double roundQualityMeasure(double value) {
        return Math.round(value * 10000) / 10000D;
    }

    public List<String> calculateOneEntityMeasures(Summary summary, List<Double> weights) {
        QualityMeasures qualityMeasures = new QualityMeasures();
        return new ArrayList<>(Arrays.asList(
                String.valueOf(qualityMeasures.degreeOfTruth(summary)),
                String.valueOf(roundQualityMeasure(qualityMeasures.degreeOfImprecision(summary))),
                String.valueOf(roundQualityMeasure(qualityMeasures.degreeOfCovering(summary))),
                String.valueOf(roundQualityMeasure(qualityMeasures.degreeOfAppropriateness(summary))),
                String.valueOf(roundQualityMeasure(QualityMeasures.LengthOfSummary(summary))),
                String.valueOf(roundQualityMeasure(qualityMeasures.degreeOfQuantifierImprecision(summary))),
                String.valueOf(roundQualityMeasure(QualityMeasures.DegreeOfQuantifierCardinality(summary))),
                String.valueOf(roundQualityMeasure(qualityMeasures.degreeOfSummarizerCardinality(summary))),
                String.valueOf(roundQualityMeasure(QualityMeasures.DegreeOfQualifierImprecision(summary))),
                String.valueOf(roundQualityMeasure(qualityMeasures.degreeOfQualifierCardinality(summary))),
                String.valueOf(roundQualityMeasure(QualityMeasures.LengthOfQualifier(summary))),
                String.valueOf(roundQualityMeasure(qualityMeasures.optimalSummary(summary, weights))
                )));
    }

    public List<Double> retrieveEnteredQualityWeight(PrimaryController primaryController) {
        return List.of(Double.parseDouble(primaryController.w1.getText()), Double.parseDouble(primaryController.w2.getText()), Double.parseDouble(primaryController.w3.getText()),
                Double.parseDouble(primaryController.w4.getText()), Double.parseDouble(primaryController.w5.getText()), Double.parseDouble(primaryController.w6.getText()), Double.parseDouble(primaryController.w7.getText()),
                Double.parseDouble(primaryController.w8.getText()), Double.parseDouble(primaryController.w9.getText()), Double.parseDouble(primaryController.w10.getText()), Double.parseDouble(primaryController.w11.getText()));
    }

    public void onGenerateOneSubjectSummaryButtonClicked(PrimaryController primaryController) {
        primaryController.generateSumFirstId.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<Quantifier> quantifierList = primaryController.primaryControllerSetup.retrieveQuantifiersFromButton(primaryController);

                List<Quantifier> customQuantifierList = primaryController.getSelectedCustomQuantifiers();
                quantifierList.addAll(customQuantifierList);

                List<LinguisticVariableValue> summarizerList = primaryController.primaryControllerSetup.retrieveSummarizersFromButton(primaryController);
                var customSummarizersList = primaryController.getSelectedCustomSummarizers();
                summarizerList.addAll(customSummarizersList);

                List<Summary> summaries = new ArrayList<>();
                List<String> summariesSentences = new ArrayList<>();

                List<Summary> summariesTmp = new ArrayList<>();

                // dla każdego quantifiera generujemy wszyskie kombinacje

                for (Quantifier quantifier : quantifierList
                ) {

                    // podsumowanie bez qualifiera z jednym sumaryzytorem
                    for (LinguisticVariableValue summarizer : summarizerList
                    ) {
                        var sentence = quantifier.getLabel() + " " + "of houses " + primaryController.primaryControllerSetup.pickLinkingWord(summarizer.getName()) + " " + summarizer.getName() + ".";
                        summariesSentences.add(sentence);
                        summaries.add(new Summary(quantifier, null, List.of(summarizer), null));
                    }
                    // podsumowanie bez qualifiera z dwoma sumarazytorami
                    if (summarizerList.size() > 1) {
                        for (LinguisticVariableValue summarizer1 : summarizerList
                        ) {
                            for (LinguisticVariableValue summarizer2 : summarizerList
                            ) {
                                if (Objects.equals(summarizer1.getName(), summarizer2.getName())) continue;
                                if (primaryController.primaryControllerSetup.validateIfSummaryWithTwoSummarizersAlreadyExists(summariesTmp, summarizer1, summarizer2)) continue;


                                var sentence = quantifier.getLabel() + " " + "of houses " + primaryController.primaryControllerSetup.pickLinkingWord(summarizer1.getName()) + " " + summarizer1.getName() + " and " + primaryController.primaryControllerSetup.pickLinkingWord(summarizer2.getName()) + " " + summarizer2.getName() + ".";
                                summariesSentences.add(sentence);
                                summariesTmp.add(new Summary(quantifier, null, List.of(summarizer1, summarizer2), null));
                            }
                        }
                        summaries.addAll(summariesTmp);
                        summariesTmp.clear();
                    }
                    // podsumowanie z qualifierem z jednym sumarazytorem
                    if (summarizerList.size() > 1) {
                        for (LinguisticVariableValue summarizer1 : summarizerList
                        ) {
                            for (LinguisticVariableValue summarizer2 : summarizerList
                            ) {
                                if (Objects.equals(summarizer1.getName(), summarizer2.getName())) continue;
                                var sentence = quantifier.getLabel() + " " + "of houses, which " + primaryController.primaryControllerSetup.pickLinkingWord(summarizer1.getName()) + " " + summarizer1.getName() + ", " + primaryController.primaryControllerSetup.pickLinkingWord(summarizer2.getName()) + " " + summarizer2.getName() + ".";
                                summariesSentences.add(sentence);
                                summariesTmp.add(new Summary(quantifier, List.of(summarizer1), List.of(summarizer2), null));
                            }
                        }
                        summaries.addAll(summariesTmp);
                        summariesTmp.clear();
                    }
                    // podsumowanie z qualifierem i dwoma sumaryzytorami
                    // tylko jesli zostały wybrane ponad 2 sumaryzatory
                    if (summarizerList.size() > 2) {
                        for (LinguisticVariableValue summarizer1 : summarizerList
                        ) {
                            for (LinguisticVariableValue summarizer2 : summarizerList
                            ) {
                                for (LinguisticVariableValue summarizer3 : summarizerList
                                ) {
                                    if (Objects.equals(summarizer1.getName(), summarizer2.getName())) continue;
                                    if (Objects.equals(summarizer2.getName(), summarizer3.getName())) continue;
                                    if (Objects.equals(summarizer1.getName(), summarizer3.getName())) continue;
                                    if (primaryController.primaryControllerSetup.validateIfSummaryWithTwoSummarizersAlreadyExists(summariesTmp, summarizer2, summarizer3)) continue;

                                    var sentence = quantifier.getLabel() + " " + "of houses, which " + primaryController.primaryControllerSetup.pickLinkingWord(summarizer1.getName()) + " " + summarizer1.getName() + ", " + primaryController.primaryControllerSetup.pickLinkingWord(summarizer2.getName()) + " " + summarizer2.getName() + " and " + primaryController.primaryControllerSetup.pickLinkingWord(summarizer3.getName()) + " " + summarizer3.getName() + ".";

                                    summariesSentences.add(sentence);
                                    summariesTmp.add(new Summary(quantifier, List.of(summarizer1), List.of(summarizer2, summarizer3), null));
                                }
                            }
                        }
                        summaries.addAll(summariesTmp);
                        summariesTmp.clear();
                    }
                }

                primaryController.primaryControllerSetup.addSummariesToTable(summaries, summariesSentences, retrieveEnteredQualityWeight(primaryController), 0, primaryController);
            }
        });
    }
}
