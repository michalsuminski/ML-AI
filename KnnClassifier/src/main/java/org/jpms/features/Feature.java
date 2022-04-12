package org.jpms.features;

public class Feature {
    private String textFeature;
    private int numberFeature;

    public Feature(String textFeature) {
        this.textFeature = textFeature;
    }

    public Feature(int numberFeature) {
        this.numberFeature = numberFeature;
    }

    public String getTextFeature() {
        return textFeature;
    }

    public void setTextFeature(String textFeature) {
        this.textFeature = textFeature;
    }

    public int getNumberFeature() {
        return numberFeature;
    }

    @Override
    public String toString() {
        if (textFeature != null) {
            return textFeature;
        }
        else {
            return String.valueOf(numberFeature);
        }
    }

    public void setNumberFeature(int numberFeature) {
        this.numberFeature = numberFeature;
    }
}
