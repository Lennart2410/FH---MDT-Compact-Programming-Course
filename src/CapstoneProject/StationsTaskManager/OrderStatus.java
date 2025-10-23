package CapstoneProject.StationsTaskManager;

public enum OrderStatus {
    ORDERED,     // just created
    PICKING,     // being picked
    PACKING,     // being packed
    LOADING,     // being loaded to vehicle
    LOADED,      // finished successfully
    EXCEPTION    // failed (e.g., out of stock)
}
