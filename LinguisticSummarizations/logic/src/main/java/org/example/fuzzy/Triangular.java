package org.example.fuzzy;

public class Triangular implements MembershipFunction {
    private double a;
    private double b;
    private double c;

    public Triangular(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double countMembership(double x) {
        if(x > a && x <= b){
            double nominator = x - a;
            double denominator = b - a;
            return nominator/denominator;
        }else if(x > b && x <= c){
            double nominator = c - x;
            double denominator = c - b;
            return nominator/denominator;
        }else{
            return 0;
        }
    }

    @Override
    public double getBegin(double begin) {
        return a;
    }

    @Override
    public double getEnd(double end) {
        return c;
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

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }
}
