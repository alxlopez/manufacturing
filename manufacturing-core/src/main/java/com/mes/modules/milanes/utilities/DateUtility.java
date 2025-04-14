package com.mes.modules.milanes.utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateUtility {
	private int year;
	private int easterMonth;
	private int easterDay;
	private ArrayList<String> holidays;

	public DateUtility(int year) {
		this.year = year;
		this.holidays = new ArrayList<String>();
		int a = year % 19;
	    int b = year / 100;
	    int c = year % 100;
	    int d = b / 4;
	    int e = b % 4;
	    int g = (8 * b + 13) / 25;
	    int h = (19 * a + b - d - g + 15) % 30;
	    int j = c / 4;
	    int k = c % 4;
	    int m = (a + 11 * h) / 319;
	    int r = (2 * e + 2 * j - k - h + m + 32) % 7;
	    this.easterMonth = (h - m + r + 90) / 25;
	    this.easterDay = (h - m + r + this.easterMonth + 19) % 32;
	    this.easterMonth--;
	    this.holidays.add("0:1");// Primero de Enero
	    this.holidays.add("4:1");// Dia del trabajo 1 de mayo
	    this.holidays.add("6:20");//Independencia 20 de Julio
	    this.holidays.add("7:7");//Batalla de boyaca 7 de agosto
	    this.holidays.add("11:8");//Maria inmaculada 8 de diciembre
	    this.holidays.add("11:25");//Navidad 25 de diciembre
	    System.out.println(this.easterDay);
	    this.calculateEmiliani(0, 6);// Reyes magos 6 de enero
	    this.calculateEmiliani(2, 19);//San jose 19 de marzo
	    this.calculateEmiliani(5, 29);//San pedro y san pablo 29 de junio
	    this.calculateEmiliani(7, 15);//Asuncion 15 de agosto
	    this.calculateEmiliani(9, 12);//Descubrimiento de america 12 de octubre
	    this.calculateEmiliani(10, 1);//Todos los santos 1 de noviembre
	    this.calculateEmiliani(10, 11);//Independencia de cartagena 11 de noviembre
	    this.calculateOtherHoliday(-3, false);//jueves santos
	    this.calculateOtherHoliday(-2, false);//viernes santo
	    //this.calculateOtherHoliday(36, true);//Asención del señor de pascua
	    this.calculateOtherHoliday(43, true);//Asención del señor de pascua
	    this.calculateOtherHoliday(60, true);//Corpus cristi
	    this.calculateOtherHoliday(68, true);//Sagrado corazon
	}

	private void calculateEmiliani(int month, int day) {
		Calendar date = Calendar.getInstance();
		date.set(this.year, month, day);
		int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
		switch (dayOfWeek) {
	        case 1:
		        date.add(Calendar.DATE, 1);
		        break;
	        case 3:
	        	date.add(Calendar.DATE, 6);
		        break;
	        case 4:
	        	date.add(Calendar.DATE, 5);
		        break;
	        case 5:
	        	date.add(Calendar.DATE, 4);
		        break;
	        case 6:
	        	date.add(Calendar.DATE, 3);
		        break;
	        case 7:
	        	date.add(Calendar.DATE, 2);
		        break;
		}
		this.holidays.add(date.get(Calendar.MONTH) + ":" + date.get(Calendar.DATE));
	}

	private void calculateOtherHoliday(int days, boolean emiliani) {
		Calendar date = Calendar.getInstance();
		date.set(this.year, this.easterMonth, this.easterDay);
		date.add(Calendar.DATE, days);
        if (emiliani) {
            this.calculateEmiliani(date.get(Calendar.MONTH), date.get(Calendar.DATE));
        } else {
        	this.holidays.add(date.get(Calendar.MONTH) + ":" + date.get(Calendar.DATE));
        }
	}

	public boolean isHoliday(int month, int day) {
		return this.holidays.contains(month + ":" +day);
	}

	public int getYear() {
		return year;
	}

	public static Date getNextDayEnabled(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		DateUtility dateUtil = new DateUtility(calendar.get(Calendar.YEAR));
		while(days > 0) {
			calendar.add(Calendar.DATE, 1);
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			//if (dayOfWeek != 1 && dayOfWeek != 7 && !dateUtil.isHoliday(calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))) {
			if (dayOfWeek != 1 && !dateUtil.isHoliday(calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))) {
				days--;
			}
		}
		return calendar.getTime();
	}

	public static Date getEnableDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		DateUtility dateUtil = new DateUtility(calendar.get(Calendar.YEAR));
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		//while(dayOfWeek == 1 || dayOfWeek == 7 || dateUtil.isHoliday(calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))) {
		while(dayOfWeek == 1 || dateUtil.isHoliday(calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))) {
			calendar.add(Calendar.DATE, 1);
			dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		}
		return calendar.getTime();
	}
}
