package com.example_view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class SummaryTable {

    private SimpleStringProperty col1;

    private SimpleStringProperty col2;

    private SimpleStringProperty col3;

    private SimpleStringProperty col4;

    private SimpleStringProperty col5;

    private SimpleStringProperty col6;

    private SimpleStringProperty col7;

    private SimpleStringProperty col8;

    private SimpleStringProperty col9;

    private SimpleStringProperty col10;

    private SimpleStringProperty col11;

    private SimpleStringProperty col12;

    private SimpleStringProperty col13;

    private CheckBox checkBox;




    public SummaryTable(String col1, String col2, String col3, String col4, String col5, String col6, String col7, String col8, String col9, String col10, String col11, String col12, String col13) {
        this.col1 = new SimpleStringProperty(col1);
        this.col2 = new SimpleStringProperty(col2);
        this.col3 = new SimpleStringProperty(col3);
        this.col4 = new SimpleStringProperty(col4);
        this.col5 = new SimpleStringProperty(col5);
        this.col6 = new SimpleStringProperty(col6);
        this.col7 = new SimpleStringProperty(col7);
        this.col8 = new SimpleStringProperty(col8);
        this.col9 = new SimpleStringProperty(col9);
        this.col10 = new SimpleStringProperty(col10);
        this.col11 = new SimpleStringProperty(col11);
        this.col12 = new SimpleStringProperty(col12);
        this.col13 = new SimpleStringProperty(col13);
        this.checkBox = new CheckBox();
    }

    public String getCol1() {
        return col1.get();
    }

    public SimpleStringProperty col1Property() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = new SimpleStringProperty(col1);
    }

    public String getCol2() {
        return col2.get();
    }

    public SimpleStringProperty col2Property() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = new SimpleStringProperty(col2);
    }

    public String getCol3() {
        return col3.get();
    }

    public SimpleStringProperty col3Property() {
        return col3;
    }

    public void setCol3(String col3) {
        this.col3 = new SimpleStringProperty(col3);
    }

    public String getCol4() {
        return col4.get();
    }

    public SimpleStringProperty col4Property() {
        return col4;
    }

    public void setCol4(String col4) {
        this.col4 = new SimpleStringProperty(col4);
    }

    public String getCol5() {
        return col5.get();
    }

    public SimpleStringProperty col5Property() {
        return col5;
    }

    public void setCol5(String col5) {
        this.col5 = new SimpleStringProperty(col5);
    }

    public String getCol6() {
        return col6.get();
    }

    public SimpleStringProperty col6Property() {
        return col6;
    }

    public void setCol6(String col6) {
        this.col6 = new SimpleStringProperty(col6);
    }

    public String getCol7() {
        return col7.get();
    }

    public SimpleStringProperty col7Property() {
        return col7;
    }

    public void setCol7(String col7) {
        this.col7 = new SimpleStringProperty(col7);
    }

    public String getCol8() {
        return col8.get();
    }

    public SimpleStringProperty col8Property() {
        return col8;
    }

    public void setCol8(String col8) {
        this.col8 = new SimpleStringProperty(col8);
    }

    public String getCol9() {
        return col9.get();
    }

    public SimpleStringProperty col9Property() {
        return col9;
    }

    public void setCol9(String col9) {
        this.col9 = new SimpleStringProperty(col9);
    }

    public String getCol10() {
        return col10.get();
    }

    public SimpleStringProperty col10Property() {
        return col10;
    }

    public void setCol10(String col10) {
        this.col10 = new SimpleStringProperty(col10);;
    }

    public String getCol11() {
        return col11.get();
    }

    public SimpleStringProperty col11Property() {
        return col11;
    }

    public void setCol11(String col11) {
        this.col11 = new SimpleStringProperty(col11);
    }

    public String getCol12() {
        return col12.get();
    }

    public SimpleStringProperty col12Property() {
        return col12;
    }

    public void setCol12(String col12) {
        this.col12 = new SimpleStringProperty(col12);
    }

    public String getCol13() {
        return col13.get();
    }

    public SimpleStringProperty col13Property() {
        return col13;
    }

    public void setCol13(String col13) {
        this.col13 = new SimpleStringProperty(col13);
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public String getSummaryAndQualityMeasures() {
        return "Summary: " + getCol1() + "\nQuality Measures: " + "T1: " + getCol2() +
                ", T2: " + getCol3() + ", T3: " + getCol4() + ", T4: " + getCol5() +
                ", T5: " + getCol6() + ", T6: " + getCol7() + ", T7: " + getCol8() +
                ", T8: " + getCol9() + ", T9: " + getCol10() + ", T10: " + getCol11()
                + ", T11: " + getCol12() +  ", T12: " + getCol13() + ".\n";
    }
}
