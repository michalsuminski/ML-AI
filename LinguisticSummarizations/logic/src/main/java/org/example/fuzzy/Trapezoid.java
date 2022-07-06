package org.example.fuzzy;

public class Trapezoid implements MembershipFunction {
    private double a;
    private double b;
    private double c;
    private double d;

    public Trapezoid(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public double countMembership(double x) {
        if(x > a && x <= b){
            double nominator = x - a;
            double denominator = b - a;
            return nominator/denominator;
        }else if(x > b && x <= c){
            return 1;
        }else if(x > c && x <= d){
            double nominator = d - x;
            double denominator = d - c;
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
        return d;
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

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }
}
