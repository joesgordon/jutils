package org.jutils.apps.summer.data;

public class InvalidChecksum
{
    public final SumFile readSum;
    public final SumFile calcSum;

    public InvalidChecksum( SumFile readSum, SumFile calcSum )
    {
        this.readSum = readSum;
        this.calcSum = calcSum;
    }
}
