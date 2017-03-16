package pharmacy.morcos.andrew.drpharmacy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

public class OrderDetails extends AppCompatActivity {

    TextView Name, Address, Mobile, phone;
    ImageView imageViewOrder, imageViewMobileCall, imageViewPhoneCall;
    LinearLayout linearLayout;
    Firebase myFirebase;
    DataSnapshot myChildMessages, myChildImages;
    HashMap<Integer, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        myFirebase = new Firebase(getResources().getString(R.string.firebaseLink));

        Name = (TextView) findViewById(R.id.textViewNameOrderDetails);
        Address = (TextView) findViewById(R.id.textViewAddressOrderDetails);
        Mobile = (TextView) findViewById(R.id.textViewMobileOrderDetails);
        phone = (TextView) findViewById(R.id.textViewPhoneOrderDetails);
        imageViewOrder = (ImageView) findViewById(R.id.imageOrderDetails);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutOrderDeails);
        imageViewOrder.setTag(0);
        imageViewMobileCall = (ImageView) findViewById(R.id.imageMobileCallOrderDetails);
        imageViewPhoneCall = (ImageView) findViewById(R.id.imagePhoneCallOrderDetails);


        Intent intent = getIntent();
        getMessages(intent.getStringExtra("orderId"));
        Name.setText(intent.getStringExtra("name"));
        Address.setText(intent.getStringExtra("address"));
        Mobile.setText(intent.getStringExtra("mobile"));
        phone.setText(intent.getStringExtra("phone"));

        final String mobile = intent.getStringExtra("mobile");
        final String phone = intent.getStringExtra("phone");

        imageViewMobileCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobile));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        imageViewPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }

    private void getMessages(String deleveryId) {
        Query queryRef = myFirebase.child("Delivery").child(deleveryId);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {

                Iterable<DataSnapshot> myChildrenMessages = snapshot.child("Messages").getChildren();
                Iterable<DataSnapshot> myChildrenImages = snapshot.child("Images").getChildren();
                int i = 0;
                hashMap = new HashMap<Integer, String>();
                while (myChildrenMessages.iterator().hasNext()) {

                    myChildMessages = myChildrenMessages.iterator().next();
                    myChildImages = myChildrenImages.iterator().next();


                    View view = new View(getBaseContext());
                    LinearLayout.LayoutParams paramsV = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            3
                    );
                    paramsV.setMargins(0, 50, 0, 50);
                    view.setLayoutParams(paramsV);

                    view.setBackgroundColor(Color.parseColor("#FFFFFF"));



                    TextView textView = new TextView(getBaseContext());
                    textView.setText(myChildMessages.getValue().toString());
                    textView.setTextColor(Color.parseColor("#FFFFFF"));

                    ImageView imageView = new ImageView(getBaseContext());
                    final int finalI = i;


                    if (!myChildImages.getValue().toString().equals("No Image")) {
                        hashMap.put(finalI, myChildImages.getValue().toString());
                        String photo = myChildImages.getValue().toString();
                        byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageView.setImageBitmap(decodedByte);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String photo = hashMap.get(finalI);
                                byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                imageViewOrder.setImageBitmap(decodedByte);
                                imageViewOrder.setVisibility(View.VISIBLE);
                                imageViewOrder.setTag(1);
                            }
                        });
                    } else
                        imageView.setImageResource(R.mipmap.ic_launcher);

                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT);
                    linearLayout.addView(view);
                    linearLayout.addView(imageView, params);
                    linearLayout.addView(textView);

                    Log.e("getv", myChildMessages.getValue().toString() + " " + myChildImages.getValue().toString());

                    i++;
                }

            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (imageViewOrder.getTag().equals(1)) {
            imageViewOrder.setVisibility(View.INVISIBLE);
            imageViewOrder.setTag(0);
        } else
            super.onBackPressed();
    }
}