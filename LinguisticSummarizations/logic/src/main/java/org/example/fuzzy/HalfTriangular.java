package org.example.fuzzy;

public class HalfTriangular implements MembershipFunction {
    private double a;
    private double b;
    private boolean isAscending;

    public HalfTriangular(double a, double b, boolean isAscending) {
        this.a = a;
        this.b = b;
        this.isAscending = isAscending;

    }

    @Override
    public double countMembership(double x) {
        // funkcja malejaca
        if (!isAscending) {
            if (x <= a) {
                return 1;
            } else if (x > a && x <= b) {
                double nominator = b - x;
                double denominator = b - a;
                return nominator / denominator;
            } else {
                return 0;
            }
        }else{
            if (x <= a) {
                return 0;
            } else if (x > a && x <= b) {
                double nominator = x - a;
                double denominator = b - a;
                return nominator / denominator;
            } else {
                return 1;
            }
        }
    }

    @Override
    public double getBegin(double begin) {
        if(isAscending){
            return a;
        }else{
            return begin;
        }
    }

    @Override
    public double getEnd(double end) {
        if(isAscending){
            return end;
        }else{
            return b;
        }
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }
}
