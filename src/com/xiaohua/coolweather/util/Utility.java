package com.xiaohua.coolweather.util;

import android.text.TextUtils;

import com.xiaohua.coolweather.model.City;
import com.xiaohua.coolweather.model.CoolWeatherDB;
import com.xiaohua.coolweather.model.County;
import com.xiaohua.coolweather.model.Province;

public class Utility {
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
