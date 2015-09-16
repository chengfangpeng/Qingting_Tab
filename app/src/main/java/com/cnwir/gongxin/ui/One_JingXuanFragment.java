package com.cnwir.gongxin.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.cnwir.mycache.ConfigCacheUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.adapter.OneJingXuan_Module1_Adapter;
import com.cnwir.gongxin.adapter.OneJingXuan_Module2_Adapter;
import com.cnwir.gongxin.adapter.OneJingXuan_Module3_Adapter;
import com.cnwir.gongxin.adapter.OneJingXuan_Module6_Adapter;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.bean.CardSortInfo;
import com.cnwir.gongxin.bean.ImgInfo;
import com.cnwir.gongxin.bean.QAppInfo;
import com.cnwir.gongxin.db.CardSortDao;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.util.AddShortCutUtils;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.util.TimeUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

/**
 * 精选
 * 
 * @author wangwm 2015年4月3日 上午9:59:59
 */
@SuppressLint("InflateParams")
public class One_JingXuanFragment extends Fragment implements OnClickListener {

	private static final String CARDSORTUPDATETIME = "card_sort_update_time";

	public View view, view_weather;

	private LayoutInflater inflater;
	private LinearLayout container;
	private Context context;

	public static String youliao, hotvideo, haibao, book, joke, ladyimg, yulu;

	private List<CardSortInfo> cardList;

	private CardSortDao cardSortDao;
	private String[] cardInfoStrs;

	private int[] module_indexs;
	private QAppInfo[] module_infos;
	private OneJingXuan_Module1_Adapter module1_adapter;
	private OneJingXuan_Module2_Adapter module2_adapter;
	private OneJingXuan_Module3_Adapter module3_Adapter;
	private OneJingXuan_Module6_Adapter module6_Adapter;

	private SortedCardNotice receiver;
	private BitmapUtils bitmapUtils;

	private ViewPager mBanner;

	private BannerAdapter mBannerAdapter;

	private ImageView[] mIndicators;

	private Timer mTimer = new Timer();

	private int mBannerPosition = 0;
	private final int FAKE_BANNER_SIZE = 100;
	private boolean mIsUserTouched = false;

	private List<ImgInfo> imgInfos;

	private Activity mActivity;

	class SortedCardNotice extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constant.ACTION_UPDATE_CARD_SORT.equals(action)) {// 更新卡片顺序
				container.removeAllViews();
				initContent();
			}
		}

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		receiver = new SortedCardNotice();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_UPDATE_CARD_SORT);
		getActivity().registerReceiver(receiver, filter);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.context = getActivity().getApplicationContext();
		this.inflater = inflater;
		bitmapUtils = new BitmapUtils(context);
		if (view == null) {
			view = inflater.inflate(R.layout.one_jingxuan, null);
			this.container = (LinearLayout) view.findViewById(R.id.one_jingxuan_content_ll);
			view.findViewById(R.id.manage_card).setOnClickListener(this);
		}

		youliao = getText(R.string.key_youliao).toString();
		hotvideo = getText(R.string.key_rebo).toString();
		joke = getText(R.string.key_douniyixiao).toString();
		book = getText(R.string.key_xiaoshuo_book).toString();
		ladyimg = getText(R.string.key_zhainan_fuli).toString();
		yulu = getText(R.string.key_yulu).toString();

		cardInfoStrs = getResources().getStringArray(R.array.cardinfos);

		initBanner(view);
		initContent();

		System.out.println("jingxuan time =" + System.currentTimeMillis());

		return view;
	}

	private void initBanner(View parentView) {
		mIndicators = new ImageView[] { (ImageView) parentView.findViewById(R.id.indicator1),
				(ImageView) parentView.findViewById(R.id.indicator2),
				(ImageView) parentView.findViewById(R.id.indicator3),
				(ImageView) parentView.findViewById(R.id.indicator4),
				(ImageView) parentView.findViewById(R.id.indicator5) };
		mBanner = (ViewPager) parentView.findViewById(R.id.banner);
		String result = ConfigCacheUtil.getUrlCache(RequestURL.getJingXuanBanner("jingxuan"));
		if (TextUtils.isEmpty(result)) {

			getImages();
		} else {
			updateBanner(result.toString());
		}

	}

	private void getImages() {

		JsonObjectRequest request = new JsonObjectRequest(RequestURL.getJingXuanBanner("jingxuan"), null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						updateBanner(response.toString());

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});

		ClientTask.getInstance(mActivity).addToRequestQueue(request);

	}

	private void updateBanner(String result) {

		JSONObject o;
		try {
			o = new JSONObject(result.toString());
			if (o.has("error"))
				if (o.getInt("error") != 0)
					return;
			ConfigCacheUtil.setUrlCache(result.toString(), RequestURL.getJingXuanBanner("jingxuan"));
			JSONArray array = o.getJSONObject("data").getJSONArray("d");
			Gson gson = new Gson();
			imgInfos = new ArrayList<ImgInfo>();
			for (int i = 0; i < array.length(); i++) {
				ImgInfo imgInfo = new ImgInfo();
				imgInfo = gson.fromJson(array.getString(i).toString(), ImgInfo.class);
				imgInfos.add(imgInfo);

			}
			mBannerAdapter = new BannerAdapter(mActivity);
			mBanner.setAdapter(mBannerAdapter);
			mBanner.setOnPageChangeListener(mBannerAdapter);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onResume() {
		super.onResume();

		if (MyApplication.getInstance().cardSortIschange) {
			changeView();
		}
	}

	private void changeView() {
		MyApplication.getInstance().cardSortIschange = false;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	/**
	 * 初始化每个模块
	 * 
	 * @author wangwm 2015年4月9日 上午11:26:24
	 */
	@SuppressLint("InflateParams")
	private void initContent() {

		JsonObjectRequest request = new JsonObjectRequest(RequestURL.getCardManagerInfo(), null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response != null) {
								JSONObject o = new JSONObject(response.toString());
								if (o.getInt("error") == 1) {
									Toast.makeText(getActivity(), R.string.request_error, Toast.LENGTH_SHORT).show();
									return;
								}
								JSONObject object = o.getJSONObject("data");
								String time = object.getString("createTime");
								long timeL = TimeUtils.stringToDateToLong(time);
								SharedPreferences sp = getActivity().getSharedPreferences(CARDSORTUPDATETIME,
										Context.MODE_PRIVATE);
								long spTime = sp.getLong(CARDSORTUPDATETIME, 0);
								if (timeL > spTime) {
									JSONArray jsonArray = object.getJSONArray("list");
									Gson gson = new Gson();
									CardSortDao dao = new CardSortDao(getActivity());
									List<CardSortInfo> cardSortInfo = new ArrayList<CardSortInfo>();
									for (int i = 0; i < jsonArray.length(); i++) {
										CardSortInfo info = new CardSortInfo();
										info = gson.fromJson(jsonArray.get(i).toString(), CardSortInfo.class);
										cardSortInfo.add(info);

									}
									dao.deleteCardInfos();
									dao.saveCardInfos(cardSortInfo);
									Editor editor = sp.edit();
									editor.putLong(CARDSORTUPDATETIME, timeL);
									editor.commit();

								}
								getCardInfo();

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});
		ClientTask.getInstance(getActivity()).addToRequestQueue(request);

	}

	private void getCardInfo() {

		StringBuilder sb = new StringBuilder();
		cardSortDao = new CardSortDao(getActivity());
		cardList = cardSortDao.findCardInfos();
		for (int i = 0; i < cardList.size(); i++) {

			if (i != cardList.size() - 1) {
				sb.append(cardList.get(i).getAlias());
				sb.append(",");
			} else {
				sb.append(cardList.get(i).getAlias());
			}
		}

		JsonObjectRequest request = new JsonObjectRequest(RequestURL.getJingXuanCardInfo(sb.toString()), null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response != null) {
								JSONObject jsonObj_1 = response.optJSONObject("data");
								Gson gson = new Gson();
								module_indexs = new int[cardInfoStrs.length];
								module_infos = new QAppInfo[cardInfoStrs.length];
								for (int i = 0; i < cardList.size(); i++) {
									JSONObject o = jsonObj_1.getJSONObject(cardList.get(i).getAlias());
									module_infos[i] = gson.fromJson(o.toString(), QAppInfo.class);
									switch (module_infos[i].getType()) {
									case 1:
										View view_module1 = inflater.inflate(R.layout.one_jingxuan_model_1, null);
										ListView module1_lv = (ListView) view_module1.findViewById(R.id.module_1_lv);
										module1_lv.setTag(i);
										module1_lv.setOnItemClickListener(new OnItemClickListener() {

											@Override
											public void onItemClick(AdapterView<?> parent, View view, int position,
													long id) {
												int index = (Integer) parent.getTag();
												toDetailQAppActivity(module_infos[index].getList().get(position));

											}
										});
										TextView title = (TextView) view_module1.findViewById(R.id.module_1_title);
										title.setText(module_infos[i].getTitle());
										View module1_change = view_module1.findViewById(R.id.module_1_change);
										module1_change.setTag(i);
										module1_change.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												int index = (Integer) v.getTag();

												if (module_indexs[index] + 5 < module_infos[index].getList().size()) {
													module_indexs[index] += 5;

												} else {
													module_indexs[index] = 0;
												}
												module1_adapter.updateData(module_infos[index].getList(),
														module_indexs[index]);

											}
										});
										View module1_add = view_module1.findViewById(R.id.module_1_add);
										module1_add.setTag(i);
										module1_add.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												int index = (Integer) v.getTag();
												AddShortCutUtils.addShortCut(module_infos[index], getActivity());

											}
										});
										View module1_more = view_module1.findViewById(R.id.module_1_more);
										module1_more.setTag(i);
										module1_more.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {

												int index = (Integer) v.getTag();

												toDetailQAppActivity(module_infos[index]);

											}
										});
										module1_adapter = new OneJingXuan_Module1_Adapter(One_JingXuanFragment.this,
												module_infos[i].getList());
										module1_lv.setAdapter(module1_adapter);
										setHeight(module1_lv);
										container.addView(view_module1);
										break;
									case 2:
										View view_module2 = inflater.inflate(R.layout.one_jingxuan_model_2, null);
										TextView title2 = (TextView) view_module2.findViewById(R.id.module_2_title);
										title2.setText(module_infos[i].getTitle());
										View module2_more = view_module2.findViewById(R.id.module_2_more);
										module2_more.setTag(i);
										module2_more.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {

												int index = (Integer) v.getTag();
												toDetailQAppActivity(module_infos[index]);

											}
										});
										View module2_change = view_module2.findViewById(R.id.module_2_change);
										module2_change.setTag(i);
										module2_change.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {

												int index = (Integer) v.getTag();
												if (module_indexs[index] + 6 < module_infos[index].getList().size()) {
													module_indexs[index] += 6;

												} else {
													module_indexs[index] = 0;
												}
												module2_adapter.updateData(module_infos[index].getList(),
														module_indexs[index]);

											}
										});
										GridView gv = (GridView) view_module2.findViewById(R.id.module_2_gv);
										gv.setTag(i);
										gv.setOnItemClickListener(new OnItemClickListener() {

											@Override
											public void onItemClick(AdapterView<?> parent, View view, int position,
													long id) {
												int index = (Integer) parent.getTag();
												toDetailQAppActivity(module_infos[index].getList().get(position));

											}
										});
										module2_adapter = new OneJingXuan_Module2_Adapter(One_JingXuanFragment.this,
												gv, module_infos[i].getList());
										gv.setAdapter(module2_adapter);
										setGridViewHeight(gv, 3);
										container.addView(view_module2);
										break;
									case 3:
										// View view_module3 =
										// inflater.inflate(R.layout.one_jingxuan_model_3,
										// null);
										// TextView title3 = (TextView)
										// view_module3.findViewById(R.id.module_3_title);
										// title3.setText(module_infos[i].getTitle());
										// View module3_more =
										// view_module3.findViewById(R.id.module_3_more);
										// module3_more.setTag(module_infos[i]);
										//
										// module3_more.setOnClickListener(new
										// OnClickListener() {
										// @Override
										// public void onClick(View v) {
										// QAppInfo info = (QAppInfo)
										// v.getTag();
										// toDetailQAppActivity(info);
										// }
										// });
										// View module3_change =
										// view_module3.findViewById(R.id.module_3_change);
										//
										// module3_change.setTag(i);
										// module3_change.setOnClickListener(new
										// OnClickListener() {
										// @Override
										// public void onClick(View v) {
										//
										// int index = (Integer) v.getTag();
										// if (module_indexs[index] + 3 <
										// module_infos[index].getList().size())
										// {
										// module_indexs[index] += 3;
										//
										// } else {
										// module_indexs[index] = 0;
										// }
										// module3_Adapter.updateData(module_infos[index].getList(),
										// module_indexs[index]);
										//
										// }
										// });
										// GridView gv3 = (GridView)
										// view_module3.findViewById(R.id.module_3_gv);
										// gv3.setTag(i);
										// gv3.setOnItemClickListener(new
										// OnItemClickListener() {
										//
										// @Override
										// public void
										// onItemClick(AdapterView<?> parent,
										// View view, int position,
										// long id) {
										// int index = (Integer)
										// parent.getTag();
										// toDetailQAppActivity(module_infos[index].getList().get(position));
										//
										// }
										// });
										// module3_Adapter = new
										// OneJingXuan_Module3_Adapter(One_JingXuanFragment.this,
										// gv3, module_infos[i].getList());
										// gv3.setAdapter(module3_Adapter);
										// setGridViewHeight(gv3, 3);
										// container.addView(view_module3);
										break;
									case 4:
										final View view_module4 = inflater.inflate(R.layout.one_jingxuan_model_4, null);
										view_module4.setTag(i);
										view_module4.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												int index = (Integer) v.getTag();
												toDetailQAppActivity(module_infos[index].getList().get(
														module_indexs[index]));

											}
										});
										TextView title4 = (TextView) view_module4.findViewById(R.id.module_4_title);
										title4.setText(module_infos[i].getTitle());
										View module4_more = view_module4.findViewById(R.id.module_4_more);
										module4_more.setTag(i);
										module4_more.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {

												int index = (Integer) v.getTag();

												toDetailQAppActivity(module_infos[index]);

											}
										});
										View module4_change = view_module4.findViewById(R.id.module_4_change);
										module4_change.setTag(i);
										module4_change.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												int index = (Integer) v.getTag();
												TextView joke_content = (TextView) v.getTag(R.id.module_4_content);
												if (module_indexs[index] + 1 < module_infos[index].getList().size()) {
													module_indexs[index]++;

												} else {
													module_indexs[index] = 0;
												}

												joke_content.setText(module_infos[index].getList().get(
														module_indexs[index]).summary);

											}
										});

										ImageView img = (ImageView) view_module4.findViewById(R.id.module_4_img);
										img.setImageResource(R.drawable.jokeimg);
										// String imgUrl =
										// module_infos[i].getImage();
										// if (imgUrl.contains("http://")) {
										// bitmapUtils.display(img, imgUrl);
										// } else {
										// bitmapUtils.display(img,
										// RequestURL.httpIP + imgUrl);
										//
										// }

										TextView joke_content = (TextView) view_module4
												.findViewById(R.id.module_4_content);
										module4_change.setTag(R.id.module_4_content, joke_content);
										if (module_infos[i].getList() != null && module_infos[i].getList().size() > 0) {
											joke_content.setText(Html
													.fromHtml(module_infos[i].getList().get(0).summary));
										}

										container.addView(view_module4);
										break;
									case 5:
										final View view_module5 = inflater.inflate(R.layout.one_jingxuan_model_5, null);
										view_module5.setTag(i);
										view_module5.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												int index = (Integer) v.getTag();
												toDetailQAppActivity(module_infos[index].getList().get(
														module_indexs[index]));

											}
										});
										TextView title5 = (TextView) view_module5.findViewById(R.id.module_5_title);
										title5.setText(module_infos[i].getTitle());
										View module5_more = view_module5.findViewById(R.id.module_5_more);
										module5_more.setTag(i);
										module5_more.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												int index = (Integer) v.getTag();

												toDetailQAppActivity(module_infos[index]);

											}
										});
										View module5_change = view_module5.findViewById(R.id.module_5_change);
										module5_change.setTag(i);
										module5_change.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												int index = (Integer) v.getTag();
												ImageView img5 = (ImageView) v.getTag(R.id.module_5_img);
												TextView desc = (TextView) v.getTag(R.id.module_5_desc);
												String url = module_infos[index].getList().get(module_indexs[index]).image;

												bitmapUtils.display(img5, url);

												desc.setText(module_infos[index].getList().get(module_indexs[index]).title);

											}
										});
										ImageView img5 = (ImageView) view_module5.findViewById(R.id.module_5_img);
										TextView desc = (TextView) view_module5.findViewById(R.id.module_5_desc);
										module5_change.setTag(R.id.module_5_img, img5);
										module5_change.setTag(R.id.module_5_desc, desc);

										if (module_infos[i].getList() != null && module_infos[i].getList().size() > 0) {

											String url = module_infos[i].getList().get(0).image;

											bitmapUtils.display(img5, url);

											desc.setText(module_infos[i].getList().get(0).title);
										}
										container.addView(view_module5);
										break;

									case 6:
										// View view_module6 =
										// inflater.inflate(R.layout.one_jingxuan_model_6,
										// null);
										//
										// TextView title6 = (TextView)
										// view_module6.findViewById(R.id.module_6_title);
										// title6.setText(module_infos[i].getTitle());
										// View module6_more =
										// view_module6.findViewById(R.id.module_6_more);
										// module6_more.setTag(i);
										// module6_more.setOnClickListener(new
										// OnClickListener() {
										// @Override
										// public void onClick(View v) {
										//
										// int index = (Integer) v.getTag();
										// toDetailQAppActivity(module_infos[index]);
										//
										// }
										// });
										// View module6_change =
										// view_module6.findViewById(R.id.module_6_change);
										// module6_change.setTag(i);
										// module6_change.setOnClickListener(new
										// OnClickListener() {
										// @Override
										// public void onClick(View v) {
										//
										// int index = (Integer) v.getTag();
										// if (module_indexs[index] + 4 <
										// module_infos[index].getList().size())
										// {
										// module_indexs[index] += 4;
										//
										// } else {
										// module_indexs[index] = 0;
										// }
										// module6_Adapter.updateData(module_infos[index].getList(),
										// module_indexs[index]);
										//
										// }
										// });
										// GridView gv_6 = (GridView)
										// view_module6.findViewById(R.id.module_6_gv);
										// gv_6.setTag(i);
										// gv_6.setOnItemClickListener(new
										// OnItemClickListener() {
										//
										// @Override
										// public void
										// onItemClick(AdapterView<?> parent,
										// View view, int position,
										// long id) {
										// int index = (Integer)
										// parent.getTag();
										// toDetailQAppActivity(module_infos[index].getList().get(position));
										//
										// }
										// });
										// module6_Adapter = new
										// OneJingXuan_Module6_Adapter(One_JingXuanFragment.this,
										// gv_6, module_infos[i].getList());
										// gv_6.setAdapter(module6_Adapter);
										// setGridViewHeight(gv_6, 2);
										// container.addView(view_module6);
										break;

									default:
										break;
									}

								}

							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

						Toast.makeText(context, "请求数据出错", 0).show();

					}
				});

		ClientTask.getInstance(context).addToRequestQueue(request);

	}

	/**
	 * 
	 * 功能描述：跳转到轻应用
	 * 
	 * */

	private void toDetailQAppActivity(QAppInfo info) {
		Intent intent = new Intent(getActivity(), DetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constant.QAPPINFO, info);
		intent.putExtras(bundle);

		getActivity().startActivity(intent);

		getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

	}

	private void setHeight(ListView listView) {
		Adapter adapter = listView.getAdapter();
		if (adapter == null) {
			return;
		}
		int totalHeight = 0;
		int size = adapter.getCount();
		for (int i = 0; i < size; i++) {
			View listItem = adapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (size - 1));
		listView.setLayoutParams(params);
	}

	private void setGridViewHeight(GridView gv, int n) {
		Adapter adapter = gv.getAdapter();
		if (adapter == null) {
			return;
		}
		int totalHeight = 0;
		int s = adapter.getCount();
		for (int i = 0; i < s; i++) {
			if (i % n == 0) {
				View listItem = adapter.getView(i, null, gv);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
		}
		ViewGroup.LayoutParams params = gv.getLayoutParams();
		params.height = totalHeight + gv.getPaddingTop() + gv.getPaddingBottom() + 2;
		gv.setLayoutParams(params);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.manage_card:// 管理卡片

			Intent intent = new Intent(getActivity(), CardManagerActivity.class);
			startActivity(intent);

			getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
		default:
			break;
		}
	}

	private void setIndicator(int position) {
		position %= imgInfos.size();
		for (ImageView indicator : mIndicators) {
			indicator.setImageResource(R.drawable.indicator_unchecked);
		}
		mIndicators[position].setImageResource(R.drawable.indicator_checked);
	}

	private class BannerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

		private LayoutInflater mInflater;

		public BannerAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return FAKE_BANNER_SIZE;
		}

		@Override
		public boolean isViewFromObject(View view, Object o) {
			return view == o;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			position %= imgInfos.size();
			View view = mInflater.inflate(R.layout.viewpager_item, container, false);
			ImageView imageView = (ImageView) view.findViewById(R.id.image);
			// imageView.setImageResource(mImagesSrc[position]);
			bitmapUtils.display(imageView, imgInfos.get(position).getImage());

			final int pos = position;
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public void finishUpdate(ViewGroup container) {
			int position = mBanner.getCurrentItem();
			if (position == 0) {
				position = imgInfos.size();
				mBanner.setCurrentItem(position, false);
			} else if (position == FAKE_BANNER_SIZE - 1) {
				position = imgInfos.size() - 1;
				mBanner.setCurrentItem(position, false);
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			mBannerPosition = position;
			setIndicator(position);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	}

}
