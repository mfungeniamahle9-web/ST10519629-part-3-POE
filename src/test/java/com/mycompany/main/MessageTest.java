package com.mycompany.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Part 3 unit tests. Uses the test data given in the task to populate the arrays.
 */
public class MessageTest {

    // Runs before every test: clears the arrays and loads Test Data Messages 1 - 5
    @BeforeEach
    public void setUp() {
        Message.resetCounters();
        Message.setSender("+27831234567");

        new Message("0000000001", "+27834557896", "Did you get the cake?").sentMessage(1);          // Sent
        new Message("0000000002", "+27838884567",
                "Where are you? You are late! I have asked you to be on time.").sentMessage(3);      // Stored
        new Message("0000000003", "+27834484567", "Yohoooo, I am at your gate.").sentMessage(2);     // Disregard
        new Message("0838884567", "0838884567", "It is dinner time!").sentMessage(1);                // Sent
        new Message("0000000005", "+27838884567", "Ok, I am leaving without you.").sentMessage(3);   // Stored
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Did you get the cake?");
        expected.add("It is dinner time!");
        assertEquals(expected, Message.sentMessageList);
    }

    @Test
    public void testDisplayLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                Message.getLongestStoredMessage());
    }

    @Test
    public void testSearchForMessageId() {
        assertEquals("Recipient: 0838884567, Message: It is dinner time!",
                Message.searchByMessageId("0838884567"));
    }

    @Test
    public void testSearchMessagesForRecipient() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Where are you? You are late! I have asked you to be on time.");
        expected.add("Ok, I am leaving without you.");
        assertEquals(expected, Message.searchByRecipient("+27838884567"));
    }

    @Test
    public void testDeleteMessageUsingHash() {
        // Test Message 2 is at position 1 in the hash array
        String hash = Message.messageHashList.get(1);
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                Message.deleteByHash(hash));
        assertEquals(1, Message.storedMessageList.size());
    }

    @Test
    public void testDisplayReport() {
        String report = Message.getStoredReport();
        assertTrue(report.contains("Message Hash: " + Message.messageHashList.get(1)));
        assertTrue(report.contains("Recipient: +27838884567"));
        assertTrue(report.contains("Message: Ok, I am leaving without you."));
    }

    @Test
    public void testDisregardedAndHashArraysPopulated() {
        assertEquals(1, Message.disregardedMessageList.size());
        assertEquals(5, Message.messageHashList.size());
        assertEquals(5, Message.messageIdList.size());
    }
}

