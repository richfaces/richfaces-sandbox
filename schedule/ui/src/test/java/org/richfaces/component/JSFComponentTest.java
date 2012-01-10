/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.component;

import org.jboss.test.faces.AbstractFacesTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Unit test for simple Component.
 */
public class JSFComponentTest
    extends AbstractFacesTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Checks if component properly calculates date ranges of displayed days
     * in order to provide initial events.
     *
     * @throws IOException if file with test data cannot be read
     * @see JSFComponentTest#doTestDisplayedDays(java.lang.String, java.lang.String)
     */
    public void testDisplayedDaysForMonthViewCalculation() throws IOException {
        doTestDisplayedDays("displayedDaysForMonthViewCalculationTestData", AbstractSchedule.VIEW_MONTH);
    }

    /**
     * Checks if component properly calculates date ranges of displayed days
     * in order to provide initial events.
     *
     * @throws IOException if file with test data cannot be read
     * @see JSFComponentTest#doTestDisplayedDays(java.lang.String, java.lang.String)
     */
    public void testDisplayedDaysForWeekViewCalculation() throws IOException {
        doTestDisplayedDays("displayedDaysForWeekViewCalculationTestData", AbstractSchedule.VIEW_AGENDA_WEEK);
        doTestDisplayedDays("displayedDaysForWeekViewCalculationTestData", AbstractSchedule.VIEW_BASIC_WEEK);
    }

    /**
     * Checks if component properly calculates date ranges of displayed days
     * in order to provide initial events.
     *
     * @throws IOException if file with test data cannot be read
     * @see JSFComponentTest#doTestDisplayedDays(java.lang.String, java.lang.String)
     */
    public void testDisplayedDaysForDayViewCalculation() throws IOException {
        doTestDisplayedDays("displayedDaysForDayViewCalculationTestData", AbstractSchedule.VIEW_AGENDA_DAY);
        doTestDisplayedDays("displayedDaysForDayViewCalculationTestData", AbstractSchedule.VIEW_BASIC_DAY);
    }

    /**
     * Checks if component properly calculates date ranges of displayed days
     * in order to provide initial events.
     *
     * @param fileNameProperty name of file with test data
     * @param view             view name for which calculation should be tested
     * @throws IOException if file with test data cannot be read
     */
    private void doTestDisplayedDays(String fileNameProperty, String view) throws IOException {
        Calendar calendar = Calendar.getInstance();
        AbstractSchedule schedule = (AbstractSchedule) application.createComponent(AbstractSchedule.COMPONENT_TYPE);
        Calendar expectedFirstDisplayedDay, expectedLastDisplayedDay;

        final String fileName = System.getProperty(fileNameProperty);
        System.out.println("filename:" + fileName);
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        int lineNr = 0;
        int firstDay;
        boolean showWeekends;
        StringTokenizer tokenizer;
        try {
            while ((line = reader.readLine()) != null) {
                lineNr++;
                System.out.print("line " + lineNr + ": " + line);
                try {
                    tokenizer = new StringTokenizer(line, ",");
                    firstDay = Integer.parseInt(tokenizer.nextToken());
                    showWeekends = Integer.parseInt(tokenizer.nextToken()) == 1;
                    calendar.set(Calendar.YEAR, Integer.parseInt(tokenizer.nextToken()));
                    calendar.set(Calendar.MONTH, Integer.parseInt(tokenizer.nextToken()));
                    calendar.set(Calendar.DATE, Integer.parseInt(tokenizer.nextToken()));
                    Date initialDate = calendar.getTime();
                    calendar.set(Calendar.YEAR, Integer.parseInt(tokenizer.nextToken()));
                    calendar.set(Calendar.MONTH, Integer.parseInt(tokenizer.nextToken()));
                    calendar.set(Calendar.DATE, Integer.parseInt(tokenizer.nextToken()));
                    expectedFirstDisplayedDay = (Calendar) calendar.clone();
                    calendar.set(Calendar.YEAR, Integer.parseInt(tokenizer.nextToken()));
                    calendar.set(Calendar.MONTH, Integer.parseInt(tokenizer.nextToken()));
                    calendar.set(Calendar.DATE, Integer.parseInt(tokenizer.nextToken()));
                    expectedLastDisplayedDay = (Calendar) calendar.clone();
                    schedule.setShowWeekends(showWeekends);
                    schedule.setFirstDay(firstDay);
                    schedule.setDate(initialDate);
                    schedule.setView(view);
                    calendar.setTime(AbstractSchedule.getFirstDisplayedDay(schedule));
                    System.out.print(" result: " + calendar.get(Calendar.YEAR) + "," + calendar.get(Calendar.MONTH) + "," + calendar.get(Calendar.DATE));
                    assert expectedFirstDisplayedDay.get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
                    assert expectedFirstDisplayedDay.get(Calendar.MONTH) == calendar.get(Calendar.MONTH);
                    assert expectedFirstDisplayedDay.get(Calendar.DATE) == calendar.get(Calendar.DATE);
                    calendar.setTime(AbstractSchedule.getLastDisplayedDate(schedule));
                    System.out.print("," + calendar.get(Calendar.YEAR) + "," + calendar.get(Calendar.MONTH) + "," + calendar.get(Calendar.DATE));
                    assert expectedLastDisplayedDay.get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
                    assert expectedLastDisplayedDay.get(Calendar.MONTH) == calendar.get(Calendar.MONTH);
                    assert expectedLastDisplayedDay.get(Calendar.DATE) == calendar.get(Calendar.DATE);
                } catch (Exception e) {
                    throw new RuntimeException("Exception during processing of line " + lineNr + " of test data. Line: " + line, e);
                } finally {
                    System.out.println("");
                }
            }
        } finally {
            reader.close();

        }
    }
}