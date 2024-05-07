package pilottageMQTT;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Utility class to get different information about the EV3 robot
 */
public class Utils {

	/**
	 * Generate a unique client ID for the MQTT client from the MAC address of the EV3 robot
	 * @return The generated client ID
	 * @throws Exception If the MAC address of the EV3 robot cannot be retrieved
	 */
	public static StringBuilder generateClientID() throws Exception {
		InetAddress localHost = getLocalIPAddress();
		NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
		byte[] mac = ni.getHardwareAddress();
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			stringBuilder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
		}
		return stringBuilder;
	}

	/**
	 * Get the local IP address of the EV3 robot
	 * @return The local IP address of the EV3 robot
	 * @throws Exception If the local IP address of the EV3 robot cannot be retrieved
	 */
	private static InetAddress getLocalIPAddress() throws Exception {
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

		while (interfaces.hasMoreElements()) {
			NetworkInterface iface = interfaces.nextElement();

			if (iface.isUp() && !iface.isLoopback() && !iface.isVirtual()) {
				Enumeration<InetAddress> addresses = iface.getInetAddresses();

				while (addresses.hasMoreElements()) {
					InetAddress addr = addresses.nextElement();

					if (addr.getHostAddress().matches("\\d+\\.\\d+\\.\\d+\\.\\d+")
							&& !addr.isLoopbackAddress()) {
						return addr;
					}
				}
			}
		}

		throw new Exception("No suitable network interface found");
	}

	/**
	 * Print a message to the standard output (The LCD screen and the LeJOS Control Center)
	 * @param message The message to print
	 */
	public static void print(String message) {
		System.out.println(message);
	}
}
