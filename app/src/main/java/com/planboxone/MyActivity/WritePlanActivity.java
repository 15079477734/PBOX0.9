package com.planboxone.MyActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.library.googledatetimepicker.date.DatePickerDialog;
import com.library.util.DatabaseManage;
import com.library.util.GetDate;
import com.planboxone.R;

import java.util.Calendar;
import java.util.Map;

import static com.library.util.Format.pad;
import static com.library.util.GetDate.getWeek;

public class WritePlanActivity extends BaseActivity {
    private final static String TAG = "MyWritePlanActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        init();
    }

    public void init() {
        String str = null;
        String dbName = null;
        Intent intent = getIntent();

        try {
            str = intent.getStringExtra("_id");
            dbName = intent.getStringExtra("dbName");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyViewHolder viewHolder;
        if (null == str) {
            viewHolder = new MyViewHolder(this);

        } else {
            viewHolder = new MyViewHolder(this, str, dbName);

        }
        viewHolder.ViewWrite();
        setContentView(viewHolder.getContentView());
    }

    public class MyViewHolder {
        private Context mContext;
        private DatabaseManage mDatabaseManage;
        private ContentValues mDatavalues;
        private ViewWriteHolder mViewWriteHolder;
        private View mView;
        private String mTitle, mDate, mCategory, mTop, mNote, mTime;
        private String eoc = null;
        private final Calendar mCalendar = Calendar.getInstance();


        public MyViewHolder(Context context) {
            this.mContext = context;
            mDatabaseManage = new DatabaseManage(mContext, "AP");
            mTitle = "";
            mCategory = "工作";
            mDate = GetDate.getDate();
            mTop = "0";
            mNote = "";
            eoc = "0";
            mTime = "A计划";

        }


        public MyViewHolder(Context context, String str, String dbName) {
            this.mContext = context;
            mDatabaseManage = new DatabaseManage(mContext, dbName);
            eoc = str;
            Map<String, String> map = mDatabaseManage.findData("_id = ?", new String[]{str});
            mTitle = map.get((String) ("title"));
            mCategory = map.get((String) ("category"));
            mDate = map.get((String) ("date"));
            mTop = map.get((String) ("top"));
            mNote = map.get((String) ("note"));
            mTime = map.get((String) ("time"));
        }

        public void ViewWrite() {


            final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

                    String str = pad(year) + "-" + pad(month + 1) + "-" + pad(day) + " " + getWeek(year, month, day);
                    mViewWriteHolder.date_textview.setText(str);
                    mDatavalues.put("date", str);
                }

            }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

            mDatavalues = new ContentValues();
            mViewWriteHolder = new ViewWriteHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            mView = inflater.inflate(R.layout.activity_write_plan, null);


            mViewWriteHolder.date_textview = (TextView) mView.findViewById(R.id.date_textview);
            mViewWriteHolder.category_textview = (TextView) mView.findViewById(R.id.category_textview);
            mViewWriteHolder.time_textview = (TextView) mView.findViewById(R.id.time_textview);
            mViewWriteHolder.note_edittext = (EditText) mView.findViewById(R.id.note_edittext);

            mViewWriteHolder.note_edittext.setEnabled(true);
            mViewWriteHolder.note_edittext.setFocusable(true);
            mViewWriteHolder.note_edittext.setFocusableInTouchMode(true);
            mViewWriteHolder.note_edittext.setClickable(true);
            mViewWriteHolder.note_edittext.requestFocus();


            mViewWriteHolder.title_edittext = (EditText) mView.findViewById(R.id.title_edittext);
            mViewWriteHolder.note_edittext.setEnabled(true);
            mViewWriteHolder.top_checkBox = (CheckBox) mView.findViewById(R.id.top_checkbox);
            mViewWriteHolder.confirm_button = (Button) mView.findViewById(R.id.confirm_button);
            mViewWriteHolder.cancel_button = (Button) mView.findViewById(R.id.cancel);
            mViewWriteHolder.class_layout = (RelativeLayout) mView.findViewById(R.id.class_relative);
            mViewWriteHolder.title_layout = (RelativeLayout) mView.findViewById(R.id.title_relative);
            mViewWriteHolder.date_layout = (RelativeLayout) mView.findViewById(R.id.date_relative);
            mViewWriteHolder.note_layout = (RelativeLayout) mView.findViewById(R.id.note_relative);
            mViewWriteHolder.time_layout = (RelativeLayout) mView.findViewById(R.id.time_relative);


            mDatavalues.put("title", mTitle);
            mDatavalues.put("date", mDate);
            mDatavalues.put("note", mNote);
            mDatavalues.put("category", mCategory);
            mDatavalues.put("time", mTime);
            mDatavalues.put("top", mTop);
            if (mTop.equals("1"))
                mViewWriteHolder.top_checkBox.setChecked(true);
            else
                mViewWriteHolder.top_checkBox.setChecked(false);
            mViewWriteHolder.time_textview.setText(mTime);
            mViewWriteHolder.category_textview.setText(mCategory);
            mViewWriteHolder.date_textview.setText(mDate);
            mViewWriteHolder.title_edittext.setText(mTitle);
            mViewWriteHolder.date_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tag = "";
                    datePickerDialog.show(WritePlanActivity.this.getFragmentManager(), tag);
                }
            });
            mViewWriteHolder.class_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    final View view1 = inflater.inflate(R.layout.dialog_class, null);
                    final RadioGroup radioGroup = (RadioGroup) view1.findViewById(R.id.class_radiogroup);
                    final AlertDialog alertDialog = new AlertDialog.Builder(mContext).setTitle("分类选择").setView(view1).create();
                    alertDialog.show();
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        public void onCheckedChanged(RadioGroup arg0, int arg1) {
                            int radioButtonId = arg0.getCheckedRadioButtonId();
                            RadioButton rb = (RadioButton) view1.findViewById(radioButtonId);
                            String str = rb.getText().toString();
                            mViewWriteHolder.category_textview.setText(str);
                            mDatavalues.put("category", str);
                            alertDialog.dismiss();
                        }
                    });
                }
            });
            mViewWriteHolder.top_checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mViewWriteHolder.top_checkBox.isChecked()) ;
                }
            });
            mViewWriteHolder.time_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    final View view1 = inflater.inflate(R.layout.dialog_time, null);
                    RadioGroup radioGroup = (RadioGroup) view1.findViewById(R.id.time_radiogroup);
                    final AlertDialog alertDialog = new AlertDialog.Builder(mContext).setView(view1).setTitle("时间选择 ").create();
                    alertDialog.show();
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        public void onCheckedChanged(RadioGroup arg0, int arg1) {
                            int radioButtonId = arg0.getCheckedRadioButtonId();
                            RadioButton rb = (RadioButton) view1.findViewById(radioButtonId);
                            String str = rb.getText().toString();
                            String dbName = "AP";
                            if (str.equals("A计划")) dbName = "AP";
                            if (str.equals("B计划")) dbName = "BP";
                            if (str.equals("C计划")) dbName = "CP";
                            mDatabaseManage = new DatabaseManage(mContext, dbName);
                            mViewWriteHolder.time_textview.setText(str);
                            mDatavalues.put("time", str);
                            alertDialog.dismiss();
                        }
                    });
                }
            });
            mViewWriteHolder.confirm_button.setOnClickListener(new View.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(View view) {
                                                                       String title = mViewWriteHolder.title_edittext.getText().toString();
                                                                       if (mViewWriteHolder.top_checkBox.isChecked())
                                                                           mDatavalues.put("top", "1");
                                                                       else
                                                                           mDatavalues.put("top", "0");
                                                                       if (title.equals(""))
                                                                           Toast.makeText(mContext, "标题不能为空", Toast.LENGTH_SHORT).show();
                                                                       else {
                                                                           mDatavalues.put("title", title);
                                                                           mDatavalues.put("note", mViewWriteHolder.note_edittext.getText().toString());
                                                                           if (eoc.equals("0")) {
                                                                               mDatabaseManage.addData(mDatavalues);
                                                                               Toast.makeText(mContext, "保存成功", Toast.LENGTH_LONG).show();
                                                                               finish();
                                                                           } else {
                                                                               Log.e("123", mDatavalues.toString());
                                                                               if (mDatabaseManage.updateData(mDatavalues, "_id=?", new String[]{eoc}))
                                                                                   Toast.makeText(mContext, "更新成功", Toast.LENGTH_LONG).show();
                                                                               finish(); /*  Intent intent = new Intent(MyWritePlanActivity.this, MyListPlanActivity.class); startActivity(intent);*/
                                                                           }
                                                                       }
                                                                   }
                                                               }
            );
            mViewWriteHolder.cancel_button.setOnClickListener(new View.OnClickListener()

                                                              {
                                                                  @Override
                                                                  public void onClick(View view) {
                                                                      finish();
                                                                  }
                                                              }
            );


        }

        public View getContentView() {
            return mView;
        }


    }

    final class ViewWriteHolder {
        public EditText title_edittext;
        public TextView date_textview, category_textview, note_edittext, time_textview;
        public CheckBox top_checkBox;
        public Button confirm_button, cancel_button;
        public RelativeLayout title_layout, note_layout, date_layout, class_layout, time_layout;

    }
}
