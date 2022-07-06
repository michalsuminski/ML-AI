package org.example.fuzzy;

import java.util.ArrayList;
import java.util.List;

public class FuzzySet {

    // funkcja przynależności dla danego zbioru
    MembershipFunction membershipFunction;

    private final ClassicSet universeOfDiscourse;

    public FuzzySet(MembershipFunction membershipFunction, ClassicSet universeOfDiscourse) {
        this.membershipFunction = membershipFunction;
        this.universeOfDiscourse = universeOfDiscourse;
    }

    public MembershipFunction getMembershipFunction() {
        return membershipFunction;
    }

    // return interval which has the value of alpha
    public ClassicSet getAlphaCut(double alpha, boolean isStrong) {
        List<Double> domain = this.universeOfDiscourse.getDomain();
        List<Double> afterAlphaCut = new ArrayList<>();

        if(isStrong) {
            for (Double elem : domain) {
                if (countDegreeOfMembership(elem) > alpha) {
                    afterAlphaCut.add(elem);
                }
            }
        }else{
            for (Double elem : domain) {
                if (countDegreeOfMembership(elem) >= alpha) {
                    afterAlphaCut.add(elem);
                }
            }
        }
        return new ClassicSet(afterAlphaCut, membershipFunction.getBegin(universeOfDiscourse.getBeginOfInterval()),membershipFunction.getEnd(universeOfDiscourse.getEndOfInterval()), universeOfDiscourse.isContinuous());
    }

    // return interval which has the value of alpha
    public ClassicSet getSupport () {
        // nosnik jest alpha cutem dla alpha=0
        return getAlphaCut(0, true);
    }


    public double getSigmaCount(){
        double cardinality = 0.0;
        for (var record: this.universeOfDiscourse.getDomain()
        ) {
            cardinality += countDegreeOfMembership(record);
        }
        return cardinality;
    }

    public double getDegreeOfFuzziness(){
        if (!this.universeOfDiscourse.isContinuous()) {
            double supportSize = this.getSupport().getDomain().size();
            double numberOfElements = this.universeOfDiscourse.getDomain().size();
            return supportSize / numberOfElements;
        }else{
            double supportSize = this.getSupport().getInterval();
            double UoDsize = this.universeOfDiscourse.getInterval();
            return supportSize / UoDsize;
        }
    }

    public double IntSimpson(double a, double b,int n){
        int i,z;
        double h,s;

        n=n+n;
        s = countDegreeOfMembership(a)*countDegreeOfMembership(b);
        h = (b-a)/n;
        z = 4;

        for(i = 1; i<n; i++){
            s = s + z * countDegreeOfMembership(a+i*h);
            z = 6 - z;
        }
        return (s * h)/3;
    }


    public double getCLM(){
        var min_value = universeOfDiscourse.getBeginOfInterval();
        var max_value = universeOfDiscourse.getEndOfInterval();
        double cardinality = 0.0;
        cardinality += IntSimpson(min_value,max_value, 100);
        return cardinality;
    }

    // function to calculate degree of membership
    public double countDegreeOfMembership(double x) {
        return membershipFunction.countMembership(x);
    }

    public ClassicSet getUniverseOfDiscourse() {
        return universeOfDiscourse;
    }
}
