package csd.jt.capsmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class VolProfileFragment extends Fragment {

    private ProgressDialog pDialog;
    ImageView img;
    TextView nameTv, emailTv, dobTv, pointsTv;

    // URL to get contacts JSON
    private static String url = "http://10.0.2.2/android/find-vol.php";

    // JSON Node names
    private static final String TAG_VOLUNTEER = "volunteer";
    private static final String TAG_FIRSTNAME = "firstname";
    private static final String TAG_LASTNAME = "lastname";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_STREET = "street";
    private static final String TAG_ZIPCODE = "zipcode";
    private static final String TAG_CITY = "city";
    private static final String TAG_DOB = "dob";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_POINTS = "points";


    // contacts JSONArray
    JSONArray completed = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> dataList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_vol_profile, container, false);

        img = (ImageView) rootView.findViewById(R.id.volProfileImg);
        nameTv = (TextView) rootView.findViewById(R.id.nameTv);
        emailTv = (TextView) rootView.findViewById(R.id.emailTv);
        dobTv = (TextView) rootView.findViewById(R.id.dobTv);
        pointsTv = (TextView) rootView.findViewById(R.id.pointsTv);

        dataList = new ArrayList<HashMap<String, String>>();

        // Calling async task to get json
        new GetData().execute();

        return rootView;
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("id", "1");

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, params);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    completed = jsonObj.getJSONArray(TAG_VOLUNTEER);

                    // looping through All Contacts
                    for (int i = 0; i < completed.length(); i++) {
                        JSONObject c = completed.getJSONObject(i);

                        String firstname = c.getString(TAG_FIRSTNAME);
                        String lastname = c.getString(TAG_LASTNAME);
                        String username = c.getString(TAG_USERNAME);
                        String email = c.getString(TAG_EMAIL);
                        String phone = c.getString(TAG_PHONE);
                        String address = c.getString(TAG_ADDRESS);
                        String street = c.getString(TAG_STREET);
                        String zipcode = c.getString(TAG_ZIPCODE);
                        String city = c.getString(TAG_CITY);
                        String dob = c.getString(TAG_DOB);
                        String image = c.getString(TAG_IMAGE);
                        String points = c.getString(TAG_POINTS);

                        // tmp hashmap for single contact
                        HashMap<String, String> row = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        row.put(TAG_FIRSTNAME, firstname);
                        row.put(TAG_LASTNAME, lastname);
                        row.put(TAG_USERNAME, username);
                        row.put(TAG_EMAIL, email);
                        row.put(TAG_PHONE, phone);
                        row.put(TAG_ADDRESS, address);
                        row.put(TAG_STREET, street);
                        row.put(TAG_ZIPCODE, zipcode);
                        row.put(TAG_CITY, city);
                        row.put(TAG_DOB, dob);
                        row.put(TAG_IMAGE, image);
                        row.put(TAG_POINTS, points);

                        // adding contact to contact list
                        dataList.add(row);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            img.setImageURI(Uri.parse("http://10.0.2.2/" + dataList.get(0).get(TAG_IMAGE)));
            String name = dataList.get(0).get(TAG_FIRSTNAME) + " " + dataList.get(0).get(TAG_LASTNAME);
            nameTv.setText(nameTv.getText() + name);
            String email = dataList.get(0).get(TAG_EMAIL);
            emailTv.setText(emailTv.getText() + email);
            String dob = dataList.get(0).get(TAG_DOB);
            dobTv.setText(dobTv.getText() + dob);
            String points = dataList.get(0).get(TAG_POINTS);
            pointsTv.setText(pointsTv.getText() + points);

        }

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
