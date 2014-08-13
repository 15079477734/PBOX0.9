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
        String str = null;
        String dbName = null;
        Intent intent = getIntent();

        try {
            str = intent.getStringExtra("_id");
            dbName = intent.getStringExtra("dbName");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == str) {
            MyViewHolder viewHolder = new MyViewHolder(this);
            viewHolder.ViewWrite();
            setContentView(viewHolder.getContentView());
        } else {
            MyViewHolder viewHolder = new MyViewHolder(this, str, dbName);
            viewHolder.ViewWrite();
            setContentView(viewHolder.getContentView());
        }
    }


    public class MyViewHolder {
        private Context mContext;
        private DatabaseManage databaseManage;
        private ContentValues datavalues;
        private ViewWriteHolder viewWriteHolder;
        private View view;
        public String mTitle, mDate, mCategory, mTop, mNote, mTime;
        public String eoc = null;
        private final Calendar mCalendar = Calendar.getInstance();


        public MyViewHolder(Context context) {
            this.mContext = context;
            databaseManage = new DatabaseManage(mContext, "AP");
            mTitle = "";
            mCategory = "工作";
            mDate = GetDate.getDate();
            mTop = "0";
            mNote = "";
            eoc = "0";
            mTime = "A计划";

        }

        public MyViewHolder(Context context, String str) {
            this.mContext = context;
            databaseManage = new DatabaseManage(mContext, "AP");
            eoc = str;
            Map<String, String> map = databaseManage.findData("_id = ?", new String[]{str});
            mTitle = map.get((String) ("title"));
            mCategory = map.get((String) ("category"));
            mDate = map.get((String) ("date"));
            mTop = map.get((String) ("top"));
            mNote = map.get((String) ("note"));
            mTime = map.get((String) ("time"));
        }

        public MyViewHolder(Context context, String str, String dbName) {
            this.mContext = context;
            databaseManage = new DatabaseManage(mContext, dbName);
            eoc = str;
            Map<String, String> map = databaseManage.findData("_id = ?", new String[]{str});
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
                    viewWriteHolder.date_textview.setText(str);
                    datavalues.put("date", str);
                }

            }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

            datavalues = new ContentValues();
            viewWriteHolder = new ViewWriteHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.activity_write_plan, null);


            viewWriteHolder.date_textview = (TextView) view.findViewById(R.id.date_textview);
            viewWriteHolder.category_textview = (TextView) view.findViewById(R.id.category_textview);
            viewWriteHolder.time_textview = (TextView) view.findViewById(R.id.time_textview);
            viewWriteHolder.note_edittext = (EditText) view.findViewById(R.id.note_edittext);

            viewWriteHolder.note_edittext.setEnabled(true);
            viewWriteHolder.note_edittext.setFocusable(true);
            viewWriteHolder.note_edittext.setFocusableInTouchMode(true);
            viewWriteHolder.note_edittext.setClickable(true);
            viewWriteHolder.note_edittext.requestFocus();


            viewWriteHolder.title_edittext = (EditText) view.findViewById(R.id.title_edittext);
            viewWriteHolder.note_edittext.setEnabled(true);
            viewWriteHolder.top_checkBox = (CheckBox) view.findViewById(R.id.top_checkbox);
            viewWriteHolder.confirm_button = (Button) view.findViewById(R.id.confirm_button);
            viewWriteHolder.cancel_button = (Button) view.findViewById(R.id.cancel);
            viewWriteHolder.class_layout = (RelativeLayout) view.findViewById(R.id.class_relative);
            viewWriteHolder.title_layout = (RelativeLayout) view.findViewById(R.id.title_relative);
            viewWriteHolder.date_layout = (RelativeLayout) view.findViewById(R.id.date_relative);
            viewWriteHolder.note_layout = (RelativeLayout) view.findViewById(R.id.note_relative);
            viewWriteHolder.time_layout = (RelativeLayout) view.findViewById(R.id.time_relative);


            datavalues.put("title", mTitle);
            datavalues.put("date", mDate);
            datavalues.put("note", mNote);
            datavalues.put("category", mCategory);
            datavalues.put("time", mTime);
            datavalues.put("top", mTop);
            if (mTop.equals("1"))
                viewWriteHolder.top_checkBox.setChecked(true);
            else
                viewWriteHolder.top_checkBox.setChecked(false);
            viewWriteHolder.time_textview.setText(mTime);
            viewWriteHolder.category_textview.setText(mCategory);
            viewWriteHolder.date_textview.setText(mDate);
            viewWriteHolder.title_edittext.setText(mTitle);
            viewWriteHolder.date_layout.setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View view) {
                                                                   String tag = "";
                                                                   datePickerDialog.show(WritePlanActivity.this.getFragmentManager(), tag);
                                                               }
                                                           }
            );
            viewWriteHolder.class_layout.setOnClickListener(new View.OnClickListener()

                                                            {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    LayoutInflater inflater = LayoutInflater.from(mContext);
                                                                    final View view1 = inflater.inflate(R.layout.dialog_class, null);
                                                                    final RadioGroup radioGroup = (RadioGroup) view1.findViewById(R.id.class_radiogroup);
                                                                    final AlertDialog alertDialog = new AlertDialog.Builder(mContext).setTitle("分类选择").setView(view1).
                                                                            create();
                                                                    alertDialog.show();
                                                                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                                        public void onCheckedChanged(RadioGroup arg0, int arg1) {

                                                                            int radioButtonId = arg0.getCheckedRadioButtonId();
                                                                            RadioButton rb = (RadioButton) view1.findViewById(radioButtonId);
                                                                            String str = rb.getText().toString();
                                                                            viewWriteHolder.category_textview.setText(str);
                                                                            datavalues.put("category", str);
                                                                            alertDialog.dismiss();
                                                                        }
                                                                    });
                                                                }
                                                            }
            );
            viewWriteHolder.top_checkBox.setOnClickListener(new View.OnClickListener()

                                                            {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    if (viewWriteHolder.top_checkBox.isChecked())
                                                                        ;
                                                                }
                                                            }
            );

            viewWriteHolder.time_layout.setOnClickListener(new View.OnClickListener()

                                                           {
                                                               @Override
                                                               public void onClick(View view) {
                                                                   LayoutInflater inflater = LayoutInflater.from(mContext);
                                                                   final View view1 = inflater.inflate(R.layout.dialog_time, null);
                                                                   RadioGroup radioGroup = (RadioGroup) view1.findViewById(R.id.time_radiogroup);
                                                                   final AlertDialog alertDialog = new AlertDialog.Builder(mContext).setView(view1).setTitle("时间选择 ").
                                                                           create();
                                                                   alertDialog.show();
                                                                   radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                                       public void onCheckedChanged(RadioGroup arg0, int arg1) {
                                                                           // TODO Auto-generated method stub
                                                                           int radioButtonId = arg0.getCheckedRadioButtonId();
                                                                           RadioButton rb = (RadioButton) view1.findViewById(radioButtonId);
                                                                           String str = rb.getText().toString();
                                                                           String dbName = "AP";
                                                                           if (str.equals("A计划"))
                                                                               dbName = "AP";
                                                                           if (str.equals("B计划"))
                                                                               dbName = "BP";
                                                                           if (str.equals("C计划"))
                                                                               dbName = "CP";

                                                                           databaseManage = new DatabaseManage(mContext, dbName);
                                                                           viewWriteHolder.time_textview.setText(str);
                                                                           datavalues.put("time", str);
                                                                           alertDialog.dismiss();


                                                                       }
                                                                   });
                                                               }
                                                           }
            );
            viewWriteHolder.confirm_button.setOnClickListener(new View.OnClickListener()

                                                              {
                                                                  @Override
                                                                  public void onClick(View view) {

                                                                      String title = viewWriteHolder.title_edittext.getText().toString();
                                                                      if (viewWriteHolder.top_checkBox.isChecked()) {
                                                                          datavalues.put("top", "1");
                                                                      } else
                                                                          datavalues.put("top", "0");
                                                                      if (title.equals("")) {
                                                                          Toast.makeText(mContext, "标题不能为空", Toast.LENGTH_SHORT).show();
                                                                      } else {
                                                                          datavalues.put("title", title);
                                                                          datavalues.put("note", viewWriteHolder.note_edittext.getText().toString());
                                                                          if (eoc.equals("0")) {
                                                                              databaseManage.addData(datavalues);
                                                                              Toast.makeText(mContext, "保存成功", Toast.LENGTH_LONG).show();
                                                                              finish();

                                                                          } else {
                                                                              Log.e("123", datavalues.toString());
                                                                              if (
                                                                                      databaseManage.updateData(datavalues, "_id=?", new String[]{eoc}))
                                                                                  Toast.makeText(mContext, "更新成功", Toast.LENGTH_LONG).show();
                                                                              finish();
                                                                            /*  Intent intent = new Intent(MyWritePlanActivity.this, MyListPlanActivity.class);
                                                                              startActivity(intent);*/

                                                                          }
                                                                      }
                                                                  }
                                                              }
            );
            viewWriteHolder.cancel_button.setOnClickListener(new View.OnClickListener()

                                                             {
                                                                 @Override
                                                                 public void onClick(View view) {
                                                                     finish();
                                                                 }
                                                             }
            );


        }

        public View getContentView() {
            return view;
        }

        class ViewWriteHolder {
            public EditText title_edittext;
            public TextView date_textview, category_textview, note_edittext, time_textview;
            public CheckBox top_checkBox;
            public Button confirm_button, cancel_button;
            public RelativeLayout title_layout, note_layout, date_layout, class_layout, time_layout;

        }
    }
}
