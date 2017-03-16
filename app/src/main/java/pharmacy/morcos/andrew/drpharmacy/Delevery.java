package pharmacy.morcos.andrew.drpharmacy;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import pharmacy.morcos.andrew.drpharmacy.Adapters.Delevery_Adapter;
import pharmacy.morcos.andrew.drpharmacy.Adapters.NEWS_Adapter;
import pharmacy.morcos.andrew.drpharmacy.Data.data_delevery;
import pharmacy.morcos.andrew.drpharmacy.Data.data_news;

public class Delevery extends AppCompatActivity {

    Firebase myFirebase;
    DataSnapshot myChild;
    RelativeLayout relativeLayout;

    ArrayList<data_delevery> DataArray;
    Delevery_Adapter deleveryAdapter;
    ListView listViewDelevery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delevery);

        myFirebase.setAndroidContext(this);
        DataArray = new ArrayList<>();

        myFirebase = new Firebase(getResources().getString(R.string.firebaseLink));

        listViewDelevery = (ListView) findViewById(R.id.deleveryListView);
        relativeLayout = (RelativeLayout) findViewById(R.id.progressBarDelevery);

        listViewDelevery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Query queryRef = myFirebase.child("SignIn").child(DataArray.get(position).getPersonId());
                queryRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot snapshot) {

                        Intent intent = new Intent(Delevery.this, OrderDetails.class);
                        intent.putExtra("name", snapshot.child("Name").getValue().toString());
                        intent.putExtra("address", snapshot.child("Address").getValue().toString());
                        intent.putExtra("mobile", snapshot.child("Mobile").getValue().toString());
                        intent.putExtra("phone", snapshot.child("Phone").getValue().toString());
                        intent.putExtra("orderId", DataArray.get(position).getDeleveryKey());
                        myFirebase.child("Delivery").child(DataArray.get(position).getDeleveryKey()).child("Seen").setValue("1");

                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(FirebaseError error) {
                    }
                });
            }
        });

        listViewDelevery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final CharSequence[] items = {
                        "Remove"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Delevery.this);
                builder.setTitle("Choose Action ..");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Remove")) {

                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    postData(i);
                                }
                            });
                            t.start();
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.e("noline", isOnline() + "");
        // DataArray = new ArrayList<>();

        Query queryRef = myFirebase.child("Delivery");
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {
                DataArray.clear();
                long size = snapshot.getChildrenCount();
                Iterable<DataSnapshot> myChildren = snapshot.getChildren();

                while (myChildren.iterator().hasNext()) {
                    int i = 0;

                    myChild = myChildren.iterator().next();
                    try {
                        Boolean seen;
                        if (myChild.child("Seen").getValue().toString().equals("0"))
                            seen = false;
                        else
                            seen = true;
                        DataArray.add(0, new data_delevery(myChild.child("Sender").getValue().toString(),
                                myChild.child("Time").getValue().toString(),
                                "new",
                                Long.toString(myChild.child("Messages").getChildrenCount()),
                                myChild.getKey().toString(),
                                myChild.child("PersonID").getValue().toString(), seen));
                    } catch (Exception e) {
                    }
                    i++;
                }
                deleveryAdapter = new Delevery_Adapter(DataArray, Delevery.this);
                listViewDelevery.setAdapter(deleveryAdapter);
                relativeLayout.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });

    }

    public Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public void postData(int pos) {
        myFirebase.child("Delivery").child(DataArray.get(pos).getDeleveryKey()).removeValue();
    }
}
