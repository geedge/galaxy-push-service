package com.nis.galaxy.domain;

/**
 * @author GouGe
 * @data 2018/12/17 15:42
 */
@SuppressWarnings("serial")
public class EventsNameNumTime {
    private String name;
    private long num;
    private String time;

    public EventsNameNumTime(){
        super();
    }

    public EventsNameNumTime(String name, long num, String time) {
        this.name = name;
        this.num = num;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
