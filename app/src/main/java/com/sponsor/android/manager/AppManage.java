package com.sponsor.android.manager;

import android.content.Context;

import com.sponsor.android.constract.ui.activity.ActivityInter;

import java.util.Stack;

public class AppManage {

	private  static final AppManage instance = new AppManage();

	public static AppManage getInstance() {
		synchronized (instance){
			return  instance;
		}
	}

	private Stack<ActivityInter> activities;

	private AppManage() {
		activities = new Stack<ActivityInter>();
	}

	public void addActivity(ActivityInter activity) {
		if (activity != null) {
			activities.add(activity);
		}
	}
	

	public void removeActivity(ActivityInter activity) {
		if (activity != null) {
			activities.remove(activity);
		}
	}

	public void finishOther(){
		ActivityInter activity = getCurrentActivity();
		while (!activities.isEmpty()){
			ActivityInter act = activities.pop();
			if (act != activity && act != null){
				act.finishActivity();
			}
		}
		addActivity(activity);
	}

	public void finishActivity(ActivityInter activity) {
		if (activity != null) {
			activities.remove(activity);
		}
		activity.finishActivity();
	}

	public ActivityInter getCurrentActivity() {
		if (activities.size() > 0){
			return  activities.lastElement();
		}
		else {
			return null;
		}
	}
	

	@SuppressWarnings("deprecation")
	public void exit(Context context) {
		clearEverything();

		System.exit(0);
	}
	
	public void clearEverything(){
		while (!activities.isEmpty()){
			ActivityInter act = activities.pop();
			try {
				if (act != null){
					act.finishActivity();
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}

}
