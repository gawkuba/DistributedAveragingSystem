# DAS Communication Protocol

## Functionalities
1. Operate in two modes: Master and Slave.
2. In Master mode:
   - The application should "remember" the value passed as the `<number>` parameter and then, in a loop, receive messages via a socket on `<port>`.
   - Each subsequent number received should be added to a pool along with the initial parameter. Upon receiving the message `0`, it should calculate and display the average on the console and broadcast this value to connected devices on the same port.
   - When receiving `-1`, the application should display `-1` on the console, broadcast a shutdown message, and terminate.
3. In Slave mode:
   - The application should send the initial `<number>` to the Master on `<port>` and then terminate.

## How to Run
### 4.1 Compilation and Execution:
- Compile the Java file with the command:
  ```
  javac <FileName>.java
  ```
- Run the compiled program with the required startup parameters:
  ```
  java <FileName> <port> <number>
  ```

### 4.2 Connecting to the Program:
- A separate `Client` program is provided to send packets to DAS.
- Compile the Client file:
  ```
  javac Client.java
  ```
- Run the compiled Client program with the necessary parameters:
  ```
  java Client <host> <port>
  ```
  - Use `localhost` as the `<host>` for connections on the same machine.
  - Use `255.255.255.255` for local network connections.
  - For remote connections, ensure port forwarding or tunneling (e.g., Tailscale, Cloudflare) is set up and provide the appropriate IP in the `<host>` field.

## Achievements
- Successfully implemented all features except broadcasting messages to all devices on a specific port (this was not thoroughly tested).

## Classes
### DAS
#### Constructor
**DAS(int port, int number)**
- **port**: UDP port for communication.
- **number**: Initial value to store or send depending on the mode.

#### Methods
**start()**
- Tries to initialize a UDP socket on the specified port:
  - If successful, switches to **Master** mode.
  - If the port is busy, switches to **Slave** mode.

**master()**
- Handles logic for Master mode:
  1. Logs entry into Master mode.
  2. Stores the initial `number` in the `capturedNumbers` list.
  3. Continuously listens for messages:
     - On receiving `0`, calculates the average of stored numbers, logs it, and broadcasts the value.
     - On receiving `-1`, logs shutdown, broadcasts `-1`, and closes the socket.
     - Parses other messages as integers or floats, logging and storing valid values.

**slave()**
- Handles logic for Slave mode:
  1. Logs entry into Slave mode.
  2. Sends the initial `number` to the Master on the specified port.
  3. Closes the socket and terminates.

**calculateAverage()**
- Calculates the arithmetic mean of all non-zero values stored in `capturedNumbers`.
  - Returns `0` if the list is empty or the calculated average otherwise.

**broadcastMessage(String message)**
- Sends a broadcast message to all devices in the network:
  - Uses the broadcast address (`255.255.255.255`).
  - Sends the provided `message` on the specified `port`.

**main(String[] args)**
- Entry point of the program:
  - Parses command-line arguments to extract `port` and `number`.
  - Validates inputs and initializes the DAS instance.
  - Logs errors and displays usage instructions for invalid arguments.

### Client
#### main()
- Main function of the Client:
  1. Parses command-line arguments `host` and `port`.
  2. Creates a UDP socket and connects to the specified address.
  3. In a loop:
     - Allows the user to enter a message to send.
     - Exits the program if the user types `exit`.
  4. Sends entered messages as UDP packets to the DAS application.
  5. Closes the socket and terminates the program.

