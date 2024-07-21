package com.example.mofomas;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.mofomas.Fragments.Cart;
import com.example.mofomas.adapter.CartItem;
import com.example.mofomas.adapter.PopularItem;
import com.example.mofomas.adapter.CartManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class callNextScreenOTP extends AppCompatActivity {
    private String currentUserId;
    private static final String CHANNEL_ID = "MofomasChannel";
    private static final int NOTIFICATION_ID = 1;
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView notificationIcon;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private DatabaseReference usersRef;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_next_screenotp);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        currentUserId = getCurrentUserEmail();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView2);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        notificationIcon = findViewById(R.id.notificationIcon);
        notificationIcon.setOnClickListener(this::showProfileSettings);

        boolean isDarkMode = sharedPreferences.getBoolean("darkMode", false);
        AppCompatDelegate.setDefaultNightMode(isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        createNotificationChannel();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView2);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    public void addItemToCart(PopularItem item) {
        CartManager.getInstance(this).addItemToCart(currentUserId, new CartItem(item.getFoodName(), item.getFoodPrice(), item.getImageUrl(), 1));
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView2);
        if (fragment instanceof Cart) {
            ((Cart) fragment).refreshCartItems();
        } else {
            NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView2);
            navController.navigate(R.id.cart);
        }
        showAddToCartNotification(item);
    }

    private String getCurrentUserEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getEmail();
        } else {
            return "guest";
        }
    }

    public void logout() {
        CartManager.getInstance(this).clearCart(currentUserId);
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Mofomas Notifications";
            String description = "Notifications for Mofomas app";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showAddToCartNotification(PopularItem item) {
        Intent intent = new Intent(this, callNextScreenOTP.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.two)
                .setContentTitle("Item Added to Cart")
                .setContentText(item.getFoodName() + " has been added to your cart.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void showProfileSettings(View view) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_profile_settings, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        CircleImageView profileImageView = popupView.findViewById(R.id.profileImageView);
        MaterialButton changeProfileImageButton = popupView.findViewById(R.id.changeProfileImageButton);
        TextInputEditText newPasswordInput = popupView.findViewById(R.id.newPasswordInput);
        TextInputEditText confirmPasswordInput = popupView.findViewById(R.id.confirmPasswordInput);
        MaterialButton updatePasswordButton = popupView.findViewById(R.id.saveButton);
        SwitchMaterial darkModeSwitch = popupView.findViewById(R.id.darkModeSwitch);
        MaterialButton logoutButton = popupView.findViewById(R.id.logoutButton);

        loadProfileImage(profileImageView);

        changeProfileImageButton.setOnClickListener(v -> openImageChooser());

        darkModeSwitch.setChecked(isDarkModeEnabled());
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> setDarkMode(isChecked));

        updatePasswordButton.setOnClickListener(v -> {
            String newPassword = newPasswordInput.getText().toString();
            String confirmPassword = confirmPasswordInput.getText().toString();
            if (!newPassword.isEmpty() && newPassword.equals(confirmPassword)) {
                updatePassword(newPassword);
            } else if (!newPassword.isEmpty()) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        });

        logoutButton.setOnClickListener(v -> {
            logout();
            popupWindow.dismiss();
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadProfileImage(imageUri);
        }
    }

    private void uploadProfileImage(Uri imageUri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && imageUri != null) {
            String userId = user.getUid();
            StorageReference profileImageRef = storageRef.child("profile/" + userId + ".jpg");
            profileImageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(this, "Profile image uploaded successfully", Toast.LENGTH_SHORT).show();
                        loadProfileImage(findViewById(R.id.profileImageView));
                        profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            usersRef.child(userId).child("profilePicUrl").setValue(uri.toString())
                                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to update profile image URL", Toast.LENGTH_SHORT).show());
                        });
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload profile image", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "User not authenticated or image not selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProfileImage(CircleImageView imageView) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && imageView != null && !isDestroyed() && !isFinishing()) {
            String userId = user.getUid();
            StorageReference profileImageRef = storageRef.child("profile/" + userId + ".jpg");
            profileImageRef.getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        if (!isDestroyed() && !isFinishing() && imageView != null) {
                            Glide.with(this)
                                    .load(uri)
                                    .error(R.drawable.food1)
                                    .into(imageView);
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!isDestroyed() && !isFinishing() && imageView != null) {
                            imageView.setImageResource(R.drawable.food1);
                        }
                    });
        } else if (imageView != null) {
            imageView.setImageResource(R.drawable.food1);
        }
    }

    private boolean isDarkModeEnabled() {
        return sharedPreferences.getBoolean("darkMode", false);
    }

    private void setDarkMode(boolean enabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("darkMode", enabled);
        editor.apply();

        AppCompatDelegate.setDefaultNightMode(enabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        recreate();
    }

    private void updatePassword(String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            usersRef.child(user.getUid()).child("Password").setValue(newPassword)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(this, "Failed to update password in database", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}