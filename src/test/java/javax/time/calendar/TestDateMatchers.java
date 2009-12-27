/*
 * Copyright (c) 2008, Stephen Colebourne & Michael Nascimento Santos
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
package javax.time.calendar;

import static javax.time.calendar.LocalDate.*;
import static javax.time.calendar.field.DayOfWeek.*;
import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Collections;

import javax.time.calendar.field.DayOfWeek;
import javax.time.calendar.field.MonthOfYear;

import org.testng.annotations.Test;

/**
 * Test DateMatchers.
 *
 * @author Michael Nascimento Santos
 * @author Stephen Colebourne
 */
@Test
public class TestDateMatchers {

    @SuppressWarnings("unchecked")
    public void test_constructor() throws Exception {
        for (Constructor constructor : DateMatchers.class.getDeclaredConstructors()) {
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            constructor.newInstance(Collections.nCopies(constructor.getParameterTypes().length, null).toArray());
        }
    }

    //-----------------------------------------------------------------------
    // leapYear()
    //-----------------------------------------------------------------------
    public void test_leapYear_serialization() throws IOException, ClassNotFoundException {
        DateMatcher leapYear = DateMatchers.leapYear();
        assertTrue(leapYear instanceof Serializable);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(leapYear);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
                baos.toByteArray()));
        assertSame(ois.readObject(), leapYear);
    }

    public void factory_leapYear() {
        assertNotNull(DateMatchers.leapYear());
        assertSame(DateMatchers.leapYear(), DateMatchers.leapYear());
    }

    public void test_leapYear() {
        assertEquals(DateMatchers.leapYear().matchesDate(LocalDate.date(1999, 1, 1)), false);
        assertEquals(DateMatchers.leapYear().matchesDate(LocalDate.date(2000, 1, 1)), true);
        assertEquals(DateMatchers.leapYear().matchesDate(LocalDate.date(2001, 1, 1)), false);
        assertEquals(DateMatchers.leapYear().matchesDate(LocalDate.date(2002, 1, 1)), false);
        assertEquals(DateMatchers.leapYear().matchesDate(LocalDate.date(2003, 1, 1)), false);
        assertEquals(DateMatchers.leapYear().matchesDate(LocalDate.date(2004, 1, 1)), true);
        assertEquals(DateMatchers.leapYear().matchesDate(LocalDate.date(2005, 1, 1)), false);
        
        assertEquals(DateMatchers.leapYear().matchesDate(LocalDate.date(1500, 1, 1)), false);
        assertEquals(DateMatchers.leapYear().matchesDate(LocalDate.date(1600, 1, 1)), true);
        assertEquals(DateMatchers.leapYear().matchesDate(LocalDate.date(1700, 1, 1)), false);
        assertEquals(DateMatchers.leapYear().matchesDate(LocalDate.date(1800, 1, 1)), false);
        assertEquals(DateMatchers.leapYear().matchesDate(LocalDate.date(1900, 1, 1)), false);
    }

    //-----------------------------------------------------------------------
    // leapDay()
    //-----------------------------------------------------------------------
    public void test_leapDay_serialization() throws IOException, ClassNotFoundException {
        DateMatcher leapDay = DateMatchers.leapDay();
        assertTrue(leapDay instanceof Serializable);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(leapDay);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
                baos.toByteArray()));
        assertSame(ois.readObject(), leapDay);
    }

    public void factory_leapDay() {
        assertNotNull(DateMatchers.leapDay());
        assertSame(DateMatchers.leapDay(), DateMatchers.leapDay());
    }

    public void test_leapDay_nonLeap() {
        LocalDate date = date(2007, 1, 1);
        for (int i = 1; i <= 365; i++) {
            assertEquals(DateMatchers.leapDay().matchesDate(date), false);
            date = date.plusDays(1);
        }
    }

    public void test_leapDay_leap() {
        LocalDate date = date(2008, 1, 1);
        for (int i = 1; i <= 366; i++) {
            assertEquals(DateMatchers.leapDay().matchesDate(date), i == 60);
            date = date.plusDays(1);
        }
    }

    //-----------------------------------------------------------------------
    // lastDayOfMonth()
    //-----------------------------------------------------------------------
    public void test_lastDayOfMonth_serialization() throws IOException, ClassNotFoundException {
        DateMatcher lastDayOfMonth = DateMatchers.lastDayOfMonth();
        assertTrue(lastDayOfMonth instanceof Serializable);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(lastDayOfMonth);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
                baos.toByteArray()));
        assertSame(ois.readObject(), lastDayOfMonth);
    }

    public void factory_lastDayOfMonth() {
        assertNotNull(DateMatchers.lastDayOfMonth());
        assertSame(DateMatchers.lastDayOfMonth(), DateMatchers.lastDayOfMonth());
    }

    public void test_lastDayOfMonth_nonLeap() {
        for (MonthOfYear month : MonthOfYear.values()) {
            int lastDayOfMonthValue = month.lengthInDays(2007);
            for (int i = 1; i <= lastDayOfMonthValue; i++) {
                LocalDate date = date(2007, month, i);
                assertEquals(DateMatchers.lastDayOfMonth().matchesDate(date), lastDayOfMonthValue == i);
            }
        }
    }

    public void test_lastDayOfMonth_leap() {
        for (MonthOfYear month : MonthOfYear.values()) {
            int lastDayOfMonthValue = month.lengthInDays(2008);
            for (int i = 1; i <= month.lengthInDays(2008); i++) {
                LocalDate date = date(2008, month, i);
                assertEquals(DateMatchers.lastDayOfMonth().matchesDate(date), lastDayOfMonthValue == i);
            }
        }
    }

    //-----------------------------------------------------------------------
    // lastDayOfYear()
    //-----------------------------------------------------------------------
    public void test_lastDayOfYear_serialization() throws IOException, ClassNotFoundException {
        DateMatcher lastDayOfYear = DateMatchers.lastDayOfYear();
        assertTrue(lastDayOfYear instanceof Serializable);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(lastDayOfYear);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
                baos.toByteArray()));
        assertSame(ois.readObject(), lastDayOfYear);
    }

    public void factory_lastDayOfYear() {
        assertNotNull(DateMatchers.lastDayOfYear());
        assertSame(DateMatchers.lastDayOfYear(), DateMatchers.lastDayOfYear());
    }

    public void test_lastDayOfYear_nonLeap() {
        for (MonthOfYear month : MonthOfYear.values()) {
            for (int i = 1; i <= month.lengthInDays(2007); i++) {
                LocalDate date = date(2007, month, i);
                assertEquals(DateMatchers.lastDayOfYear().matchesDate(date), month == MonthOfYear.DECEMBER && i == 31);
            }
        }
    }

    public void test_lastDayOfYear_leap() {
        for (MonthOfYear month : MonthOfYear.values()) {
            for (int i = 1; i <= month.lengthInDays(2008); i++) {
                LocalDate date = date(2008, month, i);
                assertEquals(DateMatchers.lastDayOfYear().matchesDate(date), month == MonthOfYear.DECEMBER && i == 31);
            }
        }
    }

    //-----------------------------------------------------------------------
    // dayOfWeekInMonth()
    //-----------------------------------------------------------------------
    public void test_dayOfWeekInMonth_serialization() throws IOException, ClassNotFoundException {
        DateMatcher dayOfWeekInMonth = DateMatchers.dayOfWeekInMonth(1, SUNDAY);
        assertTrue(dayOfWeekInMonth instanceof Serializable);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(dayOfWeekInMonth);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
                baos.toByteArray()));
        assertEquals(ois.readObject(), dayOfWeekInMonth);
    }

    public void factory_dayOfWeekInMonth() {
        assertNotNull(DateMatchers.dayOfWeekInMonth(1, MONDAY));
        assertEquals(DateMatchers.dayOfWeekInMonth(1, MONDAY), DateMatchers.dayOfWeekInMonth(1, MONDAY));
        assertEquals(DateMatchers.dayOfWeekInMonth(2, MONDAY), DateMatchers.dayOfWeekInMonth(2, MONDAY));
    }

    @Test(expectedExceptions=IllegalArgumentException.class)
    public void factory_dayOfWeekInMonth_ordinalTooSmall() {
        DateMatchers.dayOfWeekInMonth(0, MONDAY);
    }

    @Test(expectedExceptions=IllegalArgumentException.class)
    public void factory_dayOfWeekInMonth_ordinalTooBig() {
        DateMatchers.dayOfWeekInMonth(6, MONDAY);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void factory_dayOfWeekInMonth_nullDayOfWeek() {
        DateMatchers.dayOfWeekInMonth(1, null);
    }

    public void test_dayOfWeekInMonth_equals() {
        final DateMatcher mondayInFirstWeek = DateMatchers.dayOfWeekInMonth(1, MONDAY);
        assertFalse(mondayInFirstWeek.equals(null));
        assertFalse(mondayInFirstWeek.equals(new Object()));
        assertFalse(mondayInFirstWeek.equals(DateMatchers.lastDayOfMonth()));
        assertFalse(mondayInFirstWeek.equals(DateMatchers.dayOfWeekInMonth(2, MONDAY)));
        assertFalse(mondayInFirstWeek.equals(DateMatchers.dayOfWeekInMonth(1, TUESDAY)));
        assertTrue(mondayInFirstWeek.equals(mondayInFirstWeek));
        assertTrue(mondayInFirstWeek.equals(DateMatchers.dayOfWeekInMonth(1, MONDAY)));
    }

    public void test_dayOfWeekInMonth_hashCode() {
        assertEquals(DateMatchers.dayOfWeekInMonth(1, MONDAY).hashCode(), DateMatchers.dayOfWeekInMonth(1, MONDAY).hashCode());
        assertEquals(DateMatchers.dayOfWeekInMonth(1, TUESDAY).hashCode(), DateMatchers.dayOfWeekInMonth(1, TUESDAY).hashCode());
        assertEquals(DateMatchers.dayOfWeekInMonth(2, MONDAY).hashCode(), DateMatchers.dayOfWeekInMonth(2, MONDAY).hashCode());
    }

    public void test_dayOfWeekInMonth() {
        for (MonthOfYear month : MonthOfYear.values()) {
            DayOfWeek firstReference = null;
            int expectedOrdinal = 1;

            for (int i = 1; i <= month.lengthInDays(2007); i++) {
                LocalDate date = date(2007, month, i);
                DayOfWeek expectedDOW = date.getDayOfWeek();

                if (firstReference == null) {
                    firstReference = date.getDayOfWeek();
                } else if (expectedDOW == firstReference) {
                    expectedOrdinal++;
                }

                for (DayOfWeek dow : DayOfWeek.values()) {
                    for (int ordinal = 1; ordinal <= 5; ordinal++) {
                        assertEquals(DateMatchers.dayOfWeekInMonth(ordinal, dow).matchesDate(date), ordinal == expectedOrdinal && 
                                dow == expectedDOW);
                    }
                }
            }
        }
    }

    //-----------------------------------------------------------------------
    // firstInMonth()
    //-----------------------------------------------------------------------
    public void test_firstInMonth_serialization() throws IOException, ClassNotFoundException {
        DateMatcher firstInMonth = DateMatchers.firstInMonth(SUNDAY);
        assertTrue(firstInMonth instanceof Serializable);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(firstInMonth);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
                baos.toByteArray()));
        assertEquals(ois.readObject(), firstInMonth);
    }

    public void factory_firstInMonth() {
        assertNotNull(DateMatchers.firstInMonth(MONDAY));
        assertEquals(DateMatchers.firstInMonth(MONDAY), DateMatchers.firstInMonth(MONDAY));
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void factory_firstInMonth_nullDayOfWeek() {
        DateMatchers.firstInMonth(null);
    }

    public void test_firstInMonth_equals() {
        final DateMatcher mondayInFirstWeek = DateMatchers.firstInMonth(MONDAY);
        assertFalse(mondayInFirstWeek.equals(null));
        assertFalse(mondayInFirstWeek.equals(new Object()));
        assertFalse(mondayInFirstWeek.equals(DateMatchers.lastDayOfMonth()));
        assertFalse(mondayInFirstWeek.equals(DateMatchers.firstInMonth(TUESDAY)));
        assertTrue(mondayInFirstWeek.equals(mondayInFirstWeek));
        assertTrue(mondayInFirstWeek.equals(DateMatchers.firstInMonth(MONDAY)));
    }

    public void test_firstInMonth_hashCode() {
        assertEquals(DateMatchers.firstInMonth(MONDAY).hashCode(), DateMatchers.firstInMonth(MONDAY).hashCode());
        assertEquals(DateMatchers.firstInMonth(TUESDAY).hashCode(), DateMatchers.firstInMonth(TUESDAY).hashCode());
    }

    public void test_firstInMonth() {
        for (MonthOfYear month : MonthOfYear.values()) {
            for (int i = 1; i <= month.lengthInDays(2007); i++) {
                LocalDate date = date(2007, month, i);

                for (DayOfWeek dow : DayOfWeek.values()) {
                    assertEquals(DateMatchers.firstInMonth(dow).matchesDate(date), i < 8 && dow == date.getDayOfWeek());
                }
            }
        }
    }

    //-----------------------------------------------------------------------
    // weekendDay()
    //-----------------------------------------------------------------------
    public void test_weekendDay_serialization() throws IOException, ClassNotFoundException {
        DateMatcher weekendDay = DateMatchers.weekendDay();
        assertTrue(weekendDay instanceof Serializable);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(weekendDay);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
                baos.toByteArray()));
        assertSame(ois.readObject(), weekendDay);
    }

    public void factory_weekendDay() {
        assertNotNull(DateMatchers.weekendDay());
        assertSame(DateMatchers.weekendDay(), DateMatchers.weekendDay());
    }

    public void test_weekendDay() {
        for (MonthOfYear month : MonthOfYear.values()) {
            for (int i = 1; i <= month.lengthInDays(2007); i++) {
                LocalDate date = date(2007, month, i);
                DayOfWeek dayOfWeek = date.getDayOfWeek();
                assertEquals(DateMatchers.weekendDay().matchesDate(date),
                        dayOfWeek == DayOfWeek.SATURDAY || 
                        dayOfWeek == DayOfWeek.SUNDAY);
            }
        }
    }

    //-----------------------------------------------------------------------
    // nonWeekendDay()
    //-----------------------------------------------------------------------
    public void test_nonWeekendDay_serialization() throws IOException, ClassNotFoundException {
        DateMatcher nonWeekendDay = DateMatchers.nonWeekendDay();
        assertTrue(nonWeekendDay instanceof Serializable);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(nonWeekendDay);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
                baos.toByteArray()));
        assertSame(ois.readObject(), nonWeekendDay);
    }

    public void factory_nonWeekendDay() {
        assertNotNull(DateMatchers.nonWeekendDay());
        assertSame(DateMatchers.nonWeekendDay(), DateMatchers.nonWeekendDay());
    }

    public void test_nonWeekendDay() {
        for (MonthOfYear month : MonthOfYear.values()) {
            for (int i = 1; i <= month.lengthInDays(2007); i++) {
                LocalDate date = date(2007, month, i);
                DayOfWeek dayOfWeek = date.getDayOfWeek();
                assertEquals(DateMatchers.nonWeekendDay().matchesDate(date),
                        dayOfWeek != DayOfWeek.SATURDAY && 
                        dayOfWeek != DayOfWeek.SUNDAY);
            }
        }
    }

}