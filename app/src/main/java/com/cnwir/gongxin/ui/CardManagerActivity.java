package com.cnwir.gongxin.ui;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.adapter.CardManagerAdapter;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.bean.CardInfo;
import com.cnwir.gongxin.bean.CardInfo_1;
import com.cnwir.gongxin.bean.CardSortInfo;
import com.cnwir.gongxin.db.CardInfoDao;
import com.cnwir.gongxin.db.CardSortDao;
import com.cnwir.gongxin.db.ModuleDbDao;
import com.cnwir.gongxin.ui.login.LoginActivity;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.util.LogUtil;
import com.cnwir.mobeta.android.deslv.DragSortListView;

/**
 * 卡片管理
 * 
 * @author wangwm 2015年4月8日 下午1:47:47
 */
public class CardManagerActivity extends BaseActivity {

	private DragSortListView listview;
	private CardManagerAdapter adapter;
	private CardSortDao cardSortDao;

	private List<CardSortInfo> cardSortInfos;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.card_manager);
	}

	@Override
	protected void processLogic() {
		initData();
	}

	@Override
	protected void findViewById() {
		listview = (DragSortListView) findViewById(R.id.dragListView);
		findViewById(R.id.card_return).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Constant.ACTION_UPDATE_CARD_SORT);
				CardManagerActivity.this.sendBroadcast(intent);
				MyApplication.getInstance().cardSortIschange = true;
				mfinish();
			}
		});
	}

	private void initData() {
		cardSortDao = new CardSortDao(this);
		cardSortInfos = cardSortDao.findCardInfos();
		adapter = new CardManagerAdapter(this, cardSortInfos);
		listview.setAdapter(adapter);

		listview.setDropListener(onDrop);
		listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			if (from != to) {
				CardSortInfo item = adapter.getItem(from);
				adapter.remove(item);
				adapter.insert(item, to);

				List<CardSortInfo> temp_infos = cardSortDao.findCardInfos();
				CardSortInfo it = temp_infos.get(from);

				System.out.println(it.toString());
				temp_infos.remove(from);
				temp_infos.add(to, it);
				cardSortDao.saveCardInfos(temp_infos);
				listview.moveCheckState(from, to);

			}
		}
	};

}
