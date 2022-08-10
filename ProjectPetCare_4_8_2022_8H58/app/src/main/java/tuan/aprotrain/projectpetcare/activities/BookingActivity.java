package tuan.aprotrain.projectpetcare.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import tuan.aprotrain.projectpetcare.Adapter.ExpandLVCheckBox;
import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.Booking;
import tuan.aprotrain.projectpetcare.entity.Pet;
import tuan.aprotrain.projectpetcare.entity.Recycle;
import tuan.aprotrain.projectpetcare.entity.Service;

public class BookingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /*
      Phần khai báo cho adapter category
       */
    ExpandLVCheckBox listAdapter;
    ExpandableListView expListView;
    ArrayList<String> listCategory;
    HashMap<String, List<Service>> listService;
    /*
    Phần khai báo cho date and time
     */
    private TextView date_time_input;
    private Activity activity;
    private SimpleDateFormat simpleDateFormat;
    private Calendar calendar;
    //    DatePickerDialog.OnDateSetListener dateSetListener;
//    TimePickerDialog.OnTimeSetListener timeSetListener;
    /*
    Phần khai báo cho adapter của spinner chọn pet name và payment
     */
    private Spinner spinnerPetName;
    private Spinner spinnerPayment;
    private Spinner spinnerAddress;

    // code cua tuan
    private EditText notePet;

    private DatabaseReference reference;
    String startDate;
    TextView dateStart, dateEnd;
    Button btnSubmit;
    Recycle recycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        // tuan
        reference = FirebaseDatabase.getInstance().getReference();
        //selectedService = new ArrayList<>();
        notePet = findViewById(R.id.notePet);

        spinnerPetName = findViewById(R.id.spnPetName);
        ArrayAdapter<String> petNameAdapter = new ArrayAdapter<>(BookingActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, getListPetName());
        petNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPetName.setAdapter(petNameAdapter);
        spinnerPetName.setOnItemSelectedListener(this);

        spinnerPayment = findViewById(R.id.spnPayment);
        ArrayAdapter<CharSequence> paymentAdapter = ArrayAdapter.createFromResource(this, R.array.payment, android.R.layout.simple_spinner_item);
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPayment.setOnItemSelectedListener(this);
        spinnerPayment.setAdapter(paymentAdapter);

        spinnerAddress = findViewById(R.id.spnAddress);
        ArrayAdapter<CharSequence> addressAdapter = ArrayAdapter.createFromResource(this, R.array.adress, android.R.layout.simple_spinner_item);
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAddress.setOnItemSelectedListener(this);
        spinnerAddress.setAdapter(addressAdapter);
        recycle = new Recycle();
        //selectedService = new ArrayList<>();

        expListView = (ExpandableListView) findViewById(R.id.expandLV);
        prepareListData();
        listAdapter = new ExpandLVCheckBox(this, listCategory, listService);
        expListView.setAdapter(listAdapter);

        activity = this;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        date_time_input = (TextView) findViewById(R.id.appointment);
        date_time_input.setOnClickListener(textListener);

        dateStart = findViewById(R.id.appointment);
        dateEnd = findViewById(R.id.endDateHotel);


    /*
        Hàm của expandable listview checkbox
    */
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listCategory.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listCategory.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listCategory.get(groupPosition)
                                + " : "
                                + listService.get(
                                listCategory.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return true;
            }
        });



        btnSubmit = findViewById(R.id.btnSubmit);
        final TextView textView = (TextView) findViewById(R.id.price);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String petName = spinnerPetName.getSelectedItem().toString().trim();
                String timeStart = dateStart.getText().toString().trim();
                String timeEnd = dateEnd.getText().toString().trim();
                String payment = spinnerPayment.getSelectedItem().toString().trim();
                String note = notePet.getText().toString();
                float totalPrice=0;
                for(Service service : getCheckedService()){
                    totalPrice += service.getServicePrice();

                    System.out.println("list Service: " + service.getServiceName());
                }
                //textView.setText(""+totalPrice);
                getSelectedItem(petName, timeStart, timeEnd, payment, note,totalPrice);


                //getSelectedItem(petName, payment, note,selectedService);
//                for (String item : listAdapter.get)
                //count selected child item
//                int count = 0;
//                for (int mGroupPosition = 0; mGroupPosition < listAdapter.getGroupCount(); mGroupPosition++) {
//                    count = count + listAdapter.getNumberOfCheckedItemsInGroup(mGroupPosition);
//                    //list
//                }
                //textView.setText("" + count);


            }
        });

    }
//


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /* Code cua kien (phan date & time) */
    private final View.OnClickListener textListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            calendar = Calendar.getInstance();
            new DatePickerDialog(activity, mDateDataSet, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    };


    private final DatePickerDialog.OnDateSetListener mDateDataSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            new TimePickerDialog(activity, mTimeDataSet, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
        }
    };

    private final TimePickerDialog.OnTimeSetListener mTimeDataSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            date_time_input.setText(simpleDateFormat.format(calendar.getTime()));
            startDate = simpleDateFormat.format(calendar.getTime());
            long serviceTime =0;
            for(Service service : getCheckedService()){
                serviceTime += service.getServiceTime();

                System.out.println("list Service: " + service.getServiceName());
            }
            Recycle recycle = new Recycle();
            TextView endDateHotel = findViewById(R.id.endDateHotel);
            endDateHotel.setText(recycle.CalculateDate(startDate,serviceTime));
        }
    };

    // tuan
    public void getSelectedItem(String petName, String startDate, String endDate, String payment, String note, float totalPrice) {
        //public void getSelectedItem(String petName, String payment, String note,ArrayList<Service> selectedService) {
        Booking booking;
        if (petName.isEmpty() || payment.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            System.out.println("All field are required!");

        } else {
            ArrayList<Pet> petList = new ArrayList<>();
            reference.child("Pets").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot petSnapshot : snapshot.getChildren()) {
                        Pet pet = petSnapshot.getValue(Pet.class);
                        petList.add(pet);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            long petId = 0;

            for (Pet pet : petList) {
                if (pet.getPetName().equals(petName))
                    petId = pet.getPetId();
            }


            //selectedService.add((Service) listService.get(expListView.getCheckedItemPosition()));
            for(Service service : getCheckedService()){
                System.out.println("list Service: " + service.getServiceName());
            }



            booking = new Booking("#"+recycle.idHashcode(petName),
                    petId,
                    startDate,
                    endDate,
                    payment,
                    note,
                    totalPrice);

//            Intent intent = new Intent(getBaseContext(), BookingDetailActivity.class);
//            intent.putExtra("BOOKING", booking);
//            intent.putExtra("PET_NAME", petName);
//            //intent.putExtra("SERVICE_LIST",getCheckedService());
//            //intent.pu
//            startActivity(intent);
            //reference.child()
            openDialog(Gravity.BOTTOM,booking, petName);

        }
    }

    private List<String> getListPetName() {
        List<String> petNameList = new ArrayList<>();
        petNameList.add("Choose Pet");
        reference.child("Pets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //System.out.println("hello 1");
                for (DataSnapshot petSnapshot : snapshot.getChildren()) {
                    //Pet pet = petSnapshot.getValue(Pet.class);
                    petNameList.add(petSnapshot.child("petName").getValue(String.class));
                    //System.out.println("Service:"+serviceSnapshot.child("serviceName").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return petNameList;
    }

    private void prepareListData() {
        listCategory = new ArrayList<String>();
        listService = new HashMap<String, List<Service>>();
        //Intent pass data
        listCategory.add("Category");

        List<Service> list = new ArrayList<Service>();

        reference.child("Services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("hello 1");
                for (DataSnapshot serviceSnapshot : snapshot.getChildren()) {
//                    list.add(serviceSnapshot.child("serviceName").getValue(String.class)+"\t abc \t def");
                    list.add(serviceSnapshot.getValue(Service.class));
                    //System.out.println("Service:"+serviceSnapshot.child("serviceName").getValue(String.class));
                    //System.out.println("list service selected: " + selectedService);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listService.put(listCategory.get(0), list);
    }
    //
    public ArrayList<Service> getCheckedService(){
        ArrayList<Service> selectedService = new ArrayList<>();
        for (int mGroupPosition = 0; mGroupPosition < listAdapter.getGroupCount(); mGroupPosition++) {
            selectedService = listAdapter.getListCheckedChild(mGroupPosition);
            //list
        }
        return selectedService;
    }

    public void openDialog(int gravity, Booking booking, String petName){




        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.layout_dialog_booking);

        TextView petName_Details,startTimeTxt,endTimeTxt,totalPrice;
        petName_Details= dialog.findViewById(R.id.petName_Details);
        startTimeTxt= dialog.findViewById(R.id.startTimeTxt);
        endTimeTxt= dialog.findViewById(R.id.endTimeTxt);
        totalPrice= dialog.findViewById(R.id.totalPrice);
        ImageView qr_code = dialog.findViewById(R.id.qr_code);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        //set text
        generateQR(booking.getBookingId());
        System.out.println(booking.getBookingId());
        petName_Details.setText(petName);
        startTimeTxt.setText(booking.getBookingStartDate());
        endTimeTxt.setText(booking.getBookingEndDate());
        totalPrice.setText(""+booking.getTotalPrice());


        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(booking.getBookingId(), BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            qr_code.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        Button btnCancel = dialog.findViewById(R.id.btnCancelDialog);
        Button btnSend = dialog.findViewById(R.id.btnSend);

        Toast.makeText(this, "Dialog info:"+getCheckedService().get(1), Toast.LENGTH_SHORT).show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.show();
    }
    private void generateQR(String content) {
        //String content = editTextContent.getText().toString().trim();

    }

}
