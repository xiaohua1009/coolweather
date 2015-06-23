package com.xiaohua.coolweather.model;

import java.util.ArrayList;
import java.util.List;

import com.xiaohua.coolweather.db.CoolWeatherOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
	
	//���ݿ������
	public static final String DB_NAME = "cool_weather";

	//���ݿ�İ汾��
	public static final int VERSION = 1;
	private static CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase db;

	public CoolWeatherDB(Context context) {
		super();
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * ��Provinceʵ���洢�����ݿ���
	 */
	
	public void saveProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("province", null, values);
		}
	}
	
	/**
	 * ��ȡCoolWeatherDB
	 */
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB == null){
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	/**
	 * �����ݿ���ȡȫ������ʡ�ݵ���Ϣ
	 */
	
	public List<Province> loadProvince(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("provinceName")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("provinceCode")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
	
	/**
	 * ��cityʵ���洢�����ݿ���
	 */
	public void saveCity(City city){
		if(city != null){
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("city", null, values);
		}
	}
	
	/**
	 * �����ݿ���ȡĳʡ�����г��е���Ϣ
	 */
	public List<City> loadCity(int provinceId){
		List<City> list = new ArrayList();
		Cursor cursor = db.query("city", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
	
	/**
	 * ��country��Ϣ���浽���ݿ���
	 */
	
	public void saveCountry(Country country){
		ContentValues values = new ContentValues();
		values.put("cuntry_name", country.getCountryName());
		values.put("country_code", country.getCountryCode());
		values.put("city_id", country.getCityId());
		db.insert("country", null, values);
	}
	
	/**
	 * �����ݿ���ȡ��ĳ���������е�������Ϣ
	 */
	public List<Country> loadCountry(int cityId){
		List<Country> list = new ArrayList();
		Cursor cursor = db.query("country", null, "cityId = ?", new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Country country = new Country();
				country.setCityId(cursor.getInt(cursor.getColumnIndex("id")));
				country.setCountryName(cursor.getString(cursor.getColumnIndex("country_city")));
				country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
				country.setCityId(cityId);
				list.add(country);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
}
