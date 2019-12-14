package com.example.carparts.ui.add_announcement;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.carparts.R;
import com.example.carparts.RequestHandler;
import com.example.carparts.SharedPrefManager;
import com.example.carparts.URLs;
import com.example.carparts.config.Config;
import com.google.android.material.navigation.NavigationView;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.example.carparts.SharedPrefManager.isLoggedIn;

public class AddAnnouncementFragment extends Fragment {
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static final int COST_ANNOUNCEMENT = 10;
    //String amount_month = "";
    int sum;
    double cost;
        private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    private static final String SERVER_ADRESS = "http://13.80.137.25/android/";
    protected int userID;

    private AddAnnouncementViewModel addAnnouncementViewModel;

    private EditText editTextTitle, editTextDescription, editTextPrice, et_how_month_add_ann;
    private TextView tv_cost_add_ann;
    private ImageView imageViewUploadImage;
    private Button buttonUploadImage, buttonAddAnnouncement;
    private String namePhoto = "photo";
    private static int NUM_PHOTO = 0;
    private UploadImage uploadImage;

    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addAnnouncementViewModel = ViewModelProviders.of(this).get(AddAnnouncementViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_announcement, container, false);

        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);

        editTextTitle = (EditText) root.findViewById(R.id.et_title_add_ann);
        editTextDescription = (EditText) root.findViewById(R.id.et_description_add_ann);
        editTextPrice = (EditText) root.findViewById(R.id.et_price_add_ann);
        imageViewUploadImage = (ImageView) root.findViewById(R.id.im_add);
        buttonUploadImage = (Button) root.findViewById(R.id.btn_add_image);
        buttonAddAnnouncement = (Button) root.findViewById(R.id.btn_add_ann);
        tv_cost_add_ann = (TextView) root.findViewById(R.id.tv_cost_add_ann);

        et_how_month_add_ann = (EditText) root.findViewById(R.id.et_how_month_add_ann);

        tv_cost_add_ann.setEnabled(false);
        tv_cost_add_ann.setText("Total cost: " + 0 + "zł");

        et_how_month_add_ann.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                System.out.println("Sumka" + sum);
                if (!et_how_month_add_ann.getText().toString().equals("")) {
                    sum = Integer.valueOf(et_how_month_add_ann.getText().toString());
                    if (sum >= 0 && sum < 3) {
                        cost = COST_ANNOUNCEMENT * sum * 0.9;
                        cost = Math.round(cost * 100.) / 100.;
                    }
                    if (sum >= 3 && sum < 6) {
                        cost = COST_ANNOUNCEMENT * sum * 0.85;
                        cost = Math.round(cost * 100.) / 100.;
                    }
                    if (sum >= 6 && sum < 9) {
                        cost = COST_ANNOUNCEMENT * sum * 0.80;
                        cost = Math.round(cost * 100.) / 100.;
                    }
                    if (sum >= 9 && sum < 12) {
                        cost = COST_ANNOUNCEMENT * sum * 0.75;
                        cost = Math.round(cost * 100.) / 100.;
                    }
                    if (sum >= 12) {
                        cost = COST_ANNOUNCEMENT * sum * 0.60;
                        cost = Math.round(cost * 100.) / 100.;
                    }
                    System.out.println("Obliczylem hajs!");
                    tv_cost_add_ann.setEnabled(true);
                    tv_cost_add_ann.setText("Total cost: " + cost + "zł");
                }
                else{

                    tv_cost_add_ann.setText("Total cost: " + 0 + "zł");
                    tv_cost_add_ann.setEnabled(false);
                }
            }



            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Wypisuje zmiane hajsu w ogloszeniach");
            }
        });




        root.findViewById(R.id.btn_add_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });


        root.findViewById(R.id.btn_add_ann).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap image = ((BitmapDrawable) imageViewUploadImage.getDrawable()).getBitmap();
                uploadImage = new UploadImage(image, namePhoto);
                processPayment();
                uploadImage.execute();
            }
        });
        return root;
    }

    private void processPayment() {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(cost)), "PLN", "Announcement", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageViewUploadImage.setImageURI(selectedImage);
        }
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    addAnnouncement(uploadImage.getPhotoName());
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(getActivity(), "Invalid", Toast.LENGTH_SHORT).show();

        }
    }


    private class UploadImage extends AsyncTask<Void, Void, Void> {
        Bitmap image;
        String name;
        int number;

        public UploadImage(Bitmap image, String name) {
            number = ++NUM_PHOTO;
            this.image = image;
            this.name = name + number;

        }

        public String getPhotoName() {
            return name;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("image", encodedImage));
            dataToSend.add(new BasicNameValuePair("name", name));

            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADRESS + "SavePicture.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getActivity().getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
            System.out.println("wyslalem image upload");
        }
    }

    private HttpParams getHttpRequestParams() {
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
        return httpRequestParams;
    }


    private void addAnnouncement(String n) {

        final String title = editTextTitle.getText().toString().trim();
        final String description = editTextDescription.getText().toString().trim();
        final String price = editTextPrice.getText().toString().trim();
        final String name_photo = n;
        final String amount_month = et_how_month_add_ann.getText().toString().trim();
        //int userID;

        if (TextUtils.isEmpty(title)) {
            editTextTitle.setError("Please enter title");
            editTextTitle.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(description)) {
            editTextDescription.setError("Please enter description");
            editTextDescription.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(price)) {
            editTextPrice.setError("Please enter a price");
            editTextPrice.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(amount_month)) {
            et_how_month_add_ann.setError("Please enter a amount month");
            et_how_month_add_ann.requestFocus();
            return;
        }

        if (SharedPrefManager.getInstance(getActivity().getApplicationContext()).isLoggedIn()) {
            userID = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser().getId();
        }

        class AddAnnouncement extends AsyncTask<Void, Void, String> {

//            private ProgressBar progressBar;


            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                String user_id = Integer.toString(userID);
                System.out.println("UserID" + user_id);
                System.out.println("Namephoto" + name_photo);
                System.out.println("price " + price);
                System.out.println("title " + title);
                System.out.println("description " + description);
                System.out.println("amount_month " + amount_month);
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();

                params.put("title", title);
                params.put("description", description);
                params.put("price", price);
                params.put("name_photo", name_photo + ".jpg");
                params.put("user_id", user_id);
                params.put("amount_month", amount_month);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_ADDANNOUNCEMENT, params);
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
//                progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object

                    System.out.println("Jestem w addAnouncement przed JSONEM");
                    System.out.println("response: " + s);
                    JSONObject obj = new JSONObject(s);

                    System.out.println("Jestem w addAnouncement po JSONEM");
//                    if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//

                        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                        navigationView.getMenu().performIdentifierAction(R.id.nav_all_announcement, 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        AddAnnouncement an = new AddAnnouncement();
        an.execute();

    }


}