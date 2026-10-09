package com.perisic.tomato.engine;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

import javax.imageio.ImageIO;

/**
 * Game that interfaces to an external Server to retrieve a game. 
 * A game consists of an image and an integer that denotes the solution of this game. 
 * 
 * @author Marc Conrad
 *
 */
public class GameServer {

	int offlineCount = 0;

	/**
	 * Basic utility method to read string for URL.
	 */

	private static String readUrl(String urlString)  {
		try {
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(6000);
			conn.setReadTimeout(6000);
			InputStream inputStream = conn.getInputStream();

			// Choose anyone of
			// https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
			// to convert InputStream to String.
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
			return result.toString("UTF-8");
		} catch (Exception e) {
			/* To do: proper exception handling when URL cannot be read. */
			System.out.println("An Error occured: " + e.toString());
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Retrieves a random game from the web site.
	 * Falls back to a local image if the api is down so the game still runs.
	 * @return a random game or null if a game cannot be found. 
	 */
	public Game getRandomGame() {
		// See http://marconrad.com/uob/tomato for details of usage of the api. 
		
		String tomatoapi = "https://marcconrad.com/uob/tomato/api.php?out=csv&base64=yes";
		String dataraw = readUrl(tomatoapi);
		if (dataraw == null || !dataraw.contains(",")) {
			System.out.println("api down, using offline image");
			return getOfflineGame();
		}
		try {
			String[] data = dataraw.trim().split(",");
			byte[] decodeImg = Base64.getDecoder().decode(data[0].trim());
			ByteArrayInputStream quest = new ByteArrayInputStream(decodeImg);
			int solution = Integer.parseInt(data[1].trim());
			BufferedImage img = ImageIO.read(quest);
			if (img == null) {
				return getOfflineGame();
			}
			return new Game(img, solution);
		} catch (Exception e) {
			System.out.println("bad api data: " + e.getMessage());
			return getOfflineGame();
		}
	}

	Game getOfflineGame() {
		try {
			String name = "/tomato1.png";
			if (offlineCount++ % 2 == 1) {
				name = "/tomato2.png";
			}
			InputStream in = GameServer.class.getResourceAsStream(name);
			BufferedImage img = ImageIO.read(in);
			// offline copies both have solution 1, same as basic example
			return new Game(img, 1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
