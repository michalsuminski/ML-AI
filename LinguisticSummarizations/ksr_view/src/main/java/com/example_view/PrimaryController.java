package com.example_view;

import java.net.URL;
import java.util.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.controlsfx.control.CheckComboBox;
import org.example.fuzzy.LinguisticVariableValue;
import org.example.fuzzy.Quantifier;
import org.example.logic.DatabaseQueries;
import org.example.logic.Generator;

public class PrimaryController implements Initializable {
    // Quantifier ChoiceBox
    @FXML
    CheckComboBox<String> quantifierChoiceId;
    @FXML
    Label quantifierLabelId;

    // Summarizer ComboBox
    @FXML
    CheckComboBox<String> summarizerCBId;
    @FXML
    Label summarizerLabelId;
    // Generate summary button
    @FXML
    Button generateSumFirstId;
    // Go to second view

    // Weights
    @FXML
    TextField w1;
    @FXML
    TextField w2;
    @FXML
    TextField w3;
    @FXML
    TextField w4;
    @FXML
    TextField w5;
    @FXML
    TextField w6;
    @FXML
    TextField w7;
    @FXML
    TextField w8;
    @FXML
    TextField w9;
    @FXML
    TextField w10;
    @FXML
    TextField w11;

    // Table components
    @FXML
    TableView<SummaryTable> tableViewOneEntity;
    @FXML
    TableColumn<SummaryTable, String> col1;
    @FXML
    TableColumn<SummaryTable, Double> col2;
    @FXML
    TableColumn<SummaryTable, Double> col3;
    @FXML
    TableColumn<SummaryTable, Double> col4;
    @FXML
    TableColumn<SummaryTable, Double> col5;
    @FXML
    TableColumn<SummaryTable, Double> col6;
    @FXML
    TableColumn<SummaryTable, Double> col7;
    @FXML
    TableColumn<SummaryTable, Double> col8;
    @FXML
    TableColumn<SummaryTable, Double> col9;
    @FXML
    TableColumn<SummaryTable, Double> col10;
    @FXML
    TableColumn<SummaryTable, Double> col11;
    @FXML
    TableColumn<SummaryTable, Double> col12;
    @FXML
    TableColumn<SummaryTable, Double> col13;
    @FXML
    TableColumn<SummaryTable, CheckBox> saveCol;
    @FXML
    Button select_all_save;
    @FXML
    Button save_to_file;

    @FXML
    CheckComboBox<String> subjects;

    ObservableList<SummaryTable> observableList = FXCollections.observableArrayList();

    @FXML
    Button generateMultipleSum;

    @FXML
    ChoiceBox<String> typeOfMultipleSummary;

    @FXML
    Button clear;

    @FXML
    TextField quantifierName;
    @FXML
    ChoiceBox<String> quantifierType;
    @FXML
    ChoiceBox<String> quantifierFunction;
    @FXML
    TextField a;
    @FXML
    Label aLabel;
    @FXML
    TextField b;
    @FXML
    Label bLabel;
    @FXML
    TextField c;
    @FXML
    Label cLabel;
    @FXML
    TextField d;
    @FXML
    Label dLabel;
    @FXML
    Button generateQuantifier;

    @FXML
    ChoiceBox<String> summarizerRelatedID;
    @FXML
    ChoiceBox<String> summarizerFunction;
    @FXML
    TextField a2;
    @FXML
    Label aLabel2;
    @FXML
    TextField b2;
    @FXML
    Label bLabel2;
    @FXML
    TextField c2;
    @FXML
    Label cLabel2;
    @FXML
    TextField d2;
    @FXML
    Label dLabel2;
    @FXML
    Button generateSummarizer;
    @FXML
    TextField summarizerNameGen;

    public MultipleSubjectsSummaryController multipleSubjectsSummaryController;

    public OneSubjectSummaryController oneSubjectSummaryController;

    public PrimaryControllerSetup primaryControllerSetup;

    List<Quantifier> customQuantifiers = new ArrayList<>();

    public List<LinguisticVariableValue> customSummarizers = new ArrayList<>();
    public List<String> customSummarizersType = new ArrayList<>();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        multipleSubjectsSummaryController = new MultipleSubjectsSummaryController();
        oneSubjectSummaryController = new OneSubjectSummaryController();
        primaryControllerSetup = new PrimaryControllerSetup();

        tableViewOneEntity.setItems(observableList);

        // add textView to QuantifierList, QualifierList and SummarizerList
        primaryControllerSetup.createLabelsOnView(this);

        primaryControllerSetup.listenerOnAllCheckQuantifier(this);
        primaryControllerSetup.listenerOnAllCheckSummarizer(this);

        primaryControllerSetup.selectAllSaveOnClicked(this);

        oneSubjectSummaryController.onGenerateOneSubjectSummaryButtonClicked(this);

        primaryControllerSetup.saveSummmariesToFile(this);

        multipleSubjectsSummaryController.onGenerateMultipleSummaryButtonClicked(this);

        primaryControllerSetup.clearTable(this);

        createLabelsOnSecondView();
        listenerOnQuantifierFunctionType();
        onGenerateQuantifierClicked();
        onGenerateSummarizerClicked();
        listenerOnSummarizerFunctionType();
    }

    public void onGenerateQuantifierClicked() {
        generateQuantifier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String quantifierNameEntered = quantifierName.getText();
                System.out.println(quantifierNameEntered);

                validateIfQuantifierExists(quantifierNameEntered);

                String quantifierTypeEntered = quantifierType.getSelectionModel().getSelectedItem();
                System.out.println(quantifierTypeEntered);
                boolean isAbsolute = !Objects.equals(quantifierTypeEntered, "relative");

                String quantifierFunctionEntered = quantifierFunction.getSelectionModel().getSelectedItem();
                System.out.println(quantifierFunctionEntered);


                String aText = a.getText().replaceAll(",", ".");
                if (Objects.equals(aText, "")) {
                    aText = "0.0";
                }

                System.out.println(aText);
                String bText = b.getText().replaceAll(",", ".");
                if (Objects.equals(bText, "")) {
                    bText = "0.0";
                }
                System.out.println(bText);
                String cText = c.getText().replaceAll(",", ".");
                if (Objects.equals(cText, "")) {
                    cText = "0.0";
                }
                System.out.println(cText);
                String dText = d.getText().replaceAll(",", ".");
                if (Objects.equals(dText, "")) {
                    dText = "0.0";
                }
                System.out.println(dText);

                //generate quantifier
                DatabaseQueries databaseQueries = new DatabaseQueries();
                var databaseSize = databaseQueries.selectAll().size();
                List<Double> quantifierDomain = new ArrayList<>();

                Generator generator = new Generator();

                boolean isAscending = false;
                if (Objects.equals(quantifierFunctionEntered, "half-triangular ascending")) {
                    quantifierFunctionEntered = "half-triangular";
                    isAscending = true;
                } else if (Objects.equals(quantifierFunctionEntered, "half-triangular descending")) {
                    aText = cText;
                    bText = dText;
                    quantifierFunctionEntered = "half-triangular";
                }

                double beginOfInterval = 0;
                double endOfInterval = 1;
                if (isAbsolute) {
                    endOfInterval = databaseSize;

                    for (int i = 0; i < databaseSize + 1; i++) {
                        quantifierDomain.add((double) i);
                    }
                }
                if (!isAbsolute) {
                    for (int i = 0; i < databaseSize + 1; i++) {
                        quantifierDomain.add((double) i / databaseSize);
                    }

                }

                boolean isContinuous = !isAbsolute;

                var quantifier = generator.generateQuantifier(quantifierNameEntered, quantifierFunctionEntered, isAbsolute, Double.parseDouble(aText), Double.parseDouble(bText), Double.parseDouble(cText), Double.parseDouble(dText), isAscending, quantifierDomain, beginOfInterval, endOfInterval, isContinuous);

                Alert a = new Alert(Alert.AlertType.NONE, "Quantifier successfully created", ButtonType.APPLY);
                a.show();


                addCustomQuantifiers(quantifier);
            }
        });
    }

    public void validateIfSummarizerExists(String summarizerName) {
       List<String> existingSummarizer = List.of("never renovated", "recently renovated", "long ago renovated", "very small basement", "small basement", "large basement", "huge basement", "very small lot", "small lot", "large lot", "huge lot",
                "very small living space", "small living space","large living space", "huge living space", "occasional price", "cheap price", "normal price", "expensive price", "luxury price", "old sale date", "new sale date");

        for (LinguisticVariableValue summarizer: customSummarizers
             ) {
            if (summarizer.getName().equals(summarizerName)) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Summarizer with same name already exists", ButtonType.APPLY);
                a.show();
                throw new RuntimeException("Summarizer with same name already exists");
            }
        }
        for (String summarizer: existingSummarizer
        ) {
            if (summarizer.equals(summarizerName)) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Summarizer with same name already exists", ButtonType.APPLY);
                a.show();
                throw new RuntimeException("Summarizer with same name already exists");
            }
        }
    }

    public void validateIfQuantifierExists(String quantifierName) {
        List<String> existingQuantifiers = List.of("all", "almost none", "almost all", "about half", "much more than 1/4",
                "much less than 3/4", "less than 10000", "less than 7500", "more than 7500", "more than 5500", "about 10000");

        for (Quantifier quantifier: customQuantifiers
        ) {
            if (quantifier.getLabel().equals(quantifierName)) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Quantifier with same name already exists", ButtonType.APPLY);
                a.show();
                throw new RuntimeException("Quantifier with same name already exists");
            }
        }
        for (String quantifier: existingQuantifiers
        ) {
            if (quantifier.equals(quantifierName)) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Quantifier with same name already exists", ButtonType.APPLY);
                a.show();
                throw new RuntimeException("Quantifier with same name already exists");
            }
        }
    }

    public void onGenerateSummarizerClicked() {
        generateSummarizer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String summarizerNameGenText = summarizerNameGen.getText();
                System.out.println(summarizerNameGenText);


                validateIfSummarizerExists(summarizerNameGenText);

                String selectedFunctionType = summarizerFunction.getSelectionModel().getSelectedItem();
                System.out.println(selectedFunctionType);


                String aText = a2.getText().replaceAll(",", ".");
                if (Objects.equals(aText, "")) {
                    aText = "0.0";
                }
                System.out.println(aText);
                String bText = b2.getText().replaceAll(",", ".");
                if (Objects.equals(bText, "")) {
                    bText = "0.0";
                }
                System.out.println(bText);
                String cText = c2.getText().replaceAll(",", ".");
                if (Objects.equals(cText, "")) {
                    cText = "0.0";
                }
                System.out.println(cText);
                String dText = d2.getText().replaceAll(",", ".");
                if (Objects.equals(dText, "")) {
                    dText = "0.0";
                }
                System.out.println(dText);

                //generate summarizer
                DatabaseQueries databaseQueries = new DatabaseQueries();
                var databaseSize = databaseQueries.selectAll().size();

                Generator generator = new Generator();

                boolean isAscending = false;
                if (Objects.equals(selectedFunctionType, "half-triangular ascending")) {
                    selectedFunctionType = "half-triangular";
                    isAscending = true;
                } else if (Objects.equals(selectedFunctionType, "half-triangular descending")) {
                    aText = cText;
                    bText = dText;
                    selectedFunctionType = "half-triangular";
                }
                String selectedLinguisticVariable = summarizerRelatedID.getSelectionModel().getSelectedItem();
                System.out.println(selectedLinguisticVariable);

                List<Double> domain = new ArrayList<>();
                double beginOfInterval = 0.0;
                double endOfInterval = 1.0;
                switch (selectedLinguisticVariable) {
                    case "sale date" -> {
                        domain = databaseQueries.selectDateOfSale("");
                        beginOfInterval = 20140501;
                        endOfInterval = 20150601;
                    }
                    case "price" -> {
                        domain = databaseQueries.selectPrice("");
                        endOfInterval = 1300000;
                    }
                    case "living space" -> {
                        domain = databaseQueries.selectSqftLiving("");
                        endOfInterval = 15000;
                    }
                    case "lot" -> {
                        domain = databaseQueries.selectSqftLot("");
                        endOfInterval = 100000;
                    }
                    case "basement" -> {
                        domain = databaseQueries.selectSqftBasement("");
                        endOfInterval = 1000;
                    }
                    case "year of built" -> {
                        domain = databaseQueries.selectYearBuilt("");
                        endOfInterval = 130;
                    }
                    case "year of renovation" -> {
                        domain = databaseQueries.selectYearRenovated("");
                        beginOfInterval = 1934;
                        endOfInterval = 2015;
                    }
                }

                var summarizer = generator.generateLinguisticVariableValue(summarizerNameGenText, selectedFunctionType, Double.parseDouble(aText), Double.parseDouble(bText), Double.parseDouble(cText), Double.parseDouble(dText), isAscending, domain, beginOfInterval, endOfInterval, true);

                Alert a = new Alert(Alert.AlertType.NONE, "Summarizer successfully created", ButtonType.APPLY);
                a.show();

                customSummarizersType.add(selectedLinguisticVariable);
                addCustomSummarizer(summarizer);

            }
        });
    }


    public void addCustomQuantifiers(Quantifier quantifier) {
        quantifierChoiceId.getItems().add(quantifier.getLabel());
        customQuantifiers.add(quantifier);
    }

    public void addCustomSummarizer(LinguisticVariableValue summarizer) {
        summarizerCBId.getItems().add(summarizer.getName());
        customSummarizers.add(summarizer);
    }

    public List<Quantifier> getCustomQuantifiers() {
        return customQuantifiers;
    }

    public List<Quantifier> getSelectedCustomQuantifiers() {
        List<Quantifier> quantifiers = new ArrayList<>();
        for (String quantifier : quantifierChoiceId.getCheckModel().getCheckedItems()
        ) {
            for (Quantifier customQuantifier : customQuantifiers
            ) {
                if (customQuantifier.getLabel().equals(quantifier)) {
                    quantifiers.add(customQuantifier);
                }
            }
        }
        return quantifiers;
    }

    public List<LinguisticVariableValue> getSelectedCustomSummarizers() {
        List<LinguisticVariableValue> linguisticVariableValues = new ArrayList<>();
        for (String name : summarizerCBId.getCheckModel().getCheckedItems()
        ) {
            for (LinguisticVariableValue customSum : customSummarizers
            ) {
                if (customSum.getName().equals(name)) {
                    linguisticVariableValues.add(customSum);
                }
            }
        }
        return linguisticVariableValues;
    }

    public void createLabelsOnSecondView() {
        quantifierType.getItems().addAll("relative", "absolute");
        quantifierFunction.getItems().addAll("triangular", "trapezoid", "half-triangular ascending", "half-triangular descending", "gaussian");
        summarizerRelatedID.getItems().addAll("sale date", "price", "living space", "lot", "basement", "year of built", "year of renovation");
        summarizerFunction.getItems().addAll("triangular", "trapezoid", "half-triangular ascending", "half-triangular descending", "gaussian");
    }

    public void listenerOnQuantifierFunctionType() {
        quantifierFunction.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                String chosenQuantifierFunction = quantifierFunction.getItems().get((Integer) number2);
                a.setVisible(true);
                a.setText("");
                b.setVisible(true);
                b.setText("");
                c.setVisible(true);
                c.setText("");
                d.setVisible(true);
                d.setText("");
                aLabel.setText("a");
                bLabel.setText("b");
                cLabel.setText("c");
                dLabel.setText("d");
                switch (chosenQuantifierFunction) {
                    case "triangular" -> {
                        dLabel.setText("");
                        d.setVisible(false);
                    }
                    case "half-triangular ascending" -> {
                        cLabel.setText("");
                        dLabel.setText("");
                        c.setVisible(false);
                        d.setVisible(false);
                    }

                    case "half-triangular descending" -> {
                        aLabel.setText("");
                        bLabel.setText("");
                        a.setVisible(false);
                        b.setVisible(false);
                    }
                    case "gaussian" -> {
                        aLabel.setText("x'");
                        bLabel.setText("sigma");
                        cLabel.setText("");
                        dLabel.setText("");
                        c.setVisible(false);
                        d.setVisible(false);
                    }
                }
            }
        });
    }


    public List<LinguisticVariableValue> getCustomSummarizers() {
        return customSummarizers;
    }

    public List<String> getCustomSummarizersType() {
        return customSummarizersType;
    }

    public void listenerOnSummarizerFunctionType() {
        summarizerFunction.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                String chosenSummarizerFunction = summarizerFunction.getItems().get((Integer) number2);
                a2.setVisible(true);
                a2.setText("");
                b2.setVisible(true);
                b2.setText("");
                c2.setVisible(true);
                c2.setText("");
                d2.setVisible(true);
                d2.setText("");
                aLabel2.setText("a");
                bLabel2.setText("b");
                cLabel2.setText("c");
                dLabel2.setText("d");
                switch (chosenSummarizerFunction) {
                    case "triangular" -> {
                        dLabel2.setText("");
                        d2.setVisible(false);
                    }
                    case "half-triangular ascending" -> {
                        cLabel2.setText("");
                        dLabel2.setText("");
                        c2.setVisible(false);
                        d2.setVisible(false);
                    }

                    case "half-triangular descending" -> {
                        aLabel2.setText("");
                        bLabel2.setText("");
                        a2.setVisible(false);
                        b2.setVisible(false);
                    }
                    case "gaussian" -> {
                        aLabel2.setText("x'");
                        bLabel2.setText("sigma");
                        cLabel2.setText("");
                        dLabel2.setText("");
                        c2.setVisible(false);
                        d2.setVisible(false);
                    }
                }
            }
        });
    }
}
