package lk.ijse.Green_Shadow_Backend.utils;

public class GenerateID {
    public static String generateId(String prefix, int number) {
        return String.format("%s-%03d", prefix, number);
    }
}