package com.crazytech.android.login;

import com.crazytech.commons.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements LoginTask{
	private Button btnCancel, btnLogin;
	private TextView tvCreateUser;
	private EditText etUser, etPass;
	private int result;
	private LoginTask loginTask;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loginTask = (LoginTask)this;
		tvCreateUser = (TextView)findViewById(R.id.dialog_login_tv_createuser);
		btnCancel = (Button)findViewById(R.id.dialog_login_btn_cancel);
		btnLogin = (Button)findViewById(R.id.dialog_login_btn_login);
		etUser = (EditText)findViewById(R.id.dialog_login_et_user);
		etPass = (EditText)findViewById(R.id.dialog_login_et_pass);
		btnLogin.setOnClickListener(loginClickListener());
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
	}
	
	private OnClickListener loginClickListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String user = etUser.getText().toString();
				String pass = etPass.getText().toString();
				new Authenticate(LoginActivity.this, loginTask).execute(user,pass,getAuthenticateUrl());
			}
		};
	}
	
	@Override
	public void finish() {
		loginTask = null;
		super.finish();
	}
	public String getUsername() {
		return etUser.getText().toString();
	}
	
	public String getUserpass() {
		return etPass.getText().toString();
	}
	
	public String getAuthenticateUrl() {
		return "http://crazytech.co:8010/wtlib/php/authenticate.php";
	}

	

	@Override
	public void onPreExecute() {
		pd = new ProgressDialog(this);
	}

	@Override
	public void onProgressUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancelled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProgressDialog getProgressDialog() {
		return pd;
	}

	@Override
	public void onAunthenticatePostExecute(Integer result, Long id) {
		// TODO Auto-generated method stub
		this.result = result;
		if(result==1){
			finish();
			Toast.makeText(this, getString(R.string.success), Toast.LENGTH_LONG).show();
		} else Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_LONG).show();
		try {
			pd.dismiss();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
