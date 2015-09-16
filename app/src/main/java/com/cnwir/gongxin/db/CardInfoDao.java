package com.cnwir.gongxin.db;

import java.util.List;

import android.content.Context;

import com.cnwir.gongxin.bean.CardInfo_1;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

public class CardInfoDao {

	private Context context;
	private DbUtils db;

	public CardInfoDao(Context context) {

		this.context = context;
		db = DbHelper.getDbUtils(context);
	}

	public void updateCardInfo(CardInfo_1 info) {

		try {
			if (info != null) {
				CardInfo_1 findInfo = db.findById(CardInfo_1.class, info.getId());
				if (findInfo != null) {
					db.deleteById(CardInfo_1.class, info.getId());
					db.save(info);

				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	public void saveCardInfos(List<CardInfo_1> datas) {

		try {
			db.deleteAll(CardInfo_1.class);
			
			db.saveAll(datas);
		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	public List<CardInfo_1> findCardInfos() {
		List<CardInfo_1> datas = null;
		try {
			datas = db.findAll(CardInfo_1.class);
		} catch (DbException e) {
			e.printStackTrace();
		} finally {

			return datas;
		}

	}

	public void deleteCardInfos() {

		try {
			db.deleteAll(CardInfo_1.class);
		} catch (DbException e) {
			e.printStackTrace();
		}

	}

}
