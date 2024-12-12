import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

// welcome to my university midterm project
// this simple program is working in two modes master and slave
// it was a really basic networks project
// some of its specifications are in code to explain my thinking process

public class DAS {
    // basic variables declaration
    private static final int BUFFER_SIZE = 1024;
    private final int port;
    private final int number;
    private final List<Integer> capturedNumbers = new ArrayList<>();
    private DatagramSocket socket;

    // constructor
    public DAS(int port, int number) {
        this.port = port;
        this.number = number;
    }
    // tries being a master but when socket is taken it goes into slave
    public void start() {
        try {
            socket = new DatagramSocket(port);
            Master();
        } catch (SocketException exe) {
            Slave();
        }
    }
    // master logic here
    private void Master() {
        // sends a message what mode it is running
        System.out.println("Running in master mode");
        // stores initial number to list that contains captured numbers
        capturedNumbers.add(number);
        // initializing a simple byte array called buffer that acts like a storage kinda
        byte[] buffer = new byte[BUFFER_SIZE];

        // main loop for master mode
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength()).trim();

                if (message.equals("0")) {
                    double average = calculateAverage();
                    System.out.println("Average: " + average);
                    broadcastMessage(String.valueOf(average));
                } else if (message.equals("-1")) {
                    System.out.println("Received -1, shutting down");
                    broadcastMessage("-1");
                    socket.close();
                    break;
                } else {
                    try {
                        if (message.contains(".")) {
                            double receivedNumber = Double.parseDouble(message);
                            System.out.println("Received: " + receivedNumber);
                        } else {
                            int receivedNumber = Integer.parseInt(message);
                            System.out.println("Received: " + receivedNumber);
                            capturedNumbers.add(receivedNumber);
                        }
                    } catch (NumberFormatException exe) {
                        System.err.println("Invalid number received: " + exe.getMessage());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // pretty straight forward announces slave mode then passes his initial number then closes
    private void Slave() {
        System.out.println("Running in slave mode");
        try {
            socket = new DatagramSocket();
            String message = String.valueOf(number);
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getLocalHost(), port);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    // calculates arithmetic average obviously
    private double calculateAverage() {
        int summed = 0;
        int counted = 0;
        for (int numba : capturedNumbers) {
            if (numba != 0) {
                summed += numba;
                counted++;
            }
        }
        return counted == 0 ? 0 : (double) summed / counted;
    }

    // method for broadcasting messages
    private void broadcastMessage(String message) {
        try {
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName("255.255.255.255"), port);
            socket.send(packet);
        } catch (IOException exe) {
            exe.printStackTrace();
        }
    }

    // main function
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("WRONG ARGUMENTS! use this template java DAS <port> <number>");
            System.exit(1);
        }

        try {
            int port = Integer.parseInt(args[0]);
            int number = Integer.parseInt(args[1]);
            DAS das = new DAS(port, number);
            das.start();
        } catch (NumberFormatException exe) {
            System.err.println("Not good port or number");
            System.exit(1);
        }
    }
}