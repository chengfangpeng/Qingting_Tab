package com.cnwir.gongxin.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.cnwir.gongxin.bean.CollectApp;
import com.cnwir.gongxin.bean.CreateApp;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

public class CreateAppDao {

	private Context context;
	private DbUtils db;

	public CreateAppDao(Context context) {

		this.context = context;
		db = DbHelper.getDbUtils(context);
	}

	public void saveCreateApp(CreateApp userApp) {

	}

	public void deleteCreateApp(CreateApp userApp) {
		
		try {
			db.deleteById(CreateApp.class, userApp.getId());
		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	public CreateApp getCreateApp() {

		return null;
	}

	public void saveCreateApps(Map<Integer, CreateApp> createApps) {

		deleteCreateApps(createApps);

		for (Integer key : createApps.keySet()) {

			try {
				CollectApp app = db.findById(CollectApp.class, key);
				if (app == null) {
					db.save(createApps.get(key));
				}

			} catch (DbException e) {
				e.printStackTrace();
			}

		}

	}

	public void deleteCreateApps(Map<Integer, CreateApp> apps) {

		try {
			if (apps != null)
				db.deleteAll(CreateApp.class);
		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	public Map<Integer, CreateApp> findCreatetApps() {
		Map<Integer, CreateApp> apps = new HashMap<Integer, CreateApp>();
		try {

			List<CreateApp> appList = db.findAll(CreateApp.class);
			for (int i = 0; i < appList.size(); i++) {

				apps.put(appList.get(i).getId(), appList.get(i));
			}

		} catch (DbException e) {
			e.printStackTrace();
		} finally {

			return apps;
		}

	}

}
