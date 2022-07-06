package org.example.fuzzy;

public interface MembershipFunction {
    public double countMembership(double x);
    public double getBegin(double begin);
    public double getEnd(double end);
}
