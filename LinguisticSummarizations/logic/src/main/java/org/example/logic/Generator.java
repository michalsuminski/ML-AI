package org.example.logic;

import org.example.fuzzy.*;

import java.util.List;

public class Generator {


    public LinguisticVariableValue generateLinguisticVariableValue(String name, String type, double a, double b, double c, double d,boolean isAscending, List<Double> domain, double beginOfInterval, double endOfInterval, boolean isContinuous) {
        MembershipFunction membershipFunction;
        if (type.equals("triangular")) {
            membershipFunction = new Triangular(a, b, c);
        }else if (type.equals("trapezoid")){
            membershipFunction = new Trapezoid(a, b, c, d);
        }else if (type.equals("half-triangular")){
            membershipFunction = new HalfTriangular(a, b, isAscending);
        }else{
            // w przypadku gaussiana do wywolania funkcji podajemy c jako a i sigme jako b
            membershipFunction = new Gaussian(a, b);
        }
        ClassicSet universeOfDiscourse = new ClassicSet(domain, beginOfInterval, endOfInterval, isContinuous);
        FuzzySet fuzzySet = new FuzzySet(membershipFunction, universeOfDiscourse);
        return new LinguisticVariableValue(name, fuzzySet);
    }

    public Quantifier generateQuantifier(String name, String typeOfFunction, boolean isAbsolute, double a, double b, double c, double d, boolean isAscending, List<Double> domain, double beginOfInterval, double endOfInterval, boolean isContinuous) {
        MembershipFunction membershipFunction;
        if (typeOfFunction.equals("triangular")) {
            membershipFunction = new Triangular(a, b, c);
        }else if (typeOfFunction.equals("trapezoid")){
            membershipFunction = new Trapezoid(a, b, c, d);
        }else if (typeOfFunction.equals("half-triangular")){
            membershipFunction = new HalfTriangular(a, b, isAscending);
        }else{
            // w przypadku gaussiana do wywolania funkcji podajemy c jako a i sigme jako b
            membershipFunction = new Gaussian(a, b);
        }
        ClassicSet universeOfDiscourse = new ClassicSet(domain, beginOfInterval, endOfInterval, isContinuous);
        FuzzySet fuzzySet = new FuzzySet(membershipFunction, universeOfDiscourse);
        return new Quantifier(name, fuzzySet, isAbsolute);
    }
}
