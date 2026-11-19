package com.mycompany.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

// =============================================================
//  LOGIN CLASS — Part 1
// =============================================================

/**
 * Login class for the Chat Application - Part 1: Registration and Login Feature.
 *
 * This class handles:
 * - Username validation (contains underscore, max 5 characters)
 * - Password complexity validation
 * - South African cell phone number validation using regex
 * - User registration (storing credentials)
 * - User login (verifying credentials)
 * - Returning appropriate login status messages
 *
 * Coding Standard: camelCase variables, single-purpose methods,
 * full Javadoc and inline comments, no redundant code.
 *
 * @author Mc Chopper
 * @version 1.0
 */
class Login {

    // Stores the user's first name (used in the welcome message)
    private String firstName;

    // Stores the user's last name (used in the welcome message)
    private String lastName;

    // Stores the registered username after successful registration
    private String registeredUsername;

    // Stores the registered password after successful registration
    private String registeredPassword;

    // Stores the registered cell phone number after successful registration
    private String registeredCellPhone;

    /**
     * Constructor — initialises the Login object with the user's first and last name.
     *
     * @param firstName The user's first name.
     * @param lastName  The user's last name.
     */
    public Login(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Checks that the username meets the following conditions:
     * - Contains an underscore character (_)
     * - Is no more than five characters long
     *
     * @param username The username to validate.
     * @return true if valid, false otherwise.
     */
    public boolean checkUserName(String username) {
        // Return false immediately if username is null
        if (username == null) {
            return false;
        }

        // Check if username contains an underscore
        boolean hasUnderscore = username.contains("_");

        // Check if username is no more than five characters (not exactly 5)
        boolean correctLength = username.length() <= 5;

        // Both conditions must be true
        return hasUnderscore && correctLength;
    }

    /**
     * Checks that the password meets the following complexity rules:
     * - At least eight characters long
     * - Contains at least one capital (uppercase) letter
     * - Contains at least one number (digit)
     * - Contains at least one special character
     *
     * @param password The password to validate.
     * @return true if password meets all rules, false otherwise.
     */
    public boolean checkPasswordComplexity(String password) {
        // Return false if password is null or too short
        if (password == null || password.length() < 8) {
            return false;
        }

        // Flags to track which rules are met
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        // Loop through every character in the password
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                // Found an uppercase letter
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                // Found a digit
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                // Found a special character (not a letter or digit)
                hasSpecial = true;
            }
        }

        // All three rules must be satisfied
        return hasCapital && hasNumber && hasSpecial;
    }

    /**
     * Checks that the cell phone number is correctly formatted using regex.
     *
     * The number must:
     * - Start with the international country code prefix '+'
     * - Be followed by 9 to 11 digits
     * - Example valid SA number: +27838968976
     *
     * ATTRIBUTION:
     * The regex pattern was researched and adapted from:
     * - Java Regex documentation: https://www.w3schools.com/java/java_regex.asp
     * - SA number format reference:
     *   https://quickblox.com/blog/beginners-guide-to-chat-app-architecture/
     *
     * Pattern: ^\+[0-9]{9,11}$
     *   ^      = start of string
     *   \+     = literal '+' (international code prefix)
     *   [0-9]  = any digit
     *   {9,11} = between 9 and 11 digits
     *   $      = end of string
     *
     * @param cellPhoneNumber The cell phone number to validate.
     * @return true if correctly formatted, false otherwise.
     */
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        // Return false if null
        if (cellPhoneNumber == null) {
            return false;
        }

        // Regex: must start with '+' followed by 9 to 11 digits
        String regex = "^\\+[0-9]{9,11}$";

        return cellPhoneNumber.matches(regex);
    }

    /**
     * Registers the user by validating username, password, and cell phone.
     * Stores credentials if all three pass validation.
     *
     * @param username        The desired username.
     * @param password        The desired password.
     * @param cellPhoneNumber The user's cell phone number.
     * @return A message indicating the result of registration.
     */
    public String registerUser(String username, String password, String cellPhoneNumber) {
        // Validate username — stop and return error if invalid
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username "
                 + "contains an underscore and is no more than five characters in length.";
        }

        // Validate password — stop and return error if invalid
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password "
                 + "contains at least eight characters, a capital letter, a number, and a special character.";
        }

        // Validate cell phone — stop and return error if invalid
        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell phone number incorrectly formatted or does not contain international code; "
                 + "please correct the number and try again.";
        }

        // All validations passed — store the credentials
        this.registeredUsername = username;
        this.registeredPassword = password;
        this.registeredCellPhone = cellPhoneNumber;

        // Return all three success messages
        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
    }

    /**
     * Verifies that the entered username and password match the stored credentials.
     *
     * @param username The username entered at login.
     * @param password The password entered at login.
     * @return true if credentials match, false otherwise.
     */
    public boolean loginUser(String username, String password) {
        // Cannot login if no user is registered
        if (registeredUsername == null || registeredPassword == null) {
            return false;
        }

        // Compare entered credentials with stored credentials
        return registeredUsername.equals(username) && registeredPassword.equals(password);
    }

    /**
     * Returns the appropriate login status message.
     * On success: greets user by their first and last name.
     * On failure: prompts user to try again.
     *
     * @param username The username entered at login.
     * @param password The password entered at login.
     * @return The login status message string.
     */
    public String returnLoginStatus(String username, String password) {
        // Use loginUser() to verify credentials
        if (loginUser(username, password)) {
            // Success — personalised welcome message with full name
            return "Welcome " + firstName + " " + lastName + " it is great to see you.";
        } else {
            // Failure — prompt to try again
            return "Username or password incorrect, please try again.";
        }
    }

    // ---- Getters ----

    /** @return The user's first name. */
    public String getFirstName() { return firstName; }

    /** @return The user's last name. */
    public String getLastName() { return lastName; }

    /** @return The registered username. */
    public String getRegisteredUsername() { return registeredUsername; }

    /** @return The registered cell phone number. */
    public String getRegisteredCellPhone() { return registeredCellPhone; }
}


// =============================================================
//  MESSAGE CLASS — Part 2 + Part 3
// =============================================================

/**
 * Message class for the Chat Application - Part 2 and Part 3.
 *
 * Part 2 (unchanged features):
 * - Generating a unique 10-digit Message ID
 * - Validating the recipient cell phone number
 * - Validating message length (max 250 characters)
 * - Creating a Message Hash
 * - Sending, storing, or discarding a message
 * - Printing all sent messages and returning the total sent
 *
 * Part 3 (new features):
 * - Arrays for Sent, Disregarded and Stored messages, Message Hashes and Message IDs
 * - Storing messages in a JSON file and reading that file back into an array
 * - Display sender/recipient, longest message, search by ID, search by recipient,
 *   delete by hash and a report of all stored messages
 *
 * ATTRIBUTION (JSON file reading and writing):
 * Google Gson library - https://github.com/google/gson/blob/main/UserGuide.md
 *
 * Coding Standard: camelCase variables, single-purpose methods,
 * full Javadoc and inline comments, no redundant code.
 *
 * @author Mc Chopper
 * @version 2.0
 */
class Message {

    // Unique 10-digit randomly generated message ID for this message
    private String messageID;

    // Tracks this message's number in the sequence (auto-incremented)
    private int numMessagesSent;

    // The recipient's cell phone number
    private String recipient;

    // The message text content (max 250 characters)
    private String messageText;

    // The auto-generated message hash for this message
    private String messageHash;

    // ---------------------------------------------------------
    // PART 3 ARRAYS (ArrayLists are resizable arrays)
    // ---------------------------------------------------------

    // Contains all the messages sent
    public static ArrayList<String> sentMessageList = new ArrayList<>();

    // Contains all the messages that were disregarded
    public static ArrayList<String> disregardedMessageList = new ArrayList<>();

    // Contains all the messages that were stored (also read from the JSON file)
    public static ArrayList<String> storedMessageList = new ArrayList<>();

    // Contains all the message hashes
    public static ArrayList<String> messageHashList = new ArrayList<>();

    // Contains all the message IDs
    public static ArrayList<String> messageIdList = new ArrayList<>();

    // Parallel arrays: the same position (index) in each list is the same message
    public static ArrayList<String> senderList = new ArrayList<>();
    public static ArrayList<String> recipientList = new ArrayList<>();
    public static ArrayList<String> messageTextList = new ArrayList<>();
    public static ArrayList<String> flagList = new ArrayList<>();   // Sent / Disregarded / Stored

    // Name of the JSON file used to keep the stored messages
    public static final String JSON_FILE = "storedMessages.json";

    // The sender of the messages (the logged-in user's cell number)
    private static String sender = "Unknown";

    // Counts the total number of messages sent in this session
    private static int totalMessagesSent = 0;

    // Auto-increments to number each message in the sequence
    private static int messageCounter = 0;

    /**
     * Constructor — creates a new Message and auto-generates the Message ID.
     *
     * @param recipient   The recipient's cell phone number.
     * @param messageText The message text content.
     */
    public Message(String recipient, String messageText) {
        this.recipient = recipient;
        this.messageText = messageText;

        // Auto-generate the unique 10-digit message ID
        this.messageID = generateMessageID();

        // Increment and assign the message number
        messageCounter++;
        this.numMessagesSent = messageCounter;
    }

    /**
     * Constructor — creates a Message with a given Message ID (used for the test data).
     *
     * @param messageID   The 10-digit message ID.
     * @param recipient   The recipient's cell phone number.
     * @param messageText The message text content.
     */
    public Message(String messageID, String recipient, String messageText) {
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = messageID;

        // Increment and assign the message number
        messageCounter++;
        this.numMessagesSent = messageCounter;
    }

    /**
     * Generates a unique random 10-digit Message ID using Java's Random class.
     *
     * @return A 10-digit message ID as a String.
     */
    private String generateMessageID() {
        Random random = new Random();
        // Generate a number between 1000000000 and 9999999999 (always 10 digits)
        long id = 1000000000L + (long)(random.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }

    /**
     * Checks that the Message ID is not more than ten characters long.
     *
     * @return true if the message ID is 10 or fewer characters, false otherwise.
     */
    public boolean checkMessageID() {
        // Message ID must not exceed 10 characters
        return messageID != null && messageID.length() <= 10;
    }

    /**
     * Checks that a recipient cell phone number is correctly formatted.
     * Static so it can be used before a Message object exists.
     *
     * ATTRIBUTION: Regex pattern reused from Part 1 checkCellPhoneNumber method.
     * Research source: https://www.w3schools.com/java/java_regex.asp
     * Pattern: ^\+[0-9]{9,11}$
     *
     * @param cellNumber The cell number to check.
     * @return true if it starts with '+' followed by 9 to 11 digits.
     */
    public static boolean isValidRecipient(String cellNumber) {
        return cellNumber != null && cellNumber.matches("^\\+[0-9]{9,11}$");
    }

    /**
     * Checks that the recipient cell phone number of this message is correctly formatted.
     *
     * @return "Cell phone number successfully captured." if valid,
     *         or the error message if invalid.
     */
    public String checkRecipientCell() {
        if (isValidRecipient(recipient)) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. "
                 + "Please correct the number and try again.";
        }
    }

    /**
     * Validates message text — must not exceed 250 characters.
     * Static so it can be used before a Message object exists.
     *
     * @param text The message text.
     * @return "Message ready to send." if valid,
     *         "Message exceeds 250 characters by X; please reduce the size." if too long.
     */
    public static String validateMessageText(String text) {
        if (text == null || text.length() <= 250) {
            return "Message ready to send.";
        }
        // Calculate how many characters over the limit
        int excess = text.length() - 250;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    /**
     * Validates this message's text — must not exceed 250 characters.
     *
     * @return The validation message.
     */
    public String validateMessage() {
        return validateMessageText(messageText);
    }

    /**
     * Creates and returns the Message Hash.
     *
     * Hash format: firstTwoDigitsOfID:messageNumber:firstWordLastWord
     * The entire hash is converted to uppercase.
     *
     * Example:
     *   Message ID    = 0012345678
     *   Message number = 0
     *   Message        = "Hi Thanks"
     *   Hash           = "00:0:HITHANKS"
     *
     * @return The generated message hash in uppercase.
     */
    public String createMessageHash() {
        // Get the first two characters of the message ID
        String firstTwoID = messageID.substring(0, 2);

        // Split the message into individual words
        String[] words = messageText.trim().split("\\s+");

        // Get the first word of the message
        String firstWord = words[0];

        // Get the last word of the message (same as first if only one word)
        String lastWord = words[words.length - 1];

        // Build the hash: firstTwoID:messageNumber:firstWordlastWord
        String hash = firstTwoID + ":" + numMessagesSent + ":" + firstWord + lastWord;

        // Convert to uppercase and store
        this.messageHash = hash.toUpperCase();

        return this.messageHash;
    }

    /**
     * Adds one message to the Part 3 arrays. Every list gets the new entry at the
     * same position, and the message is also placed in its Sent/Disregarded/Stored array.
     *
     * @param id        The message ID.
     * @param hash      The message hash.
     * @param sentBy    The sender.
     * @param to        The recipient.
     * @param text      The message text.
     * @param flag      "Sent", "Disregarded" or "Stored".
     */
    private static void addToArrays(String id, String hash, String sentBy, String to,
                                    String text, String flag) {
        messageIdList.add(id);
        messageHashList.add(hash);
        senderList.add(sentBy);
        recipientList.add(to);
        messageTextList.add(text);
        flagList.add(flag);

        if (flag.equals("Sent")) {
            sentMessageList.add(text);
        } else if (flag.equals("Disregarded")) {
            disregardedMessageList.add(text);
        } else {
            storedMessageList.add(text);
        }
    }

    /**
     * Allows the user to send, disregard, or store the message.
     * The message is added to the matching array.
     *
     * Options:
     *   1 = Send Message  → "Message successfully sent."
     *   2 = Disregard     → "Press 0 to delete the message"
     *   3 = Store Message → "Message successfully stored."
     *
     * @param choice The user's selected option (1, 2, or 3).
     * @return The appropriate response message.
     */
    public String sentMessage(int choice) {
        // Hash is created for every message so it can be added to the hash array
        String hash = createMessageHash();

        switch (choice) {
            case 1:
                addToArrays(messageID, hash, sender, recipient, messageText, "Sent");
                totalMessagesSent++;
                return "Message successfully sent.";

            case 2:
                addToArrays(messageID, hash, sender, recipient, messageText, "Disregarded");
                return "Press 0 to delete the message";

            case 3:
                addToArrays(messageID, hash, sender, recipient, messageText, "Stored");
                return "Message successfully stored.";

            default:
                return "Invalid option. Please select 1, 2, or 3.";
        }
    }

    /**
     * Returns all messages sent during the session.
     * Displays each message in the required order:
     * Message ID, Message Hash, Recipient, Message.
     *
     * @return A formatted string listing all sent messages.
     */
    public static String printMessages() {
        StringBuilder output = new StringBuilder();

        // Loop through every message and show the ones that were sent
        for (int i = 0; i < flagList.size(); i++) {
            if (flagList.get(i).equals("Sent")) {
                output.append("Message ID: ").append(messageIdList.get(i)).append("\n");
                output.append("Message Hash: ").append(messageHashList.get(i)).append("\n");
                output.append("Recipient: ").append(recipientList.get(i)).append("\n");
                output.append("Message: ").append(messageTextList.get(i)).append("\n");
                output.append("-------------------------\n");
            }
        }

        // Return early message if no messages have been sent
        if (output.length() == 0) {
            return "No messages sent yet.";
        }
        return "=== All Sent Messages ===\n" + output;
    }

    /**
     * Returns the total number of messages sent during the current session.
     *
     * @return Total messages sent as an integer.
     */
    public static int returnTotalMessagessSent() {
        return totalMessagesSent;
    }

    /**
     * Research method — builds a JSON-formatted string for this message.
     *
     * ATTRIBUTION:
     * JSON format researched from: https://www.w3schools.com/js/js_json_intro.asp
     *
     * @return A JSON-formatted string representing this message.
     */
    public String storeMessage() {
        return "{\n"
                + "  \"messageID\": \"" + messageID + "\",\n"
                + "  \"messageHash\": \"" + messageHash + "\",\n"
                + "  \"recipient\": \"" + recipient + "\",\n"
                + "  \"message\": \"" + messageText + "\"\n"
                + "}";
    }

    // =============================================================
    //  PART 3 — JSON FILE
    // =============================================================

    /**
     * Sets the sender (the logged-in user) for the messages created from now on.
     *
     * @param sentBy The sender's cell phone number.
     */
    public static void setSender(String sentBy) {
        sender = sentBy;
    }

    /**
     * Writes every stored message to the JSON file.
     *
     * ATTRIBUTION: Gson user guide - https://github.com/google/gson/blob/main/UserGuide.md
     */
    public static void saveStoredMessagesToJson() {
        JsonArray array = new JsonArray();

        // Add every message flagged "Stored" as a JSON object
        for (int i = 0; i < flagList.size(); i++) {
            if (flagList.get(i).equals("Stored")) {
                JsonObject obj = new JsonObject();
                obj.addProperty("messageID", messageIdList.get(i));
                obj.addProperty("messageHash", messageHashList.get(i));
                obj.addProperty("sender", senderList.get(i));
                obj.addProperty("recipient", recipientList.get(i));
                obj.addProperty("message", messageTextList.get(i));
                array.add(obj);
            }
        }

        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(array, writer);
        } catch (IOException e) {
            System.out.println("Could not save the JSON file: " + e.getMessage());
        }
    }

    /**
     * Reads the JSON file into the Stored Messages array (and the other arrays).
     *
     * ATTRIBUTION: Gson user guide - https://github.com/google/gson/blob/main/UserGuide.md
     */
    public static void readStoredMessagesFromJson() {
        try (FileReader reader = new FileReader(JSON_FILE)) {
            JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();

            // Turn every JSON object back into entries in the arrays
            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                addToArrays(obj.get("messageID").getAsString(),
                            obj.get("messageHash").getAsString(),
                            obj.get("sender").getAsString(),
                            obj.get("recipient").getAsString(),
                            obj.get("message").getAsString(),
                            "Stored");
            }
        } catch (IOException e) {
            // File does not exist yet - nothing has been stored
        } catch (Exception e) {
            System.out.println("Could not read the JSON file: " + e.getMessage());
        }
    }

    // =============================================================
    //  PART 3 — STORED MESSAGES FEATURES
    // =============================================================

    /**
     * Displays the sender and recipient of all stored messages.
     *
     * @return A formatted string, one line per stored message.
     */
    public static String displaySenderAndRecipient() {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < flagList.size(); i++) {
            if (flagList.get(i).equals("Stored")) {
                output.append("Sender: ").append(senderList.get(i))
                      .append(" | Recipient: ").append(recipientList.get(i)).append("\n");
            }
        }

        if (output.length() == 0) {
            return "There are no stored messages.\n";
        }
        return output.toString();
    }

    /**
     * Finds the longest stored message.
     *
     * @return The longest stored message, or an empty string if none are stored.
     */
    public static String getLongestStoredMessage() {
        String longest = "";

        for (String message : storedMessageList) {
            if (message.length() > longest.length()) {
                longest = message;
            }
        }
        return longest;
    }

    /**
     * Searches for a message ID and returns the recipient and message.
     *
     * @param id The message ID to search for.
     * @return The recipient and message, or a not-found message.
     */
    public static String searchByMessageId(String id) {
        int index = messageIdList.indexOf(id);

        if (index == -1) {
            return "Message ID not found.";
        }
        return "Recipient: " + recipientList.get(index) + ", Message: " + messageTextList.get(index);
    }

    /**
     * Searches for all the stored messages for a particular recipient.
     *
     * @param searchRecipient The recipient's cell number.
     * @return A list of the stored messages for that recipient.
     */
    public static ArrayList<String> searchByRecipient(String searchRecipient) {
        ArrayList<String> found = new ArrayList<>();

        for (int i = 0; i < recipientList.size(); i++) {
            if (recipientList.get(i).equals(searchRecipient) && flagList.get(i).equals("Stored")) {
                found.add(messageTextList.get(i));
            }
        }
        return found;
    }

    /**
     * Deletes a message using its message hash.
     * The message is removed from every array it is in.
     *
     * @param hash The message hash.
     * @return A confirmation message, or a not-found message.
     */
    public static String deleteByHash(String hash) {
        int index = messageHashList.indexOf(hash);

        if (index == -1) {
            return "Message hash not found.";
        }

        String text = messageTextList.get(index);
        String flag = flagList.get(index);

        // Remove from the Sent / Disregarded / Stored array
        if (flag.equals("Sent")) {
            sentMessageList.remove(text);
        } else if (flag.equals("Disregarded")) {
            disregardedMessageList.remove(text);
        } else {
            storedMessageList.remove(text);
        }

        // Remove from the parallel arrays (same position in each)
        messageIdList.remove(index);
        messageHashList.remove(index);
        senderList.remove(index);
        recipientList.remove(index);
        messageTextList.remove(index);
        flagList.remove(index);

        return "Message: \"" + text + "\" successfully deleted.";
    }

    /**
     * Builds a report with the full details of all stored messages.
     *
     * @return The report as a formatted string.
     */
    public static String getStoredReport() {
        StringBuilder report = new StringBuilder("=== Stored Messages Report ===\n");

        for (int i = 0; i < flagList.size(); i++) {
            if (flagList.get(i).equals("Stored")) {
                report.append("Message Hash: ").append(messageHashList.get(i)).append("\n");
                report.append("Message ID: ").append(messageIdList.get(i)).append("\n");
                report.append("Sender: ").append(senderList.get(i)).append("\n");
                report.append("Recipient: ").append(recipientList.get(i)).append("\n");
                report.append("Message: ").append(messageTextList.get(i)).append("\n");
                report.append("-------------------------\n");
            }
        }
        return report.toString();
    }

    // ---- Getters ----

    /** @return The 10-digit message ID. */
    public String getMessageID() { return messageID; }

    /** @return The message hash. */
    public String getMessageHash() { return messageHash; }

    /** @return The recipient cell phone number. */
    public String getRecipient() { return recipient; }

    /** @return The message text content. */
    public String getMessageText() { return messageText; }

    /** @return The message number in the sequence. */
    public int getNumMessagesSent() { return numMessagesSent; }

    /**
     * Resets all static arrays and counters — used for unit testing only
     * so tests do not interfere with each other.
     */
    public static void resetCounters() {
        sentMessageList.clear();
        disregardedMessageList.clear();
        storedMessageList.clear();
        messageHashList.clear();
        messageIdList.clear();
        senderList.clear();
        recipientList.clear();
        messageTextList.clear();
        flagList.clear();
        totalMessagesSent = 0;
        messageCounter = 0;
    }
}


// =============================================================
//  MAIN CLASS — Part 1 + Part 2 + Part 3
// =============================================================

/**
 * Main class for the Chat Application - Part 1, Part 2 and Part 3 combined.
 *
 * Console-only application — no GUIs or JOptionPane used.
 *
 * PART 1: Register and login.
 * PART 2: QuickChat - send, disregard or store messages.
 * PART 3: Arrays + a fourth main menu option "Stored Messages":
 *   a) Display the sender and recipient of all stored messages
 *   b) Display the longest stored message
 *   c) Search for a message ID and display the recipient and message
 *   d) Search for all the messages stored for a particular recipient
 *   e) Delete a message using the message hash
 *   f) Display a report of all the stored messages
 *
 * @author Mc Chopper
 * @version 2.0
 */
public class Main {

    // Shared scanner for all console input throughout the application
    static Scanner scanner = new Scanner(System.in);

    /**
     * Entry point of the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {

        // =========================================================
        // PART 1 — REGISTRATION
        // =========================================================

        // Display welcome banner
        System.out.println("======================================");
        System.out.println("   Welcome to the Chat Application   ");
        System.out.println("======================================\n");

        // Collect the user's first and last name
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine().trim();

        // Create Login object with the user's name
        Login login = new Login(firstName, lastName);

        System.out.println("\n--- Registration ---");

        // Loop until a valid username is entered
        String username = "";
        while (true) {
            System.out.print("Enter a username (must contain '_' and be max 5 characters): ");
            username = scanner.nextLine().trim();

            if (login.checkUserName(username)) {
                System.out.println("Username successfully captured.");
                break;
            } else {
                System.out.println("Username is not correctly formatted; please ensure that "
                        + "your username contains an underscore and is no more than five characters in length.");
            }
        }

        // Loop until a valid password is entered
        String password = "";
        while (true) {
            System.out.print("Enter a password (min 8 chars, uppercase, number, special char): ");
            password = scanner.nextLine().trim();

            if (login.checkPasswordComplexity(password)) {
                System.out.println("Password successfully captured.");
                break;
            } else {
                System.out.println("Password is not correctly formatted; please ensure that the password "
                        + "contains at least eight characters, a capital letter, a number, and a special character.");
            }
        }

        // Loop until a valid cell phone number is entered
        String cellPhone = "";
        while (true) {
            System.out.print("Enter your cell phone number with international code (e.g. +27838968976): ");
            cellPhone = scanner.nextLine().trim();

            if (login.checkCellPhoneNumber(cellPhone)) {
                System.out.println("Cell phone number successfully added.");
                break;
            } else {
                System.out.println("Cell phone number incorrectly formatted or does not contain "
                        + "international code; please correct the number and try again.");
            }
        }

        // Store all validated credentials
        login.registerUser(username, password, cellPhone);

        // =========================================================
        // PART 1 — LOGIN
        // =========================================================

        System.out.println("\n--- Login ---");

        // Loop until login is successful
        while (true) {
            System.out.print("Enter your username: ");
            String loginUsername = scanner.nextLine().trim();

            System.out.print("Enter your password: ");
            String loginPassword = scanner.nextLine().trim();

            // Display the login status message
            System.out.println(login.returnLoginStatus(loginUsername, loginPassword));

            // Break out of loop only if login was successful
            if (login.loginUser(loginUsername, loginPassword)) {
                break;
            }

            System.out.println("Please try again.\n");
        }

        // =========================================================
        // PART 2 — QUICKCHAT MESSAGING
        // =========================================================

        // Display QuickChat welcome — only reached after successful login
        System.out.println("\nWelcome to QuickChat.");

        // PART 3 — the logged-in user is the sender, and the JSON file is read into the arrays
        Message.setSender(login.getRegisteredCellPhone());
        Message.readStoredMessagesFromJson();

        // Ask user how many messages they want to send upfront
        int numMessages = 0;
        while (numMessages <= 0) {
            System.out.print("\nHow many messages would you like to send? ");
            try {
                numMessages = Integer.parseInt(scanner.nextLine().trim());
                if (numMessages <= 0) {
                    System.out.println("Please enter a number greater than 0.");
                }
            } catch (NumberFormatException e) {
                // Handle non-numeric input
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }

        // ---- Main Menu Loop ----
        // App keeps running until user selects Quit (option 3)
        boolean running = true;
        while (running) {

            // Display the numeric menu
            System.out.println("\n--- QuickChat Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Select an option: ");

            String menuChoice = scanner.nextLine().trim();

            switch (menuChoice) {
                case "1":
                    // Send the defined number of messages
                    sendMessages(numMessages);
                    break;

                case "2":
                    // Feature still in development
                    System.out.println("Coming Soon.");
                    break;

                case "3":
                    // Exit the application
                    running = false;
                    System.out.println("\nGoodbye!");
                    break;

                case "4":
                    // PART 3 — the Stored Messages menu
                    storedMessagesMenu();
                    break;

                default:
                    System.out.println("Invalid option. Please select 1, 2, 3, or 4.");
            }
        }

        scanner.close();
    }

    /**
     * Handles sending the specified number of messages.
     *
     * For each message:
     * - Validates the recipient cell number (re-prompts if invalid)
     * - Validates the message length (re-prompts if over 250 chars)
     * - Displays the Message ID and Message Hash
     * - Asks user to Send, Disregard, or Store
     *
     * After all messages are processed:
     * - Displays all sent messages (ID, Hash, Recipient, Message)
     * - Displays total number of messages sent
     *
     * @param numMessages The number of messages the user wants to send.
     */
    private static void sendMessages(int numMessages) {

        // Loop through each message slot
        for (int i = 1; i <= numMessages; i++) {
            System.out.println("\n--- Message " + i + " of " + numMessages + " ---");

            // ---- Validate recipient cell number ----
            String recipient = "";
            while (true) {
                System.out.print("Enter recipient cell number (with international code e.g. +27718693002): ");
                recipient = scanner.nextLine().trim();

                if (Message.isValidRecipient(recipient)) {
                    System.out.println("Cell phone number successfully captured.");
                    break;
                }
                System.out.println("Cell phone number is incorrectly formatted or does not contain "
                        + "an international code. Please correct the number and try again.");
            }

            // ---- Validate message text ----
            String messageText = "";
            while (true) {
                System.out.print("Enter your message (max 250 characters): ");
                messageText = scanner.nextLine().trim();

                String validation = Message.validateMessageText(messageText);
                if (validation.equals("Message ready to send.")) {
                    System.out.println(validation);
                    break;
                }
                // Message too long — display error and re-prompt
                System.out.println(validation);
                System.out.println("Please enter a message of less than 250 characters.");
            }

            // The Message object is only created once recipient and text are valid
            Message msg = new Message(recipient, messageText);

            // Display the auto-generated Message ID and Hash
            System.out.println("Message ID generated: " + msg.getMessageID());
            System.out.println("Message Hash: " + msg.createMessageHash());

            // ---- Send / Disregard / Store ----
            boolean actionDone = false;
            while (!actionDone) {
                System.out.println("\nWhat would you like to do?");
                System.out.println("1) Send Message");
                System.out.println("2) Disregard Message");
                System.out.println("3) Store Message to send later");
                System.out.print("Select an option: ");

                try {
                    int action = Integer.parseInt(scanner.nextLine().trim());
                    String result = msg.sentMessage(action);
                    System.out.println(result);

                    if (action == 1) {
                        // Sent — move to next message
                        actionDone = true;
                    } else if (action == 3) {
                        // Stored — keep the JSON file up to date, then move to next message
                        Message.saveStoredMessagesToJson();
                        actionDone = true;
                    } else if (action == 2) {
                        // Disregarded — repeat this message slot
                        System.out.println("Message disregarded. Please re-enter message " + i + ".");
                        actionDone = true;
                        i--;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter 1, 2, or 3.");
                }
            }
        }

        // ---- Display all sent messages and total count ----
        System.out.println("\n======================================");
        System.out.println(Message.printMessages());
        System.out.println("Total messages sent: " + Message.returnTotalMessagessSent());
        System.out.println("======================================");
    }

    /**
     * PART 3 — the Stored Messages menu (main menu option 4).
     * Uses the arrays to search, delete and report on the stored messages.
     */
    private static void storedMessagesMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Stored Messages ---");
            System.out.println("a) Display the sender and recipient of all stored messages");
            System.out.println("b) Display the longest stored message");
            System.out.println("c) Search for a message ID");
            System.out.println("d) Search for all messages stored for a particular recipient");
            System.out.println("e) Delete a message using the message hash");
            System.out.println("f) Display a report of all stored messages");
            System.out.println("g) Back to main menu");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "a":
                    System.out.print(Message.displaySenderAndRecipient());
                    break;

                case "b":
                    String longest = Message.getLongestStoredMessage();
                    if (longest.isEmpty()) {
                        System.out.println("There are no stored messages.");
                    } else {
                        System.out.println("Longest stored message: " + longest);
                    }
                    break;

                case "c":
                    System.out.print("Enter the message ID: ");
                    System.out.println(Message.searchByMessageId(scanner.nextLine().trim()));
                    break;

                case "d":
                    System.out.print("Enter the recipient cell number: ");
                    ArrayList<String> found = Message.searchByRecipient(scanner.nextLine().trim());
                    if (found.isEmpty()) {
                        System.out.println("No stored messages for that recipient.");
                    } else {
                        for (String message : found) {
                            System.out.println("- " + message);
                        }
                    }
                    break;

                case "e":
                    System.out.print("Enter the message hash: ");
                    System.out.println(Message.deleteByHash(scanner.nextLine().trim()));
                    // Keep the JSON file in step with the stored messages array
                    Message.saveStoredMessagesToJson();
                    break;

                case "f":
                    System.out.print(Message.getStoredReport());
                    break;

                case "g":
                    back = true;
                    break;

                default:
                    System.out.println("Invalid option. Please select a, b, c, d, e, f, or g.");
            }
        }
    }
}
