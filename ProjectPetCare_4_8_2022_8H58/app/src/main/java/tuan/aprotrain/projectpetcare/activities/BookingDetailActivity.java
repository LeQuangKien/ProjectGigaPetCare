package tuan.aprotrain.projectpetcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.Booking;
import tuan.aprotrain.projectpetcare.entity.Image;
import tuan.aprotrain.projectpetcare.entity.Recycle;

public class BookingDetailActivity extends AppCompatActivity {
    ImageView qr_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        qr_code = findViewById(R.id.qr_code);

        Recycle recycle = new Recycle();
        String petName = getIntent().getStringExtra("PET_NAME");
        Booking bookingData = (Booking) getIntent().getSerializableExtra("BOOKING");
        Booking booking = new Booking();

        booking.setBookingId(recycle.idHashcode(petName));

        System.out.println(booking.getBookingId());
        generateQR(booking.getBookingId());

        System.out.println(bookingData.getPetId());
        System.out.println(bookingData.getBookingStartDate());
        System.out.println(bookingData.getBookingEndDate());
        System.out.println(bookingData.getPayment());
        System.out.println(bookingData.getNotes());
    }
    private void generateQR(String content) {
        //String content = editTextContent.getText().toString().trim();
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            qr_code.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}