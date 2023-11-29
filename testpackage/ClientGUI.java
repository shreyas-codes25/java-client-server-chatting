package testpackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ClientGUI extends JFrame {
    private Socket socket;
    private JTextArea chatArea;
    private JTextField messageField;

    private ClientGUI() {
        initUI();
        initClient();
    }

    private void initUI() {
        setTitle("Client GUI");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        messageField = new JTextField();
        JButton sendButton = new JButton("Send");

        sendButton.addActionListener(e -> {
            String message = messageField.getText();
            sendMessageToServer(message);
            messageField.setText("");
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void initClient() {
        try {
            socket = new Socket("localhost", 6666);

            // Start a new thread to handle receiving messages from the server
            Thread receiveMessageThread = new Thread(() -> {
                try {
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    String serverResponse;
                    while (true) {
                        serverResponse = dis.readUTF();
                        chatArea.append("Server says: " + serverResponse + "\n");
                        if (serverResponse.equals("stop")) {
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveMessageThread.start();

            // Wait for the receiveMessageThread to finish
            receiveMessageThread.join();

            // Close resources
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToServer(String message) {
        try {
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            dout.writeUTF(message);
            dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientGUI());
    }
}

