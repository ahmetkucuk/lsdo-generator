package app.models;

import java.sql.Timestamp;
/**
 * Class for handling time intervals in trajectories
 *
 * @author berkay - Jul 15, 2016
 *
 */
public class TInterval implements Comparable<TInterval>{

    private long start; // inclusive
    private long end; // exclusive
    private long SAMPLING_PERIOD = -1L; // for entire dataset - handle
    // with care
    private boolean PERIOD_SET = false; // for entire dataset - handle
    // with care

    public TInterval(long start, long end) { // [start, end)
        this.start = start;
        this.end = end;

    }

    public TInterval(long start) throws SamplingPeriodException {
        if (isIntervalPeriodSet()) {
            this.start = start;
            this.end = start + SAMPLING_PERIOD;
        } else {
            throw new SamplingPeriodException("Sampling period is not set for TInterval class \n\t(Start time: " + start
                    + ", End time: Not Applicable)");
        }
    }

    public TInterval(String startTimestamp, String endTimestamp) {
        // [start, end)
        this.start = convertTimestampToLong(startTimestamp);
        this.end = convertTimestampToLong(endTimestamp);

    }

    public TInterval(String startTimestamp) throws SamplingPeriodException {
        if (isIntervalPeriodSet()) {
            this.start = convertTimestampToLong(startTimestamp);
            this.end = this.start + SAMPLING_PERIOD;
        } else {
            throw new SamplingPeriodException("Sampling period is not set for TInterval class \n\t(Start time: " + start
                    + ", End time: Not Applicable)");
        }
    }

    public boolean overlaps(TInterval timeInterval) {

        // Proof:
        // Let Condition I (C1) mean that interval A strictly after interval B
        // 					|---- A ------|
        // |--- B -----|
        // (True if A.start > B.end)
        // Let Condition II (C2) mean that A is strictly Before B
        // |---- A -----|
        // 					|--- B ----|
        // (True if A.end < B.start)
        // Then Overlap exists if neither C1 Nor C2 is true -
        // (If one range is neither completely after the other,
        // nor completely before the other, then they must overlap.)
        // De Morgan's laws says that: Not (C1 Or C2) <=> Not C2 And Not C1
        // which is (A.start <= B.end) and (A.end >= B.start).
        // We use exclusive end times (so equality in the statements are
        // omitted)

        return (this.start < timeInterval.end) && (this.end > timeInterval.start);
    }

    public boolean startsBefore(TInterval timeInterval) {
        //Proof:
        //If you need proof, probably you shouldn't use this code.
        return this.start < timeInterval.start;
    }

    public boolean startsAfter(TInterval interval) {
        //Proof: see above
        return this.start > interval.start;
    }

    public boolean endsBefore(TInterval interval){
        return this.end < interval.end;
    }

    public boolean endsBeforeStartOf(TInterval interval){
        return this.end <= interval.start;
    }

    public boolean endsAfter(TInterval interval){
        return this.end > interval.end;
    }

    public boolean contains(long timePoint) {
        return (timePoint >= this.start && timePoint < this.end);
    }




    public void setSamplingPeriod(long period) {
        if (period > 0) {
            PERIOD_SET = true;
            SAMPLING_PERIOD = period;
        }
    }

    public boolean isIntervalPeriodSet() {
        return PERIOD_SET;
    }

    public boolean isValid() {
        if (start > end) {
            return false;
        } else if (isIntervalPeriodSet()) {
            if (start + SAMPLING_PERIOD != end) {
                return false;
            }
        }
        return true;
    }

    public static String convertLongToTimestamp(long time) {
        return new Timestamp(time).toString();
    }

    public static long convertTimestampToLong(String timestampString) {
        return Timestamp.valueOf(timestampString).getTime();
    }

    public long getStartTime() {
        return start;
    }

    public long getEndTime() {
        return end;
    }

    public String getStartTimeString() {
        return convertLongToTimestamp(start);
    }

    public String getEndTimeString() {
        return convertLongToTimestamp(end);
    }

    public TInterval intersection(TInterval desiredTInterval){

        long iStart = Math.max(this.start, desiredTInterval.start);
        long iEnd = Math.min(this.end, desiredTInterval.end);
        TInterval intersectionInterval = new TInterval(iStart, iEnd);

        if(intersectionInterval.isValid()){
            return intersectionInterval;
        } else{
            return null;
        }
    }

    public double getLifespan() {
        return (this.end - this.start);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TInterval) {
            TInterval o = (TInterval)obj;
            if(this.start == o.start && this.end == o.end) return true;
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(start);
        result = 31 * result + Long.hashCode(start);
        return result;
    }

    public String toString(){
        return "[" + convertLongToTimestamp(this.start) +", "
                + convertLongToTimestamp(this.end) +")";
    }

    @Override
    public int compareTo(TInterval tI) {
        return new Long(this.start).compareTo(tI.start);
    }



}

class SamplingPeriodException extends Exception {

    /*
     * auto generated serial version uid
     */
    private static final long serialVersionUID = -6297563735544962623L;

    public SamplingPeriodException(String message) {
        super(message);
    }

    public SamplingPeriodException(String message, Throwable throwable) {
        super(message, throwable);
    }

}