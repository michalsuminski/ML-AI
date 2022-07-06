package org.example.fuzzy;

public class Gaussian implements MembershipFunction {
    // todo check in book
    private double c;
    private double sigma;

    public Gaussian(double c, double sigma) {
        this.c = c;
        this.sigma = sigma;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    @Override
    public double countMembership(double x) {
        return 0;
    }

    @Override
    public double getBegin(double begin) {
        return 0;
    }

    @Override
    public double getEnd(double end) {
        return 0;
    }

}
