package com.cnwir.gongxin.db;

import java.util.List;

import com.cnwir.gongxin.bean.CardInfo_1;
import com.cnwir.gongxin.bean.CardSortInfo;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import android.content.Context;

public class CardSortDao {
	
	private Context context;
	
	private DbUtils db;
	
	public CardSortDao(Context context) {

		this.context = context;
		db = DbHelper.getDbUtils(context);
	}
	
	public void updateCardSort(CardSortInfo info){
		try {
			if (info != null) {
				CardSortInfo findInfo = db.findById(CardSortInfo.class, info.getId());
				if (findInfo != null) {
					db.deleteById(CardSortInfo.class, info.getId());
					db.save(info);

				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		}

		
	}
	
	public void saveCardInfos(List<CardSortInfo> datas) {

		try {
			db.deleteAll(CardSortInfo.class);
			
			db.saveAll(datas);
		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	public List<CardSortInfo> findCardInfos() {
		List<CardSortInfo> datas = null;
		try {
			datas = db.findAll(CardSortInfo.class);
		} catch (DbException e) {
			e.printStackTrace();
		} finally {

			return datas;
		}

	}

	public void deleteCardInfos() {

		try {
			db.deleteAll(CardSortInfo.class);
		} catch (DbException e) {
			e.printStackTrace();
		}

	}
}
