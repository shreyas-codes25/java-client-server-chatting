package testpackage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatApplication2 {

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private DataOutputStream serverOutputStream;
    private DataInputStream dataInputStream;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ChatApplication2();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ChatApplication2() {
        initialize();
        connectToServer();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Client");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Existing code for GUI initialization...
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Use this panel for message components
        JPanel messagePanel = new JPanel();
        panel.add(messagePanel, BorderLayout.SOUTH);
        messagePanel.setLayout(new BorderLayout(0, 0));

        messageField = new JTextField();
        messagePanel.add(messageField, BorderLayout.CENTER);
        messageField.setColumns(10);

        JButton sendButton = new JButton("Send");
        messagePanel.add(sendButton, BorderLayout.EAST);
        // ActionListener for the send button
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        // Existing code for GUI update thread...

        frame.setVisible(true);
    }

    private void connectToServer() {
        Thread serverThread = new Thread(() -> {
        try {
            Socket socket = new Socket("localhost", 6666);
            serverOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            while (true) {
                String receivedMessage = dataInputStream.readUTF();
                displayMessage("Server says: " + receivedMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    });
    serverThread.setDaemon(true);
    serverThread.start();
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            try {
                // Send the message to the server
                serverOutputStream.writeUTF(message);
                serverOutputStream.flush();

                // For simplicity, just append the sent message to the chat area
                chatArea.append("You: " + message + "\n");

                // Clear the message field
                messageField.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void displayMessage(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append(message + "\n"));
    }
}
