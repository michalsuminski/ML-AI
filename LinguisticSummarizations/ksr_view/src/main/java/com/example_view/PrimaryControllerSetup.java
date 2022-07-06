package com.example_view;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.fuzzy.LinguisticVariable;
import org.example.fuzzy.LinguisticVariableValue;
import org.example.fuzzy.Quantifier;
import org.example.fuzzy.Summary;
import org.example.logic.Initializer;
import org.example.logic.QualityMeasures;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example_view.OneSubjectSummaryController.roundQualityMeasure;

public class PrimaryControllerSetup {
    public String pickLinkingWord(String summarizer) {
        if (summarizer.isEmpty()) {
            return "";
        } else if (summarizer.equals("never renovated") || summarizer.equals("long ago renovated") || summarizer.equals("recently renovated")) {
            return "were";
        }
        return "have";
    }

    void createLabelsOnView(PrimaryController primaryController) {
        primaryController.quantifierChoiceId.getItems().addAll("all", "almost none", "almost all", "about half", "much more than 1/4",
                "much less than 3/4", "less than 10000", "less than 7500", "more than 7500", "more than 5500", "about 10000");

        primaryController.summarizerCBId.getItems().addAll("all", "old sale date", "new sale date", "occasional price", "cheap price",
                "normal price", "expensive price", "luxury price", "very small living space", "small living space",
                "large living space", "huge living space", "very small lot", "small lot", "large lot", "huge lot",
                "very small basement", "small basement", "large basement", "huge basement", "young building", "old building", "very old building", "recently renovated", "long ago renovated");

        primaryController.subjects.getItems().addAll("bungalows", "two-storeys", "storeys");

        primaryController.typeOfMultipleSummary.getItems().addAll("First form", "Second form", "Third form", "Forth form");

    }

    void clearTable(PrimaryController primaryController) {
        primaryController.clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryController.observableList.clear();
            }
        });
    }

    void selectAllSaveOnClicked(PrimaryController primaryController) {
        primaryController.select_all_save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (int i = 0; i < primaryController.tableViewOneEntity.getItems().size(); i++) {
                    primaryController.tableViewOneEntity.getItems().get(i).getCheckBox().setSelected(true);
                }
            }
        });
    }

    LinguisticVariableValue findSummarizer(String summarizer) {
        Initializer initializer = new Initializer();
        List<LinguisticVariable> linguisticVariables = initializer.initializeLingusiticVariable();
        if (summarizer.isEmpty()) return null;
        return switch (summarizer) {
            case "old sale date" -> linguisticVariables.get(0).getLinguisticVariableList().get(0);
            case "new sale date" -> linguisticVariables.get(0).getLinguisticVariableList().get(1);
            case "occasional price" -> linguisticVariables.get(1).getLinguisticVariableList().get(0);
            case "cheap price" -> linguisticVariables.get(1).getLinguisticVariableList().get(1);
            case "normal price" -> linguisticVariables.get(1).getLinguisticVariableList().get(2);
            case "expensive price" -> linguisticVariables.get(1).getLinguisticVariableList().get(3);
            case "luxury price" -> linguisticVariables.get(1).getLinguisticVariableList().get(4);
            case "very small living space" -> linguisticVariables.get(2).getLinguisticVariableList().get(0);
            case "small living space" -> linguisticVariables.get(2).getLinguisticVariableList().get(1);
            case "large living space" -> linguisticVariables.get(2).getLinguisticVariableList().get(2);
            case "huge living space" -> linguisticVariables.get(2).getLinguisticVariableList().get(3);
            case "very small lot" -> linguisticVariables.get(3).getLinguisticVariableList().get(0);
            case "small lot" -> linguisticVariables.get(3).getLinguisticVariableList().get(1);
            case "large lot" -> linguisticVariables.get(3).getLinguisticVariableList().get(2);
            case "huge lot" -> linguisticVariables.get(3).getLinguisticVariableList().get(3);
            case "very small basement" -> linguisticVariables.get(4).getLinguisticVariableList().get(0);
            case "small basement" -> linguisticVariables.get(4).getLinguisticVariableList().get(1);
            case "large basement" -> linguisticVariables.get(4).getLinguisticVariableList().get(2);
            case "huge basement" -> linguisticVariables.get(4).getLinguisticVariableList().get(3);
            case "young building" -> linguisticVariables.get(5).getLinguisticVariableList().get(0);
            case "old building" -> linguisticVariables.get(5).getLinguisticVariableList().get(1);
            case "very old building" -> linguisticVariables.get(5).getLinguisticVariableList().get(2);
            case "never renovated" -> linguisticVariables.get(6).getLinguisticVariableList().get(0);
            case "recently renovated" -> linguisticVariables.get(6).getLinguisticVariableList().get(1);
            case "long ago renovated" -> linguisticVariables.get(6).getLinguisticVariableList().get(2);
            default -> null;
        };
    }

    Quantifier findQuantifier(String quantifierName) {
        Initializer initializer = new Initializer();

        List<Quantifier> quantifiers = initializer.initializeQuantifierVariable(10000, 7500, 7500, 5550, 10000);
        return switch (quantifierName) {
            case "almost none" -> quantifiers.get(0);
            case "almost all" -> quantifiers.get(1);
            case "about half" -> quantifiers.get(2);
            case "much more than 1/4" -> quantifiers.get(3);
            case "much less than 3/4" -> quantifiers.get(4);
            case "less than 10000" -> quantifiers.get(5);
            case "less than 7500" -> quantifiers.get(6);
            case "more than 7500" -> quantifiers.get(7);
            case "more than 5500" -> quantifiers.get(8);
            case "about 10000" -> quantifiers.get(9);
            default -> null;
        };
    }

    void saveSummmariesToFile(PrimaryController primaryController) {
        primaryController.save_to_file.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                List<String> textToSave = new ArrayList<>();
                Alert a = new Alert(Alert.AlertType.NONE,"Summaries succesfully saved to file", ButtonType.APPLY);

                for (int i = 0; i < primaryController.tableViewOneEntity.getItems().size(); i++) {
                    if (primaryController.tableViewOneEntity.getItems().get(i).getCheckBox().isSelected()) {
                        textToSave.add(primaryController.tableViewOneEntity.getItems().get(i).getSummaryAndQualityMeasures());
                    }
                }
                try {
                    FileWriter myWriter = new FileWriter("saved_summaries.txt");
                    for (String str : textToSave) {
                        myWriter.write(str + System.lineSeparator());
                    }
                    myWriter.close();
                    System.out.println("Successfully wrote to the file.");
                    a.show();
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }
        });
    }

    List<Quantifier> retrieveQuantifiersFromButton(PrimaryController primaryController) {
        List<Quantifier> qualifierList = new ArrayList<>();
        for (String qualifier : primaryController.quantifierChoiceId.getCheckModel().getCheckedItems()
        ) {
            if (qualifier.equals("all")) continue;
            qualifierList.add(findQuantifier(qualifier));
        }
        qualifierList.removeAll(Collections.singleton(null));
        return qualifierList;
    }

    List<LinguisticVariableValue> retrieveSummarizersFromButton(PrimaryController primaryController) {
        List<LinguisticVariableValue> linguisticVariableValues = new ArrayList<>();
        for (String summarizer : primaryController.summarizerCBId.getCheckModel().getCheckedItems()
        ) {
            if (summarizer.equals("all")) continue;
            linguisticVariableValues.add(findSummarizer(summarizer));
        }
        linguisticVariableValues.removeAll(Collections.singleton(null));
        return linguisticVariableValues;
    }

    void listenerOnAllCheckQuantifier(PrimaryController primaryController) {
        primaryController.quantifierChoiceId.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            private boolean changing = false;

            @Override
            public void onChanged(Change<? extends String> c) {
                if (!changing && primaryController.quantifierChoiceId.getCheckModel().isChecked(0)) {
                    // trigger no more calls to checkAll when the selected items are modified by checkAll
                    changing = true;
                    primaryController.quantifierChoiceId.getCheckModel().checkAll();
                }
                if (changing && !primaryController.quantifierChoiceId.getCheckModel().isChecked(0)) {
                    // trigger no more calls to checkAll when the selected items are modified by checkAll
                    changing = false;
                    primaryController.quantifierChoiceId.getCheckModel().clearChecks();
                }
            }
        });
    }

    void listenerOnAllCheckSummarizer(PrimaryController primaryController) {
        primaryController.summarizerCBId.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            private boolean changing = false;

            @Override
            public void onChanged(Change<? extends String> c) {
                if (!changing && primaryController.summarizerCBId.getCheckModel().isChecked(0)) {
                    // trigger no more calls to checkAll when the selected items are modified by checkAll
                    changing = true;
                    primaryController.summarizerCBId.getCheckModel().checkAll();
                }
                if (changing && !primaryController.summarizerCBId.getCheckModel().isChecked(0)) {
                    // trigger no more calls to checkAll when the selected items are modified by checkAll
                    changing = false;
                    primaryController.summarizerCBId.getCheckModel().clearChecks();
                }
            }
        });
    }

    public void addSummariesToTable(List<Summary> summaries, List<String> summariesSentences, List<Double> weights, int form, PrimaryController primaryController) {
        QualityMeasures qualityMeasures = new QualityMeasures();

        // prepare columns
        primaryController.col1.setCellValueFactory(new PropertyValueFactory<>("Col1"));
        primaryController.col2.setCellValueFactory(new PropertyValueFactory<>("Col2"));
        primaryController.col3.setCellValueFactory(new PropertyValueFactory<>("Col3"));
        primaryController.col4.setCellValueFactory(new PropertyValueFactory<>("Col4"));
        primaryController.col5.setCellValueFactory(new PropertyValueFactory<>("Col5"));
        primaryController.col6.setCellValueFactory(new PropertyValueFactory<>("Col6"));
        primaryController.col7.setCellValueFactory(new PropertyValueFactory<>("Col7"));
        primaryController.col8.setCellValueFactory(new PropertyValueFactory<>("Col8"));
        primaryController.col9.setCellValueFactory(new PropertyValueFactory<>("Col9"));
        primaryController.col10.setCellValueFactory(new PropertyValueFactory<>("Col10"));
        primaryController.col11.setCellValueFactory(new PropertyValueFactory<>("Col11"));
        primaryController.col12.setCellValueFactory(new PropertyValueFactory<>("Col12"));
        primaryController.col13.setCellValueFactory(new PropertyValueFactory<>("Col13"));
        primaryController.saveCol.setCellValueFactory(new PropertyValueFactory<>("CheckBox"));

        List<SummaryTable> summaryTables = new ArrayList<>();
        if (form == 0) {
            for (int i = 0; i < summariesSentences.size(); i++) {
                List<String> measures = primaryController.oneSubjectSummaryController.calculateOneEntityMeasures(summaries.get(i), weights);
                summaryTables.add(new SummaryTable(summariesSentences.get(i), measures.get(0), measures.get(1), measures.get(2), measures.get(3), measures.get(4), measures.get(5), measures.get(6), measures.get(7), measures.get(8), measures.get(9), measures.get(10), measures.get(11)));
            }
        } else if (form == 1) {
            for (int i = 0; i < summariesSentences.size(); i++) {
                summaryTables.add(new SummaryTable(summariesSentences.get(i),String.valueOf(roundQualityMeasure(qualityMeasures.degreeOfTruthFormOne(summaries.get(i)))), "", "", "", "", "", "", "", "", "", "", ""));
            }
        } else if (form == 2) {
            for (int i = 0; i < summariesSentences.size(); i++) {
                summaryTables.add(new SummaryTable(summariesSentences.get(i),String.valueOf(roundQualityMeasure(qualityMeasures.degreeOfTruthFormTwo(summaries.get(i)))), "", "", "", "", "", "", "", "", "", "", ""));
            }
        } else if (form == 3) {
            for (int i = 0; i < summariesSentences.size(); i++) {
                summaryTables.add(new SummaryTable(summariesSentences.get(i),String.valueOf(roundQualityMeasure(qualityMeasures.degreeOfTruthFormThree(summaries.get(i)))), "", "", "", "", "", "", "", "", "", "", ""));
            }
        } else if (form == 4) {
            for (int i = 0; i < summariesSentences.size(); i++) {
                summaryTables.add(new SummaryTable(summariesSentences.get(i),String.valueOf(roundQualityMeasure(qualityMeasures.degreeOfTruthFormFour(summaries.get(i)))), "", "", "", "", "", "", "", "", "", "", ""));
            }
        }
        primaryController.observableList.addAll(summaryTables);
        primaryController.tableViewOneEntity.setItems(primaryController.observableList);
    }

    public boolean validateIfSummaryWithTwoSummarizersAlreadyExists(List<Summary> summaries, LinguisticVariableValue s1, LinguisticVariableValue s2) {
        for (Summary summary : summaries
        ) {
            if (summary.getSummarizers().size() == 2) {
                if (summary.getSummarizers().contains(s1) && summary.getSummarizers().contains(s2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
