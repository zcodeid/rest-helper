package id.zcode.rest;

import org.junit.Test;

public class HelperTest {

    @Test
    public void toDate(){
        org.junit.Assert.assertNotNull(Helper.toDate("2019-01-01"));
    }

    @Test
    public void toDateWithTime(){
        org.junit.Assert.assertNotNull(Helper.toDate("2019-01-01 12:02:00"));
    }

    @Test
    public void toDateInvalidString(){
        org.junit.Assert.assertNull(Helper.toDate("xxx"));
    }
}
