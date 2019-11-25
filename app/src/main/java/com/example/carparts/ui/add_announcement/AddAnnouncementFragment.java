package com.example.carparts.ui.add_announcement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.example.carparts.SharedPrefManager.isLoggedIn;

public class AddAnnouncementFragment extends Fragment {
    private static final int RESULT_LOAD_IMAGE = 1;
    //    private static final String SERVER_ADRESS = "masticable-stapler.000webhostapp.com";

    private static final String SERVER_ADRESS = "http://13.80.137.25/android/";
    protected int userID;

    private AddAnnouncementViewModel addAnnouncementViewModel;

    private EditText editTextTitle, editTextDescription, editTextPrice;
    private ImageView imageViewUploadImage;
    private Button buttonUploadImage, buttonAddAnnouncement;
    private String namePhoto = "photo";
    private static int NUM_PHOTO = 0;
    private UploadImage uploadImage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addAnnouncementViewModel = ViewModelProviders.of(this).get(AddAnnouncementViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_announcement, container, false);

        editTextTitle = (EditText) root.findViewById(R.id.et_title_add_ann);
        editTextDescription = (EditText) root.findViewById(R.id.et_description_add_ann);
        editTextPrice = (EditText) root.findViewById(R.id.et_price_add_ann);
        imageViewUploadImage = (ImageView) root.findViewById(R.id.im_add);
        buttonUploadImage = (Button) root.findViewById(R.id.btn_add_image);
        buttonAddAnnouncement = (Button) root.findViewById(R.id.btn_add_ann);


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
////                uploadImage = new UploadImage(image, namePhoto);
//                new UploadImage(image, namePhoto).execute();
////                uploadImage.execute();
                uploadImage = new UploadImage(image, namePhoto);
                uploadImage.execute();
                addAnnouncement(uploadImage.getPhotoName());
               // addAnnouncement();
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageViewUploadImage.setImageURI(selectedImage);
        }
    }


    private class UploadImage extends AsyncTask<Void, Void, Void> {
        Bitmap image;
        String name;
        int number;

        public UploadImage(Bitmap image, String name) {
            //TODO: do usuniecia i ostatni z bazy
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
            HttpPost post = new HttpPost(SERVER_ADRESS +"SavePicture.php");

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

        if(SharedPrefManager.getInstance(getActivity().getApplicationContext()).isLoggedIn()){
             userID = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser().getId();
        }

        class AddAnnouncement extends AsyncTask<Void, Void, String> {

//            private ProgressBar progressBar;


            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                String user_id=Integer.toString(userID);
                System.out.println("UserID"+ user_id);
                System.out.println("Namephoto" + name_photo);
                System.out.println("price " + price);
                System.out.println("title " + title);
                System.out.println("description " + description);
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("description", description);
                params.put("price", price);
                params.put("name_photo", name_photo+".jpg");
                params.put("user_id", user_id);

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