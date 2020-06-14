package com.gz;

public class StaticTest {
    public static void main(String[] args) {
//        TestA testA = TestA.getInstance();//先执行static，后执行new
//        TestA testA = new TestA();//先执行new，后执行static
        System.out.println(TestA.a);
        System.out.println(TestA.b);
    }
}
class TestA{
    public static int b ;
    public static TestA testA = new TestA();
    public static int a = 0;

    static {
        System.out.println("static");
        b=444;
    }

    public TestA(){
        System.out.println("new");
        a ++;
        b ++;
    }

    public static void test(){
        System.out.println("test");
    }

    public static TestA getInstance(){
        System.out.println("getInstance");
        return testA;
    }
}
