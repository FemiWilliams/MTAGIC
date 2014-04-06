package com.example.mtagic;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class GestureSettings extends Activity implements OnClickListener{

	private int level;
	private int score;
	private int currentGestureNumber;
	private String gameType;
	private String name;
	
	private ImageView colorR;
	private ImageView colorO;
	private ImageView colorY;
	private ImageView colorG;
	private ImageView colorB;
	private ImageView colorP;
	private ImageView colorBl;
	private ImageView colorC;
	private ImageView colorPi;
	private ImageView thick1;
	private ImageView thick2;
	private ImageView thick3;
	private Button buttonColor;
	
	private int lineWidth = 5;
	private String color = "#000000";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_gesture_settings);
		
		
		
		Intent intent = getIntent();
		level = intent.getIntExtra("LEVEL", 0);
		score = intent.getIntExtra("SCORE", 0);
		gameType = intent.getStringExtra("TYPE");
		name = intent.getStringExtra("NAME");
		currentGestureNumber = intent.getIntExtra("CURRENT_GESTURE", 0);
		
		//set up the colors for the squares
		colorR = (ImageView) findViewById(R.id.red_square);
		colorO = (ImageView) findViewById(R.id.orange_square);
		colorY = (ImageView) findViewById(R.id.yellow_square);
		colorG = (ImageView) findViewById(R.id.green_square);
		colorB = (ImageView) findViewById(R.id.blue_square);
		colorP = (ImageView) findViewById(R.id.purple_square);
		colorBl = (ImageView) findViewById(R.id.black_square);
		colorC = (ImageView) findViewById(R.id.cyan_square);
		colorPi = (ImageView) findViewById(R.id.pink_square);
		thick1 = (ImageView) findViewById(R.id.thick_1);
		thick2 = (ImageView) findViewById(R.id.thick_2);
		thick3 = (ImageView) findViewById(R.id.thick_3);
		buttonColor = (Button) findViewById(R.id.button_color);
		
		buttonColor.setOnClickListener(this);
		colorR.setOnClickListener(this);
		colorO.setOnClickListener(this);
		colorY.setOnClickListener(this);
		colorG.setOnClickListener(this);
		colorB.setOnClickListener(this);
		colorP.setOnClickListener(this);
		colorBl.setOnClickListener(this);
		colorC.setOnClickListener(this);
		colorPi.setOnClickListener(this);
		thick1.setOnClickListener(this);
		thick2.setOnClickListener(this);
		thick3.setOnClickListener(this);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gesture_settings, menu);
		return true;
	}

	//changes the selection for all the squares
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
			case R.id.red_square:
			{
				color = "#FF0000";
				clearColors();
				colorR.setImageResource(R.drawable.check_black);
				break;
			}
			case R.id.orange_square:
			{
				color = "#FFA500";
				clearColors();
				colorO.setImageResource(R.drawable.check_black);
				break;
			}
			case R.id.yellow_square:
			{
				color = "#FFFF00";
				clearColors();
				colorY.setImageResource(R.drawable.check_black);
				break;
			}
			case R.id.green_square:
			{
				color = "#008000";
				clearColors();
				colorG.setImageResource(R.drawable.check_black);
				break;
			}
			case R.id.blue_square:
			{
				color = "#0000FF";
				clearColors();
				colorB.setImageResource(R.drawable.check_black);
				break;
			}
			case R.id.purple_square:
			{
				color = "#800080";
				clearColors();
				colorP.setImageResource(R.drawable.check_black);
				break;
			}
			case R.id.black_square:
			{
				color = "#000000";
				clearColors();
				colorBl.setImageResource(R.drawable.check_red);
				break;
			}
			case R.id.cyan_square:
			{
				color = "#00FFFF";
				clearColors();
				colorC.setImageResource(R.drawable.check_black);
				break;
			}
			case R.id.pink_square:
			{
				color = "#F52887";
				clearColors();
				colorPi.setImageResource(R.drawable.check_black);
				break;
			}
			case R.id.thick_1:
			{
				clearLines();
				thick1.setBackgroundColor(Color.parseColor("#66FF33"));
				lineWidth = 1;
				break;
			}
			case R.id.thick_2:
			{
				clearLines();
				thick2.setBackgroundColor(Color.parseColor("#66FF33"));
				lineWidth = 3;
				break;
			}
			case R.id.thick_3:
			{
				clearLines();
				thick3.setBackgroundColor(Color.parseColor("#66FF33"));
				lineWidth = 5;
				break;
			}
			case R.id.button_color:
			{
			
				Intent intent = new Intent(this, GestureGame.class);
				intent.putExtra("LEVEL", level);
				intent.putExtra("SCORE", score);
				intent.putExtra("NAME", name);
				intent.putExtra("TYPE", gameType);
				intent.putExtra("LINE_WIDTH", lineWidth);
				intent.putExtra("COLOR", color);
				intent.putExtra("CURRENT_GESTURE", currentGestureNumber);
				startActivity(intent);
				finish();
			}
		}


	}
	
	public void clearColors(){
		colorR.setImageDrawable(null);
		colorO.setImageDrawable(null);
		colorY.setImageDrawable(null);
		colorG.setImageDrawable(null);
		colorB.setImageDrawable(null);
		colorP.setImageDrawable(null);
		colorBl.setImageDrawable(null);
		colorC.setImageDrawable(null);
		colorPi.setImageDrawable(null);
		
	}
	
	public void clearLines(){
		thick1.setBackgroundColor(Color.parseColor("#00000000"));
		thick2.setBackgroundColor(Color.parseColor("#00000000"));
		thick3.setBackgroundColor(Color.parseColor("#00000000"));
		
	}
	
	// disable the back button on the phone
		@Override
		public void onBackPressed() {
		}
}