package io;

import ScheduleAkka.SlaveAkkaSystem;
import ScheduleAkka.StartAkkaSystem;

public class test {
    public static void main(String[] args){
        new StartAkkaSystem().start();
        new SlaveAkkaSystem().start();
    }
}
