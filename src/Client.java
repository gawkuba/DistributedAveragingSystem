import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java Client <host> <port>");
            System.exit(1);
        }

        String host = args[0];
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Port should be an integer.");
            return;
        }

        try (DatagramSocket socket = new DatagramSocket();
            Scanner scanner = new Scanner(System.in)) {

            InetAddress address = InetAddress.getByName(host);

            while (true) {
                System.out.print("Enter a message to send (or 'exit' to quit): ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                byte[] buffer = input.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(packet);
                System.out.println("Packet sent: " + input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}