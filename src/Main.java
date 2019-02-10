package com.company;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args)throws InterruptedException {
        Process process = new Process(args);
        process.run();
        time(process.getArgument());
        Verify verify = new Verify();
        verify.verify(process.getArgument());

    }
    public  static  void  time(Argument argument)throws InterruptedException{
        Thread.sleep(TimeUnit.MINUTES.toMillis(argument.getIntervalTime()));

    }
}
