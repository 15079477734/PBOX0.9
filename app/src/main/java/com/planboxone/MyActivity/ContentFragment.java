package com.planboxone.MyActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.library.util.DatabaseManage;
import com.library.util.Daysrange;
import com.library.util.GetDate;
import com.library.util.MemoryCache;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import com.planboxone.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2014/7/24 0024.
 */
public class ContentFragment extends Fragment {
    private final static String TAG = "ContentFragment";
    private DatabaseManage databaseManage;
    private ListView listViewReady;
    private ArrayList<String> ids;
    private int newsType = 0;
    public List<Map<String, String>> data;
    public List<Map<String, String>> data2;
    private String dbName;
    private BaseAdapter mAdapter;
    private Daysrange daysrange;

    public ContentFragment() {
        super();
    }

    public ContentFragment(int newsType) {
        this.newsType = newsType;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbName = "AP";
        switch (newsType) {
            case 0:
                dbName = "AP";
                break;
            case 1:
                dbName = "BP";
                break;
        }
        Log.e(TAG, dbName);
        databaseManage = new DatabaseManage(getActivity(), dbName);
        daysrange = new Daysrange();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.activity_my_list, null);
        listViewReady = (ListView) view.findViewById(android.R.id.list);
        listViewReady.setDivider(null);
        refresh();

        listViewReady.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                 @Override
                                                 public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                            final View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_tip, null);
                                                     final AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.dialog).setView(view1).create();
                                                     alertDialog.setCanceledOnTouchOutside(true);
                                                     ((Button) view1.findViewById(R.id.btn_delete)).setOnClickListener(new Button.OnClickListener() {
                                                                                                                           @Override
                                                                                                                           public void onClick(View view) {
                                                                                                                               if (databaseManage.deleteData("_id = ?", new String[]{ids.get(position)})) {
                                                                                                                                   Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                                                                                                                                   refresh();
                                                                                                                                   alertDialog.dismiss();
                                                                                                                               }
                                                                                                                           }
                                                                                                                       }
                                                     );
                                                     ((Button) view1.findViewById(R.id.btn_edit)).setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View view) {
                                                             Map<String, String> str = databaseManage.findData("_id = ?", new String[]{ids.get(position)});
                                                             alertDialog.dismiss();
                                                             Intent intent = new Intent();
                                                             intent.putExtra("_id", str.get("_id"));
                                                             intent.putExtra("dbName", dbName);
                                                             intent.setClass(getActivity(), WritePlanActivity.class);
                                                             getActivity().startActivity(intent);
                                                             getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.zoin);
                                                         }
                                                     });
                                                     ((Button) view1.findViewById(R.id.btn_add)).setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View view) {
                                                             ContentValues values = new ContentValues();
                                                             int progress = Integer.valueOf(databaseManage.findData("_id = ?", new String[]{ids.get(position)}).get("progress"));
                                                             progress += 25;
                                                             if (progress > 100)
                                                                 progress = 0;
                                                             values.put("progress", progress);
                                                             databaseManage.updateData(values, "_id = ?", new String[]{ids.get(position)});
                                                         }
                                                     });
                                                     ((Button) view1.findViewById(R.id.btn_progress)).setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View view) {
                                                             alertDialog.dismiss();
                                                             refreshDB();
                                                             refresh();
                                                         }
                                                     });
                                                     alertDialog.show();
                                                 }
                                             }
        );
        return view;

    }
    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        data = databaseManage.listData();
        List<Map<String, String>> data2 = new ArrayList<Map<String, String>>();
        for (Map<String, String> m : data) {
            String str = m.get("progress");
            if (m.get("top").equals("1"))
                m.put("type", "6");
            else if (str.equals("100"))
                m.put("type", "5");
            else {
                int a = daysrange.calculate(m.get("date"));
                if (a > 20)
                    m.put("type", "4");
                else if (a > 10)
                    m.put("type", "3");
                else if (a > 5)
                    m.put("type", "2");
                else
                    m.put("type", "1");
            }
            data2.add(m);
        }
        Collections.sort(data2, new MapComparator());
        ids = new ArrayList<String>();
        for (Map<String, String> m : data2) {
            ids.add(m.get("_id"));
        }

        //    Log.e(TAG, data2.toString());
        mAdapter = new MyAdapter(getActivity(), getItems(), data2);
        AnimationAdapter animAdapter = new ScaleInAnimationAdapter(mAdapter);
        animAdapter.setAbsListView(listViewReady);
        listViewReady.setAdapter(animAdapter);
    }

    public void refreshDB() {


    }

    static class MapComparatorTwo implements Comparator<Map<String, String>> {

        @Override
        public int compare(Map<String, String> o1, Map<String, String> o2) {
            Daysrange daysrange = new Daysrange();
            int a = daysrange.calculate(o1.get("date"));
//            Log.e(TAG, "A =  " + String.valueOf(a));

            Daysrange daysrange2 = new Daysrange();
            int b = daysrange2.calculate(o2.get("date"));
//            Log.e(TAG, "B =  " + String.valueOf(b));


            int c = String.valueOf(a).compareTo(String.valueOf(b));
//            Log.e(TAG, "C =  " + String.valueOf(c));
            return c;

            //   return o1.get("date").compareTo(o1.get("date"));
           /*
           CompareTo详解
             如果前面大于后面  例A=3 B=2 A>B A.compareTo（B）      return 1；
             即 A-B=1
             按从小到大排序
           */
           /* if(a<b)
            {
                return 1;
            }*/
        }
    }

    static class MapComparator implements Comparator<Map<String, String>> {

        @Override
        public int compare(Map<String, String> o1, Map<String, String> o2) {
            // TODO Auto-generated method stub
            String b1 = o1.get("type");
            String b2 = o2.get("type");
            return b1.compareTo(b2);
        }
    }

    public ArrayList<Integer> getItems() {
        ArrayList<Integer> items = new ArrayList<Integer>();
        for (int i = 0; i < data.size(); i++) {
            items.add(i);
        }
        return items;
    }

    private class MyAdapter extends ArrayAdapter<Integer> {
        private final Context mContext;
        private List<Map<String, String>> mData;

        public MyAdapter(Context context, ArrayList<Integer> items) {
            super(items);
            mContext = context;
            mData = data;
        }

        public MyAdapter(Context context, ArrayList<Integer> items, List<Map<String, String>> list) {
            super(items);
            mContext = context;
            this.mData = list;
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).hashCode();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View contentView, ViewGroup parent) {
            ViewHolder holder = null;
            if (contentView == null) {
                holder = new ViewHolder();
                contentView = LayoutInflater.from(getActivity()).inflate(R.layout.plantwo, null);
                holder.title = (TextView) contentView.findViewById(R.id.tv_event_title);
                holder.time = (TextView) contentView.findViewById(R.id.tv_event_time);
                holder.head = (ImageView) contentView.findViewById(R.id.lv_event_head);
                holder.progress = (TextView) contentView.findViewById(R.id.tv_event_progress);
                holder.background = (LinearLayout) contentView.findViewById(R.id.ll_event_background);
                holder.due = (TextView) contentView.findViewById(R.id.tv_event_due);
                contentView.setTag(holder);
            } else {
                holder = (ViewHolder) contentView.getTag();
            }
            int imageResId;
            int t = Integer.valueOf(mData.get(position).get("type"));
            switch (t) {
                case 1:
                    imageResId = R.drawable.cover_bg4;
                    holder.background.setBackgroundResource(R.drawable.listitem_red);
                    break;
                case 2:
                    imageResId = R.drawable.cover_bg5;
                    holder.background.setBackgroundResource(R.drawable.listitem_yellow);
                    break;
                case 3:
                    imageResId = R.drawable.cover_bg6;
                    holder.background.setBackgroundResource(R.drawable.listitem_blue);
                    break;
                case 4:
                    imageResId = R.drawable.cover_bg3;
                    holder.background.setBackgroundResource(R.drawable.listitem_green);
                    break;
                default:
                    imageResId = R.drawable.cover_bg1;
                    holder.background.setBackgroundResource(R.drawable.listitem_white);
                    break;
            }


            holder.background.setPadding(6, 8, 8, 8);
            MemoryCache memoryCache = new MemoryCache();
            Bitmap bitmap = memoryCache.getBitmapFromMemCache(imageResId);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResId);
                memoryCache.addBitmapToMemoryCache(imageResId, bitmap);
            }
            holder.head.setImageBitmap(bitmap);


            Daysrange daysrange = new Daysrange(GetDate.getDatetimeString());
            int a = daysrange.calculate(mData.get(position).get("date"));
            holder.time.setText(String.valueOf(Math.abs(a)) + "天");
            if (a < 0) {
                holder.title.setText(mData.get(position).get("title") + "已经");
            } else {
                holder.title.setText(mData.get(position).get("title") + "还剩");
            }
            holder.progress.setText(mData.get(position).get("progress") + "%");
            holder.due.setText(mData.get(position).get("date"));

            return contentView;
        }
    }

    final class ViewHolder {
        public TextView progress;
        public LinearLayout background;
        public TextView title;
        public TextView time;
        public TextView categatory;
        public ImageView head;
        public CheckBox top;
        public TextView due;
    }
}
