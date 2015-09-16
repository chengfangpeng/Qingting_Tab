package com.cnwir.gongxin.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.cnwir.gongxin.bean.CollectApp;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

public class CollectDao {
	private Context context;
	private DbUtils db;

	public CollectDao(Context context) {

		this.context = context;
		db = DbHelper.getDbUtils(context);
	}

	public void saveCollectApps(Map<Integer, CollectApp> apps) {
		
		deleteCollectApps(apps);

		for (Integer key : apps.keySet()) {

			try {
				CollectApp app = db.findById(CollectApp.class, key);
				if (app == null) {
					db.save(apps.get(key));
				}

			} catch (DbException e) {
				e.printStackTrace();
			}

		}

	}


	@SuppressWarnings("finally")
	public Map<Integer, CollectApp> findCollectApps() {
		Map<Integer, CollectApp> apps = new HashMap<Integer, CollectApp>();
		try {

			List<CollectApp> appList = db.findAll(CollectApp.class);
			for (int i = 0; i < appList.size(); i++) {

				apps.put(appList.get(i).getId(), appList.get(i));
			}

		} catch (DbException e) {
			e.printStackTrace();
		} finally {

			return apps;
		}

	}

	public void deleteCollectApps(Map<Integer, CollectApp> apps) {

		try {
			if (apps != null) {
				db.deleteAll(CollectApp.class);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}

	}
}
