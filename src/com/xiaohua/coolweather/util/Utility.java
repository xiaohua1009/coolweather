package com.xiaohua.coolweather.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.xiaohua.coolweather.model.City;
import com.xiaohua.coolweather.model.CoolWeatherDB;
import com.xiaohua.coolweather.model.County;
import com.xiaohua.coolweather.model.Province;

public class Utility {
	
	/**
	 * 解析服务器返回的JSON数据，并将解析出的数据存储到本地
	 */
	public static void handleWeatherResponse(Context context,String response){
		try {
			//System.out.println(response);
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 将服务器返回的所欲偶天气信息存储到sharedPreferences文件
	 * @param context
	 * @param cityName
	 * @param weatherCode
	 * @param temp1
	 * @param temp2
	 * @param weatherDesp
	 * @param publishTime
	 */
	public static void saveWeatherInfo(Context context,String cityName,String weatherCode,String temp1,String temp2,
			String weatherDesp,String publishTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2",temp2);
		editor.putString("weather_desp",weatherDesp);
		editor.putString("publish_time",publishTime);
		editor.putString("current_date",sdf.format(new Date()));
		editor.commit();
	}
	/**
	 * 解析和处理服务器返回的生机数据
	 */
	
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
		
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if(allProvinces != null && allProvinces.length>0){
				for(String p : allProvinces){
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//将解析出来的数据存储到Province表
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 解析和处理服务器返回的市级数据
	 */
	
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities = response.split(",");
			if(allCities != null && allCities.length>0){
				for(String c:allCities){
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 解析和处理服务器返回的县级数据
	 */
	public static boolean handleCountriesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCountries = response.split(",");
			if(allCountries != null && allCountries.length>0){
				for(String c:allCountries){
					String[] array = c.split("\\|");
					County County = new County();
					County.setCountyCode(array[0]);
					County.setCountyName(array[1]);
					County.setCityId(cityId);
					coolWeatherDB.saveCounty(County);
				}
				return true;
			}
		}
		return false;
	}
	

}
