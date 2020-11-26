package cn.edu.seu.xzp.movie.object;

import java.util.Date;
import java.util.Calendar;

public class Movie {

    private String name;
    private String origin_name;
    private Date date;
    private String type;
    private String address;
    private double rating;
    private String douban_url;
    private String introduction;



    private String series;
    private Calendar calendar = Calendar.getInstance();
    public Movie() {

    }



    public String toString() {
        return String.format("原名：%s\n上映日期：%tF\n类型：%s\n系列：%s\n豆瓣评分：%.1f\n位置：\n        %s\n简介：\n        %s",origin_name,date,type,series,rating,address,introduction);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public void setOrigin_name(String origin_name) {
        this.origin_name = origin_name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        this.calendar.setTime(date);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDouban_url() {
        return douban_url;
    }

    public void setDouban_url(String douban_url) {
        this.douban_url = douban_url;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
