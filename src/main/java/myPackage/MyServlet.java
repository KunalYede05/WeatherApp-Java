package myPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//API Setup
		String apiKey="2d8d0a04aa80990677b6e9e517b3ef22";
		
		//get the city from the form input
		String city=request.getParameter("city");
		
		//create URL from the open weather api
		String apiUrl="https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
		
		//API Integration
		URL url=new URL(apiUrl);
		HttpURLConnection connection=(HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		//Reading the data from network
		InputStream inputStream= connection.getInputStream();
		InputStreamReader reader=new InputStreamReader(inputStream);
		
		//Want to store in the string
		StringBuilder responseContent = new StringBuilder();
		
		//Create scanner object to take the input from the reader
		Scanner scanner = new Scanner(reader);
		
		while(scanner.hasNext()) {
			responseContent.append(scanner.nextLine());
		}
		
		scanner.close();
//		System.out.println(responseContent);
		
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
		System.out.println(jsonObject);
		
		//date and Time
	    long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
	    String date = new Date(dateTimestamp).toString();
	    
	    //Temperature
	    double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
	    int temperatureCelcius = (int) (temperatureKelvin - 273.15);
	    
	    //Humidity
	    int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
	    
	    //wind Speed
	    double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
	    
	    //Weather Condition
	    String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
	    
	    //set the data as request attributes (for sending to the JSP Page)
	    request.setAttribute("date", date);
	    request.setAttribute("city", city);
	    request.setAttribute("temperature", temperatureCelcius);
	    request.setAttribute("weatherCondition", weatherCondition);
	    request.setAttribute("humidity", humidity);
	    request.setAttribute("windSpeed", windSpeed);
	    request.setAttribute("weatherData", responseContent.toString());
	    
	    connection.disconnect();
	    
	    //forward the request to the weather.jsp page for rendering
	    request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
