package com.example.mofomas;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class callNextScreenOTP extends AppCompatActivity {

    private String currentUserId; // Add this to manage current user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_next_screenotp);

        // Retrieve current user email
        currentUserId = getCurrentUserEmail();

        // Find the NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView2);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        // Set up bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView2);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    // Method to handle adding item to cart from Home fragment
    public void addItemToCart(PopularItem item) {
        // Use CartManager to add item to the cart for the current user and persist it
        CartManager.getInstance(this).addItemToCart(currentUserId, new CartItem(item.getFoodName(), item.getFoodPrice(), item.getImageUrl(), 1));

        // Find the current fragment
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView2);

        if (fragment instanceof Cart) {
            // If the Cart fragment is visible, refresh the cart items
            ((Cart) fragment).refreshCartItems();
        } else {
            // If the Cart fragment is not visible, navigate to it without adding the item again
            NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView2);
            navController.navigate(R.id.cart); // Ensure this ID matches your navigation graph
        }
    }

    // Method to get the current user email
    private String getCurrentUserEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getEmail();
        } else {
            return "guest"; // Fallback or handle the case when user is not logged in
        }
    }

    // Method to clear cart items on logout
    public void logout() {
        // Clear the cart items for the current user
        CartManager.getInstance(this).clearCart(currentUserId);

        // Perform logout operations here (e.g., sign out from FirebaseAuth)
        FirebaseAuth.getInstance().signOut();

        // Redirect to login screen or perform other necessary actions
    }
}
