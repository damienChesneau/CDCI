package com.cdci.test;

import com.cdci.annotation.DistribuitedTest;

public class MyTest {

    @DistribuitedTest
    public void mytest(){
        System.out.println("Hello from mytest");
    }
}
