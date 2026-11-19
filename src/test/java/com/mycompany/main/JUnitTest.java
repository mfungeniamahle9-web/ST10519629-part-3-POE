package com.mycompany.main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for the Chat Application (Part 1, Part 2 and Part 3).
 * Place this file in src/test/java/com/mycompany/main/ (same package as Main.java,
 * because Login and Message are package-private classes).
 */
class MainTest {

    private static final String RECIPIENT_1 = "+27718693002";
    private static final String RECIPIENT_2 = "+27838884567";
    private static final String SENDER = "+27838968976";

    private Login login;

    @BeforeEach
    void setUp() {
        login = new Login("Mc", "Chopper");
        Message.resetCounters();
        Message.setSender(SENDER);
        new File(Message.JSON_FILE).delete();
    }

    @AfterEach
    void tearDown() {
        Message.resetCounters();
        new File(Message.JSON_FILE).delete();
    }

    // =========================================================
    //  PART 1 — LOGIN CLASS
    // =========================================================

    @Test
    void checkUserName_valid() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    void checkUserName_validShortWithUnderscore() {
        assertTrue(login.checkUserName("a_"));
    }

    @Test
    void checkUserName_noUnderscore() {
        assertFalse(login.checkUserName("kyle1"));
    }

    @Test
    void checkUserName_tooLong() {
        assertFalse(login.checkUserName("kyle_1"));
    }

    @Test
    void checkUserName_null() {
        assertFalse(login.checkUserName(null));
    }

    @Test
    void checkPasswordComplexity_valid() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    void checkPasswordComplexity_tooShort() {
        assertFalse(login.checkPasswordComplexity("Ab1!"));
    }

    @Test
    void checkPasswordComplexity_noCapital() {
        assertFalse(login.checkPasswordComplexity("password1!"));
    }

    @Test
    void checkPasswordComplexity_noNumber() {
        assertFalse(login.checkPasswordComplexity("Password!!"));
    }

    @Test
    void checkPasswordComplexity_noSpecial() {
        assertFalse(login.checkPasswordComplexity("Password11"));
    }

    @Test
    void checkPasswordComplexity_null() {
        assertFalse(login.checkPasswordComplexity(null));
    }

    @Test
    void checkCellPhoneNumber_valid() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    void checkCellPhoneNumber_noInternationalCode() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    @Test
    void checkCellPhoneNumber_tooManyDigits() {
        assertFalse(login.checkCellPhoneNumber("+278389689761234"));
    }

    @Test
    void checkCellPhoneNumber_tooFewDigits() {
        assertFalse(login.checkCellPhoneNumber("+2783896"));
    }

    @Test
    void checkCellPhoneNumber_containsLetters() {
        assertFalse(login.checkCellPhoneNumber("+2783896abc6"));
    }

    @Test
    void checkCellPhoneNumber_null() {
        assertFalse(login.checkCellPhoneNumber(null));
    }

    @Test
    void registerUser_success() {
        String result = login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username successfully captured.\nPassword successfully captured.\n"
                + "Cell phone number successfully added.", result);
        assertEquals("kyl_1", login.getRegisteredUsername());
        assertEquals("+27838968976", login.getRegisteredCellPhone());
    }

    @Test
    void registerUser_badUsername() {
        String result = login.registerUser("kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(result.startsWith("Username is not correctly formatted"));
        assertNull(login.getRegisteredUsername());
    }

    @Test
    void registerUser_badPassword() {
        String result = login.registerUser("kyl_1", "password", "+27838968976");
        assertTrue(result.startsWith("Password is not correctly formatted"));
        assertNull(login.getRegisteredUsername());
    }

    @Test
    void registerUser_badCellPhone() {
        String result = login.registerUser("kyl_1", "Ch&&sec@ke99!", "08966553");
        assertTrue(result.startsWith("Cell phone number incorrectly formatted"));
        assertNull(login.getRegisteredCellPhone());
    }

    @Test
    void loginUser_correctCredentials() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void loginUser_wrongPassword() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("kyl_1", "wrong"));
    }

    @Test
    void loginUser_wrongUsername() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("xx_1", "Ch&&sec@ke99!"));
    }

    @Test
    void loginUser_notRegistered() {
        assertFalse(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void returnLoginStatus_success() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Welcome Mc Chopper it is great to see you.",
                login.returnLoginStatus("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void returnLoginStatus_failure() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username or password incorrect, please try again.",
                login.returnLoginStatus("kyl_1", "bad"));
    }

    @Test
    void login_getNames() {
        assertEquals("Mc", login.getFirstName());
        assertEquals("Chopper", login.getLastName());
    }

    // =========================================================
    //  PART 2 — MESSAGE CLASS
    // =========================================================

    @Test
    void messageID_isTenDigits() {
        Message msg = new Message(RECIPIENT_1, "Hello");
        assertEquals(10, msg.getMessageID().length());
        assertTrue(msg.getMessageID().matches("\\d{10}"));
        assertTrue(msg.checkMessageID());
    }

    @Test
    void messageID_tooLongFails() {
        Message msg = new Message("12345678901", RECIPIENT_1, "Hello");
        assertFalse(msg.checkMessageID());
    }

    @Test
    void isValidRecipient_valid() {
        assertTrue(Message.isValidRecipient("+27718693002"));
    }

    @Test
    void isValidRecipient_invalid() {
        assertFalse(Message.isValidRecipient("08575975889"));
        assertFalse(Message.isValidRecipient(null));
    }

    @Test
    void checkRecipientCell_valid() {
        Message msg = new Message(RECIPIENT_1, "Hello");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    @Test
    void checkRecipientCell_invalid() {
        Message msg = new Message("08575975889", "Hello");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.", msg.checkRecipientCell());
    }

    @Test
    void validateMessageText_exactly250() {
        String text = "a".repeat(250);
        assertEquals("Message ready to send.", Message.validateMessageText(text));
    }

    @Test
    void validateMessageText_over250() {
        String text = "a".repeat(260);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                Message.validateMessageText(text));
    }

    @Test
    void validateMessage_instanceMethod() {
        Message ok = new Message(RECIPIENT_1, "Hi Mike, can you join us for dinner tonight");
        assertEquals("Message ready to send.", ok.validateMessage());

        Message tooLong = new Message(RECIPIENT_1, "x".repeat(251));
        assertEquals("Message exceeds 250 characters by 1; please reduce the size.",
                tooLong.validateMessage());
    }

    @Test
    void createMessageHash_format() {
        // First message after reset => message number 1
        Message msg = new Message("0012345678", RECIPIENT_1, "Hi Thanks");
        assertEquals("00:1:HITHANKS", msg.createMessageHash());
        assertEquals("00:1:HITHANKS", msg.getMessageHash());
    }

    @Test
    void createMessageHash_longMessageUsesFirstAndLastWord() {
        Message msg = new Message("0012345678", RECIPIENT_1, "Hi Mike, can you join us for dinner tonight");
        assertEquals("00:1:HITONIGHT", msg.createMessageHash());
    }

    @Test
    void createMessageHash_singleWord() {
        Message msg = new Message("1234567890", RECIPIENT_1, "Hello");
        assertEquals("12:1:HELLOHELLO", msg.createMessageHash());
    }

    @Test
    void createMessageHash_numberIncrementsPerMessage() {
        new Message("0012345678", RECIPIENT_1, "First one");
        Message second = new Message("0012345679", RECIPIENT_1, "Second one");
        assertEquals(2, second.getNumMessagesSent());
        assertEquals("00:2:SECONDONE", second.createMessageHash());
    }

    @Test
    void sentMessage_send() {
        Message msg = new Message(RECIPIENT_1, "Hello there");
        assertEquals("Message successfully sent.", msg.sentMessage(1));
        assertEquals(1, Message.returnTotalMessagessSent());
        assertEquals(1, Message.sentMessageList.size());
        assertEquals("Sent", Message.flagList.get(0));
    }

    @Test
    void sentMessage_disregard() {
        Message msg = new Message(RECIPIENT_1, "Hello there");
        assertEquals("Press 0 to delete the message", msg.sentMessage(2));
        assertEquals(0, Message.returnTotalMessagessSent());
        assertEquals(1, Message.disregardedMessageList.size());
    }

    @Test
    void sentMessage_store() {
        Message msg = new Message(RECIPIENT_1, "Hello there");
        assertEquals("Message successfully stored.", msg.sentMessage(3));
        assertEquals(1, Message.storedMessageList.size());
        assertEquals(0, Message.returnTotalMessagessSent());
    }

    @Test
    void sentMessage_invalidChoice() {
        Message msg = new Message(RECIPIENT_1, "Hello there");
        assertEquals("Invalid option. Please select 1, 2, or 3.", msg.sentMessage(9));
        assertTrue(Message.messageIdList.isEmpty());
    }

    @Test
    void printMessages_noneSent() {
        assertEquals("No messages sent yet.", Message.printMessages());
    }

    @Test
    void printMessages_showsOnlySent() {
        Message sent = new Message("1111111111", RECIPIENT_1, "Sent message");
        sent.sentMessage(1);
        Message stored = new Message("2222222222", RECIPIENT_2, "Stored message");
        stored.sentMessage(3);

        String output = Message.printMessages();
        assertTrue(output.contains("=== All Sent Messages ==="));
        assertTrue(output.contains("Message ID: 1111111111"));
        assertTrue(output.contains("Recipient: " + RECIPIENT_1));
        assertTrue(output.contains("Message: Sent message"));
        assertFalse(output.contains("Stored message"));
    }

    @Test
    void returnTotalMessagesSent_countsOnlySent() {
        new Message(RECIPIENT_1, "One").sentMessage(1);
        new Message(RECIPIENT_1, "Two").sentMessage(1);
        new Message(RECIPIENT_1, "Three").sentMessage(2);
        new Message(RECIPIENT_1, "Four").sentMessage(3);
        assertEquals(2, Message.returnTotalMessagessSent());
    }

    @Test
    void storeMessage_returnsJsonString() {
        Message msg = new Message("0012345678", RECIPIENT_1, "Hi Thanks");
        msg.createMessageHash();
        String json = msg.storeMessage();
        assertTrue(json.contains("\"messageID\": \"0012345678\""));
        assertTrue(json.contains("\"messageHash\": \"00:1:HITHANKS\""));
        assertTrue(json.contains("\"recipient\": \"" + RECIPIENT_1 + "\""));
        assertTrue(json.contains("\"message\": \"Hi Thanks\""));
    }

    @Test
    void getters_returnConstructorValues() {
        Message msg = new Message("0012345678", RECIPIENT_1, "Hi Thanks");
        assertEquals("0012345678", msg.getMessageID());
        assertEquals(RECIPIENT_1, msg.getRecipient());
        assertEquals("Hi Thanks", msg.getMessageText());
    }

    // =========================================================
    //  PART 3 — ARRAYS AND STORED MESSAGE FEATURES
    // =========================================================

    /** Loads the Part 3 test data: 2 sent, 1 disregarded, 2 stored messages. */
    private void loadTestData() {
        new Message("0000000001", RECIPIENT_1, "Did you get the cake?").sentMessage(1);
        new Message("0000000002", RECIPIENT_2, "Where are you? You are late! I have asked you to be on time.").sentMessage(3);
        new Message("0000000003", RECIPIENT_2, "Yohoooo, I am at your gate.").sentMessage(2);
        new Message("0000000004", "0838884567", "It is dinner time!").sentMessage(1);
        new Message("0000000005", RECIPIENT_1, "Ok, I am leaving without you.").sentMessage(3);
    }

    @Test
    void part3_sentArrayCorrectlyPopulated() {
        loadTestData();
        assertEquals(2, Message.sentMessageList.size());
        assertTrue(Message.sentMessageList.contains("Did you get the cake?"));
        assertTrue(Message.sentMessageList.contains("It is dinner time!"));
    }

    @Test
    void part3_disregardedArrayCorrectlyPopulated() {
        loadTestData();
        assertEquals(1, Message.disregardedMessageList.size());
        assertEquals("Yohoooo, I am at your gate.", Message.disregardedMessageList.get(0));
    }

    @Test
    void part3_storedArrayCorrectlyPopulated() {
        loadTestData();
        assertEquals(2, Message.storedMessageList.size());
    }

    @Test
    void part3_parallelListsSameSize() {
        loadTestData();
        int size = Message.messageIdList.size();
        assertEquals(5, size);
        assertEquals(size, Message.messageHashList.size());
        assertEquals(size, Message.senderList.size());
        assertEquals(size, Message.recipientList.size());
        assertEquals(size, Message.messageTextList.size());
        assertEquals(size, Message.flagList.size());
    }

    @Test
    void part3_senderIsLoggedInUser() {
        loadTestData();
        for (String s : Message.senderList) {
            assertEquals(SENDER, s);
        }
    }

    @Test
    void part3_displaySenderAndRecipient() {
        loadTestData();
        String output = Message.displaySenderAndRecipient();
        assertTrue(output.contains("Sender: " + SENDER + " | Recipient: " + RECIPIENT_2));
        assertTrue(output.contains("Sender: " + SENDER + " | Recipient: " + RECIPIENT_1));
        // Only the 2 stored messages are listed
        assertEquals(2, output.split("\n").length);
    }

    @Test
    void part3_displaySenderAndRecipient_noneStored() {
        assertEquals("There are no stored messages.\n", Message.displaySenderAndRecipient());
    }

    @Test
    void part3_longestStoredMessage() {
        loadTestData();
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                Message.getLongestStoredMessage());
    }

    @Test
    void part3_longestStoredMessage_noneStored() {
        assertEquals("", Message.getLongestStoredMessage());
    }

    @Test
    void part3_searchByMessageId_found() {
        loadTestData();
        assertEquals("Recipient: 0838884567, Message: It is dinner time!",
                Message.searchByMessageId("0000000004"));
    }

    @Test
    void part3_searchByMessageId_notFound() {
        loadTestData();
        assertEquals("Message ID not found.", Message.searchByMessageId("9999999999"));
    }

    @Test
    void part3_searchByRecipient_returnsStoredOnly() {
        loadTestData();
        ArrayList<String> found = Message.searchByRecipient(RECIPIENT_2);
        // RECIPIENT_2 has 1 stored and 1 disregarded message - only the stored one is returned
        assertEquals(1, found.size());
        assertEquals("Where are you? You are late! I have asked you to be on time.", found.get(0));
    }

    @Test
    void part3_searchByRecipient_multipleStored() {
        loadTestData();
        new Message("0000000006", RECIPIENT_1, "Another stored one").sentMessage(3);
        ArrayList<String> found = Message.searchByRecipient(RECIPIENT_1);
        assertEquals(2, found.size());
        assertTrue(found.contains("Ok, I am leaving without you."));
        assertTrue(found.contains("Another stored one"));
    }

    @Test
    void part3_searchByRecipient_noneFound() {
        loadTestData();
        assertTrue(Message.searchByRecipient("+27000000000").isEmpty());
    }

    @Test
    void part3_deleteByHash_storedMessage() {
        loadTestData();
        // Message 5 is the 5th message created: ID 0000000005 -> hash "00:5:OKYOU."
        String hash = Message.messageHashList.get(4);
        String result = Message.deleteByHash(hash);

        assertEquals("Message: \"Ok, I am leaving without you.\" successfully deleted.", result);
        assertEquals(4, Message.messageIdList.size());
        assertEquals(1, Message.storedMessageList.size());
        assertFalse(Message.messageHashList.contains(hash));
        // All parallel lists stay in step
        assertEquals(4, Message.senderList.size());
        assertEquals(4, Message.recipientList.size());
        assertEquals(4, Message.messageTextList.size());
        assertEquals(4, Message.flagList.size());
    }

    @Test
    void part3_deleteByHash_sentMessage() {
        loadTestData();
        String hash = Message.messageHashList.get(0);
        Message.deleteByHash(hash);
        assertEquals(1, Message.sentMessageList.size());
        assertFalse(Message.sentMessageList.contains("Did you get the cake?"));
    }

    @Test
    void part3_deleteByHash_disregardedMessage() {
        loadTestData();
        String hash = Message.messageHashList.get(2);
        Message.deleteByHash(hash);
        assertTrue(Message.disregardedMessageList.isEmpty());
    }

    @Test
    void part3_deleteByHash_notFound() {
        loadTestData();
        assertEquals("Message hash not found.", Message.deleteByHash("NOT:A:HASH"));
        assertEquals(5, Message.messageIdList.size());
    }

    @Test
    void part3_storedReport_containsAllDetails() {
        loadTestData();
        String report = Message.getStoredReport();
        assertTrue(report.startsWith("=== Stored Messages Report ==="));
        assertTrue(report.contains("Message ID: 0000000002"));
        assertTrue(report.contains("Message ID: 0000000005"));
        assertTrue(report.contains("Message Hash: "));
        assertTrue(report.contains("Sender: " + SENDER));
        assertTrue(report.contains("Recipient: " + RECIPIENT_2));
        assertTrue(report.contains("Message: Ok, I am leaving without you."));
        // Sent and disregarded messages are not part of the report
        assertFalse(report.contains("Did you get the cake?"));
        assertFalse(report.contains("Yohoooo"));
    }

    @Test
    void part3_storedReport_empty() {
        assertEquals("=== Stored Messages Report ===\n", Message.getStoredReport());
    }

    // ---- JSON file ----

    @Test
    void json_saveCreatesFile() {
        loadTestData();
        Message.saveStoredMessagesToJson();
        assertTrue(new File(Message.JSON_FILE).exists());
    }

    @Test
    void json_roundTripRestoresStoredMessages() {
        loadTestData();
        String expectedId = Message.messageIdList.get(1);
        String expectedHash = Message.messageHashList.get(1);
        Message.saveStoredMessagesToJson();

        // Clear everything, then read the file back into the arrays
        Message.resetCounters();
        Message.readStoredMessagesFromJson();

        assertEquals(2, Message.storedMessageList.size());
        assertEquals(2, Message.messageIdList.size());
        assertTrue(Message.messageIdList.contains(expectedId));
        assertTrue(Message.messageHashList.contains(expectedHash));
        assertTrue(Message.storedMessageList.contains("Ok, I am leaving without you."));
        assertEquals(SENDER, Message.senderList.get(0));
        assertEquals("Stored", Message.flagList.get(0));
    }

    @Test
    void json_onlyStoredMessagesAreSaved() {
        loadTestData();
        Message.saveStoredMessagesToJson();
        Message.resetCounters();
        Message.readStoredMessagesFromJson();

        assertTrue(Message.sentMessageList.isEmpty());
        assertTrue(Message.disregardedMessageList.isEmpty());
    }

    @Test
    void json_readMissingFileDoesNothing() {
        new File(Message.JSON_FILE).delete();
        Message.readStoredMessagesFromJson();
        assertTrue(Message.storedMessageList.isEmpty());
    }

    @Test
    void json_saveAfterDeleteUpdatesFile() {
        loadTestData();
        Message.saveStoredMessagesToJson();

        Message.deleteByHash(Message.messageHashList.get(1)); // delete a stored message
        Message.saveStoredMessagesToJson();

        Message.resetCounters();
        Message.readStoredMessagesFromJson();
        assertEquals(1, Message.storedMessageList.size());
        assertEquals("Ok, I am leaving without you.", Message.storedMessageList.get(0));
    }

    // ---- reset ----

    @Test
    void resetCounters_clearsEverything() {
        loadTestData();
        Message.resetCounters();
        assertTrue(Message.messageIdList.isEmpty());
        assertTrue(Message.sentMessageList.isEmpty());
        assertTrue(Message.storedMessageList.isEmpty());
        assertTrue(Message.disregardedMessageList.isEmpty());
        assertEquals(0, Message.returnTotalMessagessSent());
    }
}