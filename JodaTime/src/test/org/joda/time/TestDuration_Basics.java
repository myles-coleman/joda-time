/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2004 Stephen Colebourne.  
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally 
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
 */
package org.joda.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This class is a Junit unit test for DateTime.
 *
 * @author Stephen Colebourne
 */
public class TestDuration_Basics extends TestCase {
    // Test in 2002/03 as time zones are more well known
    // (before the late 90's they were all over the place)

    private static final DateTimeZone PARIS = DateTimeZone.getInstance("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.getInstance("Europe/London");
    
    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365;
    long y2003days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365 + 365;
    
    // 2002-06-09
    private long TEST_TIME_NOW =
            (y2002days + 31L + 28L + 31L + 30L + 31L + 9L -1L) * DateTimeConstants.MILLIS_PER_DAY;
            
    // 2002-04-05
    private long TEST_TIME1 =
            (y2002days + 31L + 28L + 31L + 5L -1L) * DateTimeConstants.MILLIS_PER_DAY
            + 12L * DateTimeConstants.MILLIS_PER_HOUR
            + 24L * DateTimeConstants.MILLIS_PER_MINUTE;
        
    // 2003-05-06
    private long TEST_TIME2 =
            (y2003days + 31L + 28L + 31L + 30L + 6L -1L) * DateTimeConstants.MILLIS_PER_DAY
            + 14L * DateTimeConstants.MILLIS_PER_HOUR
            + 28L * DateTimeConstants.MILLIS_PER_MINUTE;
    
    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestDuration_Basics.class);
    }

    public TestDuration_Basics(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    public void testTest() {
        assertEquals("2002-06-09T00:00:00.000Z", new Instant(TEST_TIME_NOW).toString());
        assertEquals("2002-04-05T12:24:00.000Z", new Instant(TEST_TIME1).toString());
        assertEquals("2003-05-06T14:28:00.000Z", new Instant(TEST_TIME2).toString());
    }

    //-----------------------------------------------------------------------
    public void testGetDurationType() {
        Duration test = new Duration();
        assertEquals(DurationType.getMillisType(), test.getDurationType());
    }

    public void testGetTotalMillis() {
        Duration test = new Duration(123L);
        assertEquals(123L, test.getTotalMillis());
    }

    public void testGetIsPrecise() {
        Duration test = new Duration(123L);
        assertEquals(true, test.isPrecise());
        test = new Duration(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(false, test.isPrecise());
    }

    public void testGetMethods() {
        Duration test = new Duration();
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getDays());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(0, test.getMillis());
    }

    public void testEqualsHashCode() {
        Duration test1 = new Duration(123L);
        Duration test2 = new Duration(123L);
        assertEquals(true, test1.equals(test2));
        assertEquals(true, test2.equals(test1));
        assertEquals(true, test1.equals(test1));
        assertEquals(true, test2.equals(test2));
        assertEquals(true, test1.hashCode() == test2.hashCode());
        assertEquals(true, test1.hashCode() == test1.hashCode());
        assertEquals(true, test2.hashCode() == test2.hashCode());
        
        Duration test3 = new Duration(321L);
        assertEquals(false, test1.equals(test3));
        assertEquals(false, test2.equals(test3));
        assertEquals(false, test3.equals(test1));
        assertEquals(false, test3.equals(test2));
        assertEquals(false, test1.hashCode() == test3.hashCode());
        assertEquals(false, test2.hashCode() == test3.hashCode());
        
        assertEquals(false, test1.equals("Hello"));
        assertEquals(true, test1.equals(new MockDuration(123L)));
        assertEquals(false, test1.equals(new Duration(123L, DurationType.getAverageYearMonthType())));
    }
    
    class MockDuration extends AbstractDuration {
        public MockDuration(long value) {
            super(value, null);
        }
    }

    public void testCompareTo() {
        Duration test1 = new Duration(123L);
        Duration test1a = new Duration(123L);
        assertEquals(0, test1.compareTo(test1a));
        assertEquals(0, test1a.compareTo(test1));
        assertEquals(0, test1.compareTo(test1));
        assertEquals(0, test1a.compareTo(test1a));
        
        Duration test2 = new Duration(321L);
        assertEquals(-1, test1.compareTo(test2));
        assertEquals(+1, test2.compareTo(test1));
        
        Duration test3 = new Duration(321L, DurationType.getAllType());
        assertEquals(-1, test1.compareTo(test3));
        assertEquals(+1, test3.compareTo(test1));
        assertEquals(0, test3.compareTo(test2));
        
        assertEquals(+1, test2.compareTo(new MockDuration(123L)));
        assertEquals(0, test1.compareTo(new MockDuration(123L)));
        
        try {
            test1.compareTo(null);
            fail();
        } catch (NullPointerException ex) {}
        try {
            test1.compareTo(new Long(123L));
            fail();
        } catch (ClassCastException ex) {}
        try {
            test1.compareTo(new Duration(1, 2, 3, 4, 5, 6, 7, 8));
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testIsEqual() {
        Duration test1 = new Duration(123L);
        Duration test1a = new Duration(123L);
        assertEquals(true, test1.isEqual(test1a));
        assertEquals(true, test1a.isEqual(test1));
        assertEquals(true, test1.isEqual(test1));
        assertEquals(true, test1a.isEqual(test1a));
        
        Duration test2 = new Duration(321L);
        assertEquals(false, test1.isEqual(test2));
        assertEquals(false, test2.isEqual(test1));
        
        Duration test3 = new Duration(321L, DurationType.getAllType());
        assertEquals(false, test1.isEqual(test3));
        assertEquals(false, test3.isEqual(test1));
        assertEquals(true, test3.isEqual(test2));
        
        assertEquals(false, test2.isEqual(new MockDuration(123L)));
        assertEquals(true, test1.isEqual(new MockDuration(123L)));
        assertEquals(false, test1.isEqual(null));
        assertEquals(true, new Duration(0L).isEqual(null));
    }
    
    public void testIsBefore() {
        Duration test1 = new Duration(123L);
        Duration test1a = new Duration(123L);
        assertEquals(false, test1.isShorterThan(test1a));
        assertEquals(false, test1a.isShorterThan(test1));
        assertEquals(false, test1.isShorterThan(test1));
        assertEquals(false, test1a.isShorterThan(test1a));
        
        Duration test2 = new Duration(321L);
        assertEquals(true, test1.isShorterThan(test2));
        assertEquals(false, test2.isShorterThan(test1));
        
        Duration test3 = new Duration(321L, DurationType.getAllType());
        assertEquals(true, test1.isShorterThan(test3));
        assertEquals(false, test3.isShorterThan(test1));
        assertEquals(false, test3.isShorterThan(test2));
        
        assertEquals(false, test2.isShorterThan(new MockDuration(123L)));
        assertEquals(false, test1.isShorterThan(new MockDuration(123L)));
        assertEquals(false, test1.isShorterThan(null));
        assertEquals(false, new Duration(0L).isShorterThan(null));
    }
    
    public void testIsAfter() {
        Duration test1 = new Duration(123L);
        Duration test1a = new Duration(123L);
        assertEquals(false, test1.isLongerThan(test1a));
        assertEquals(false, test1a.isLongerThan(test1));
        assertEquals(false, test1.isLongerThan(test1));
        assertEquals(false, test1a.isLongerThan(test1a));
        
        Duration test2 = new Duration(321L);
        assertEquals(false, test1.isLongerThan(test2));
        assertEquals(true, test2.isLongerThan(test1));
        
        Duration test3 = new Duration(321L, DurationType.getAllType());
        assertEquals(false, test1.isLongerThan(test3));
        assertEquals(true, test3.isLongerThan(test1));
        assertEquals(false, test3.isLongerThan(test2));
        
        assertEquals(true, test2.isLongerThan(new MockDuration(123L)));
        assertEquals(false, test1.isLongerThan(new MockDuration(123L)));
        assertEquals(true, test1.isLongerThan(null));
        assertEquals(false, new Duration(0L).isLongerThan(null));
    }
    
    //-----------------------------------------------------------------------
    public void testSerialization() throws Exception {
        Duration test = new Duration(123L);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Duration result = (Duration) ois.readObject();
        ois.close();
        
        assertEquals(test, result);
    }

    //-----------------------------------------------------------------------
    // TODO: Adds
    
    //-----------------------------------------------------------------------
    public void testToString() {
        Duration test = new Duration(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals("P1Y2M3W4DT5H6M7.008S", test.toString());
        
        test = new Duration(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("PT0S", test.toString());
        
        test = new Duration(12345L);
        assertEquals("PT12.345S", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testToDuration() {
        Duration test = new Duration(123L);
        Duration result = test.toDuration();
        assertSame(test, result);
    }

    public void testToMutableDuration() {
        Duration test = new Duration(123L);
        MutableDuration result = test.toMutableDuration();
        assertEquals(test.getDurationType(), result.getDurationType());
        assertEquals(test.isPrecise(), result.isPrecise());
        assertEquals(test.getTotalMillis(), result.getTotalMillis());
    }

    //-----------------------------------------------------------------------
    public void testConstant() {
        assertEquals(0L, Duration.ZERO.getTotalMillis());
        assertEquals(DurationType.getMillisType(), Duration.ZERO.getDurationType());
    }

    //-----------------------------------------------------------------------
    public void testImmutable() {
        MockChangeDuration test = new MockChangeDuration(111L);
        test.testSetDuration_RD();
        assertEquals(111L, test.getTotalMillis());
        
        test = new MockChangeDuration(111L);
        test.testSetDuration_ints();
        assertEquals(111L, test.getTotalMillis());
        
        test = new MockChangeDuration(111L);
        test.testSetTotalMillis_1();
        assertEquals(111L, test.getTotalMillis());
        
        test = new MockChangeDuration(111L);
        test.testSetTotalMillis_2();
        assertEquals(111L, test.getTotalMillis());
        
        test = new MockChangeDuration(111L);
        test.testSetYears();
        assertEquals(111L, test.getTotalMillis());
        
        test = new MockChangeDuration(111L);
        test.testSetMonths();
        assertEquals(111L, test.getTotalMillis());
        
        test = new MockChangeDuration(111L);
        test.testSetWeeks();
        assertEquals(111L, test.getTotalMillis());
        
        test = new MockChangeDuration(111L);
        test.testSetDays();
        assertEquals(111L, test.getTotalMillis());
        
        test = new MockChangeDuration(111L);
        test.testSetHours();
        assertEquals(111L, test.getTotalMillis());
        
        test = new MockChangeDuration(111L);
        test.testSetMinutes();
        assertEquals(111L, test.getTotalMillis());
        
        test = new MockChangeDuration(111L);
        test.testSetSeconds();
        assertEquals(111L, test.getTotalMillis());
        
        test = new MockChangeDuration(111L);
        test.testSetMillis();
        assertEquals(111L, test.getTotalMillis());
    }
    
    static class MockChangeDuration extends Duration {
        MockChangeDuration(long duration) {
            super(duration);
        }
        public void testSetDuration_RD() {
            setDuration(null);
        }
        public void testSetDuration_ints() {
            setDuration(1, 2, 3, 4, 5, 6, 7, 8);
        }
        public void testSetTotalMillis_1() {
            setTotalMillis(123L);
        }
        public void testSetTotalMillis_2() {
            setTotalMillis(123L, 321L);
        }
        public void testSetYears() {
            setYears(1);
        }
        public void testSetMonths() {
            setMonths(1);
        }
        public void testSetWeeks() {
            setWeeks(1);
        }
        public void testSetDays() {
            setDays(1);
        }
        public void testSetHours() {
            setHours(1);
        }
        public void testSetMinutes() {
            setMinutes(1);
        }
        public void testSetSeconds() {
            setSeconds(1);
        }
        public void testSetMillis() {
            setMillis(1);
        }
    }

}