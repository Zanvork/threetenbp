/*
 * Copyright (c) 2007-2009, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.time.calendar.field;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReferenceArray;

import javax.time.MathUtils;
import javax.time.calendar.Calendrical;
import javax.time.calendar.CalendricalRule;
import javax.time.calendar.DateMatcher;
import javax.time.calendar.DateProvider;
import javax.time.calendar.DateTimeFieldRule;
import javax.time.calendar.ISOChronology;
import javax.time.calendar.IllegalCalendarFieldValueException;
import javax.time.calendar.LocalDate;

/**
 * A representation of a week of week-based-year in the ISO-8601 calendar system.
 * <p>
 * WeekOfWeekBasedYear is an immutable time field that can only store a week of week-based-year.
 * It is a type-safe way of representing a week of week-based-year in an application.
 * <p>
 * The week of week-based-year is a field that should be used in combination with
 * the WeekBasedYear field. Together they represent the ISO-8601 week based date
 * calculation described in {@link WeekBasedYear}.
 * <p>
 * Static factory methods allow you to construct instances.
 * The week of week-based-year may be queried using getValue().
 * <p>
 * WeekOfWeekBasedYear is immutable and thread-safe.
 *
 * @author Michael Nascimento Santos
 * @author Stephen Colebourne
 */
public final class WeekOfWeekBasedYear
        implements Calendrical, Comparable<WeekOfWeekBasedYear>, DateMatcher, Serializable {

    /**
     * A serialization identifier for this instance.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Cache of singleton instances.
     */
    private static final AtomicReferenceArray<WeekOfWeekBasedYear> CACHE = new AtomicReferenceArray<WeekOfWeekBasedYear>(53);

    /**
     * The week of week-based-year being represented.
     */
    private final int weekOfWeekyear;

    //-----------------------------------------------------------------------
    /**
     * Gets the rule that defines how the week of week-based-year field operates.
     * <p>
     * The rule provides access to the minimum and maximum values, and a
     * generic way to access values within a calendrical.
     *
     * @return the week of week-based-year rule, never null
     */
    public static DateTimeFieldRule<Integer> rule() {
        return ISOChronology.weekOfWeekBasedYearRule();
    }

    //-----------------------------------------------------------------------
    /**
     * Obtains an instance of <code>WeekOfWeekBasedYear</code> from a value.
     * <p>
     * A week of week-based-year object represents one of the 53 weeks of the year,
     * from 1 to 53. These are cached internally and returned as singletons,
     * so they can be compared using ==.
     *
     * @param weekOfWeekyear  the week of week-based-year to represent, from 1 to 53
     * @return the WeekOfWeekBasedYear singleton, never null
     * @throws IllegalCalendarFieldValueException if the weekOfWeekyear is invalid
     */
    public static WeekOfWeekBasedYear weekOfWeekyear(int weekOfWeekyear) {
        try {
            WeekOfWeekBasedYear result = CACHE.get(--weekOfWeekyear);
            if (result == null) {
                WeekOfWeekBasedYear temp = new WeekOfWeekBasedYear(weekOfWeekyear + 1);
                CACHE.compareAndSet(weekOfWeekyear, null, temp);
                result = CACHE.get(weekOfWeekyear);
            }
            return result;
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalCalendarFieldValueException(rule(), ++weekOfWeekyear, 1, 53);
        }
    }

    /**
     * Obtains an instance of <code>WeekOfWeekBasedYear</code> from a date provider.
     * <p>
     * This can be used extract a week of week-based-year object directly from
     * any implementation of DateProvider, including those in other calendar systems.
     *
     * @param dateProvider  the date provider to use, not null
     * @return the WeekOfWeekBasedYear singleton, never null
     */
    public static WeekOfWeekBasedYear weekOfWeekyear(DateProvider dateProvider) {
        LocalDate date = LocalDate.date(dateProvider);
        Year year = WeekBasedYear.computeYear(date);

        LocalDate yearStart = LocalDate.date(year, MonthOfYear.JANUARY, DayOfMonth.dayOfMonth(4));

        return weekOfWeekyear(MathUtils.safeToInt((date.toModifiedJulianDays() - yearStart.toModifiedJulianDays() +
                yearStart.getDayOfWeek().getValue() - 1) / 7 + 1));
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance with the specified week of week-based-year.
     *
     * @param weekOfWeekyear  the week of week-based-year to represent
     */
    private WeekOfWeekBasedYear(int weekOfWeekyear) {
        this.weekOfWeekyear = weekOfWeekyear;
    }

    /**
     * Resolve the singleton.
     *
     * @return the singleton, never null
     */
    private Object readResolve() {
        return weekOfWeekyear(weekOfWeekyear);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the value of the specified calendrical rule.
     * <p>
     * This method queries the value of the specified calendrical rule.
     * If the value cannot be returned for the rule from this instance then
     * <code>null</code> will be returned.
     *
     * @param rule  the rule to use, not null
     * @return the value for the rule, null if the value cannot be returned
     */
    public <T> T get(CalendricalRule<T> rule) {
        return rule().deriveValueFor(rule, weekOfWeekyear, this);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the week of week-based-year value.
     *
     * @return the week of week-based-year, from 1 to 53
     */
    public int getValue() {
        return weekOfWeekyear;
    }


    /**
     * Checks if the value of this week of week-based-year matches the input date.
     *
     * @param date  the date to match, not null
     * @return true if the date matches, false otherwise
     */
    public boolean matchesDate(LocalDate date) {
        return WeekOfWeekBasedYear.weekOfWeekyear(date).equals(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if this week of weekyear is valid for the specified week-based-year.
     *
     * @param weekyear  the weekyear to validate against, not null
     * @return true if this week of weekyear is valid for the week-based-year
     */
    public boolean isValid(WeekBasedYear weekyear) {
        if (weekyear == null) {
            throw new NullPointerException("Weekyear cannot be null");
        }
        return (weekOfWeekyear < 53 || weekyear.lengthInWeeks() == 53);
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this week of week-based-year instance to another.
     *
     * @param otherWeekOfWeekBasedYear  the other week of week-based-year instance, not null
     * @return the comparator value, negative if less, positive if greater
     * @throws NullPointerException if otherWeekOfWeekBasedYear is null
     */
    public int compareTo(WeekOfWeekBasedYear otherWeekOfWeekBasedYear) {
        int thisValue = this.weekOfWeekyear;
        int otherValue = otherWeekOfWeekBasedYear.weekOfWeekyear;
        return (thisValue < otherValue ? -1 : (thisValue == otherValue ? 0 : 1));
    }

    /**
     * Is this week of week-based-year instance after the specified week of week-based-year.
     *
     * @param otherWeekOfWeekBasedYear  the other week of week-based-year instance, not null
     * @return true if this is after the specified week of week-based-year
     * @throws NullPointerException if otherWeekOfWeekBasedYear is null
     */
    public boolean isAfter(WeekOfWeekBasedYear otherWeekOfWeekBasedYear) {
        return compareTo(otherWeekOfWeekBasedYear) > 0;
    }

    /**
     * Is this week of week-based-year instance before the specified week of week-based-year.
     *
     * @param otherWeekOfWeekBasedYear  the other week of week-based-year instance, not null
     * @return true if this is before the specified week of week-based-year
     * @throws NullPointerException if otherWeekOfWeekBasedYear is null
     */
    public boolean isBefore(WeekOfWeekBasedYear otherWeekOfWeekBasedYear) {
        return compareTo(otherWeekOfWeekBasedYear) < 0;
    }

    //-----------------------------------------------------------------------
    /**
     * Is this instance equal to that specified, evaluating the week of week-based-year.
     *
     * @param otherWeekOfWeekBasedYear  the other week of week-based-year instance, null returns false
     * @return true if the week of week-based-year is the same
     */
    @Override
    public boolean equals(Object otherWeekOfWeekBasedYear) {
        return this == otherWeekOfWeekBasedYear;
    }

    /**
     * A hash code for the week of week-based-year object.
     *
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        return weekOfWeekyear;
    }

    /**
     * A string describing the week of week-based-year object.
     *
     * @return a string describing this object
     */
    @Override
    public String toString() {
        return "WeekOfWeekBasedYear=" + getValue();
    }

}