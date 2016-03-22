package com.reddevil.loadshedding.networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Sherlock on 3/19/2016.
 */
public class NetworkConnection {

    private Context mContext;
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetworkInfo;

    public NetworkConnection(Context context)
    {
        this.mContext = context;
        mConnectivityManager =(ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean checkConnectionStatus()
    {
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return (mNetworkInfo != null && mNetworkInfo.isConnected());
    }

    public boolean checkWifiConnectionStatus()
    {
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return (mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public boolean checkMobileConnectionStatus()
    {
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return (mNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /*public class LoginProcess extends AsyncTask<String, Void, String> {

    private static final String TAG = "LOGIN PROCESS";
    private boolean isConnectedToInternet, isConnectedToWifi, isConnectedToMobile;
    private ProgressDialog progressDialog;
    private boolean isSuccess = false;
    private String errorMessage;
    private String jsonResponse;
    private Context mContext;

    public LoginProcess(Context ctx) {

        mContext = ctx;
        progressDialog = new ProgressDialog(ctx);


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i(TAG, "Starting progress dialog");

        //show progress dialog while starting to log in
        progressDialog.setMessage("Logging in, Please wait");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        String email = params[0];
        String password = params[1];

        initConnection();

        //checking if connected to internet
        //later of check wifi or mobile data preference
        if (isConnectedToInternet) {
            try {

                Log.i(TAG, "Creating http Connection");

                //configuring URL for request

                URL url = new URL(AppConfig.getLoginApiUrl(mContext));

                Log.i(TAG, "Url : " + url.toString());


                Uri.Builder uriBuilder = new Uri.Builder()
                        .appendQueryParameter("REQUEST_TYPE", "LOGIN")
                        .appendQueryParameter("email", email)
                        .appendQueryParameter("password", password);
                String query = uriBuilder.build().getEncodedQuery();

                //creating HttpConnection
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setConnectTimeout(10000);
                con.setRequestMethod("POST");
                con.setChunkedStreamingMode(0);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                con.connect();

                int responseCode = con.getResponseCode();
                if (responseCode == 200) {
                    Log.i(TAG, "Response code OK");
                    this.isSuccess = true;
                    InputStream mInputStream = new BufferedInputStream(con.getInputStream());
                    jsonResponse = convertStreamToString(mInputStream);
                    mInputStream.close();
                } else {
                    Log.i(TAG, "Response not OK");
                    this.isSuccess = false;
                    this.errorMessage = "Oops! Error occurred, Try again later";
                }

                con.disconnect();


            } catch (IOException e) {
                this.isSuccess = false;
                this.errorMessage = "Internal Error! Try again later";
                e.printStackTrace();
            }


        } else {
            this.isSuccess = false;
            this.errorMessage = "No Internet Connection";
        }


        return jsonResponse;
    }


    @Override
    protected void onPostExecute(String s) {

        String status, username;
        int userId;
        //if everything is ok
        if (this.isSuccess) {
            Log.i(TAG, "Login Successful");

            try {
                System.out.println(TAG + " " + s);
                JSONObject responseObj = new JSONObject(s);
                status = responseObj.getString("status");
                if (status.equals("success")) {
                    Log.i(TAG, "Login Response Successful");
                    userId = responseObj.getInt("userid");
                    username = responseObj.getString("username");

                    //maintaining Session here
                    SessionManager sessionManager = new SessionManager(mContext);
                    sessionManager.login(username, userId);

                    //launching profile activity
                    Intent intent = new Intent(mContext, MyProfile.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                } else {
                    Log.i(TAG, "Login Response not successful");
                    errorMessage = responseObj.getString("error");
                    Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                Toast.makeText(mContext, "Cannot parse response! Try again later", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Login unsuccessful");
            Toast.makeText(mContext, this.errorMessage, Toast.LENGTH_SHORT).show();
        }

        progressDialog.dismiss();
    }


    /* Convert returned Http Response to String
    public String convertStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        try {
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            inputStream.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /*
    * Checks for internet connection availability
    public void initConnection() {
        NetworkConnection networkConnection = new NetworkConnection(mContext);
        isConnectedToInternet = networkConnection.checkConnectionStatus();
        isConnectedToWifi = networkConnection.getWifiConnectionStatus();
        isConnectedToMobile = networkConnection.getMobileConnectionStatus();
    }

}

*/
}
