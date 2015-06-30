package co.crazytech.gcmtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private final static int PLAY_SERVICES_RES_REQUEST = 9000;
    public static final String LOG_TAG = "GSMTest";
    private static final String PROP_REG_ID = "registration_id";
    private static final String PROP_APP_VER = "app_version";
    public static final String SENDER_ID = "720690548921";
    private String regId;

    private TextView textvDisplay;
    private EditText editText;

    private GoogleCloudMessaging gcm;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textvDisplay = (TextView)findViewById(R.id.textv_display);
        editText = (EditText)findViewById(R.id.editText);

        if(checkPlayServices()){
            gcm = GoogleCloudMessaging.getInstance(this);
            regId = getRegistrationId(getApplicationContext());
            if (regId.isEmpty()||regId.equals(""))registerInBackground();
            else editText.setText(regId);
        }else{
            Log.i(LOG_TAG,"Google Play Services APK not found");
        }
    }

    private boolean checkPlayServices() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS){
            if (GooglePlayServicesUtil.isUserRecoverableError(result))
                GooglePlayServicesUtil.getErrorDialog(result,this,PLAY_SERVICES_RES_REQUEST).show();
            else {
                Log.i(LOG_TAG, "This device is not supported");
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context){
        final SharedPreferences prefs = getGCMPreferences(context);
        String regId = prefs.getString(PROP_REG_ID,"");
        if (regId.isEmpty()) {
            Log.i(LOG_TAG,"Registration not found");
            return "";
        }
        int regVersion = prefs.getInt(PROP_APP_VER,Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (regVersion!=currentVersion){
            Log.i(LOG_TAG,"App version changed");
            return "";
        }
       return regId;
    }

    private static int getAppVersion(Context context){
        try{
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e){
            throw new RuntimeException("Could not get package name: "+e);
        }
    }

    private SharedPreferences getGCMPreferences(Context context){
        return getSharedPreferences(MainActivity.class.getSimpleName(),Context.MODE_PRIVATE);
    }

    private void registerInBackground() {
        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] params) {
               Context context = getApplicationContext();
                Log.i(MainActivity.LOG_TAG,"Registering device");
                String msg = "";
                try {
                    if(gcm == null) gcm = GoogleCloudMessaging.getInstance(context);
                    regId = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID="+regId;
                    sendRegIdToServer("username","email",regId);
                    storeRegId(context,regId);
                } catch (IOException e) {
                    msg = "Error : "+e.getMessage();
                    Log.e(MainActivity.LOG_TAG,msg);
                }
                return msg;
            }

            @Override
            protected void onPostExecute(Object o) {
                editText.setText(regId);
                super.onPostExecute(o);
            }
        }.execute(null,null,null);
    }

    // server implementation
    private void sendRegIdToServer(String username, String email, String regId) throws UnsupportedEncodingException,IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://phpmysql-crazytechco.rhcloud.com/gcm/gcmreg.php");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name",username));
        params.add(new BasicNameValuePair("email",email));
        params.add(new BasicNameValuePair("regid",regId));
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        HttpResponse httpResponse = httpClient.execute(httpPost);
    }

    private void storeRegId(Context context, String regId){
        SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(LOG_TAG,"Saving regId on app version "+appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROP_REG_ID,regId);
        editor.putInt(PROP_APP_VER,appVersion);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
