package de.htw.ai.kbe.SongsServlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.xml.XmlMapper;

public class SongServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private String mySignature = null;
	private List<Song> songList = null;
	private Map<Integer, Song> songDB = null;
	private ObjectMapper objectMapper;
	
	private AtomicInteger currentID = new AtomicInteger();;
	
	public void init(ServletConfig servletConfig) throws ServletException {
		objectMapper = new ObjectMapper();
		songDB = new HashMap<Integer, Song>();
		InputStream input = getClass().getClassLoader().getResourceAsStream("/songs.json");
		
		try {
			songList = (List<Song>) objectMapper.readValue(input, new TypeReference<List<Song>>(){});
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i=0; i<songList.size();i++){
			songDB.put(songList.get(i).getId(), songList.get(i));
		}
		this.mySignature = servletConfig.getInitParameter("signature");
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// alle Parameter (keys)
		
		Enumeration<String> paramNames = request.getParameterNames();
		
		//String responseStr = "";
		String param = "";
		while (paramNames.hasMoreElements()) {
			param = paramNames.nextElement();
			//responseStr = responseStr + param + "=" + request.getParameter(param) + "\n";
			if(param.equals("songID")){
				if(request.getParameter(param).matches("[0-9]*"))
				{
					int id=Integer.valueOf(request.getParameter(param));
					response.setContentType(request.getHeader("accept"));
					try (PrintWriter out = response.getWriter()) {
						if(songDB.get(id)!=null)
							if(request.getHeader("accept").equals("application/json")){
								out.print(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(songDB.get(id)));
							}
							else if(request.getHeader("accept").equals("application/xml")){
								XmlMapper mapper = new XmlMapper();
								out.print(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(songDB.get(id)));
							}
						else
							out.print("ID wurde nicht gefunden.");
					}
				}
				else {
					response.setContentType("text/plain");
	    			try (PrintWriter out = response.getWriter()){
	    				out.print("Parameterfehler");
	    			}
					
				}
			}
			else if(param.equals("all")){
				response.setContentType(request.getHeader("accept"));
				try (PrintWriter out = response.getWriter()) {
					if(request.getHeader("accept").equals("application/json")){
						out.print(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(songDB));
					}
					else if(request.getHeader("accept").equals("application/xml")){
						XmlMapper mapper = new XmlMapper();
						out.print(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(songDB));
					}
				}
			}
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		BufferedReader payloadReader = request.getReader();
		String payload = payloadReader.readLine();
		if(payload!=null){
			int id = findId();
			Song song = (Song) objectMapper.readValue(payload, new TypeReference<Song>(){});
			song.setId(id);
			songList.add(song);
			songDB.put(songList.get(id-1).getId(), songList.get(id-1));
			response.setContentType("text/plain");
			
			try (PrintWriter out = response.getWriter()) {
				out.println("Song '"+song.getTitle()+"' wurde mit der ID: '" +currentID + "' hinzugefügt.");
			}
			
			try {
				objectMapper.writerWithDefaultPrettyPrinter().writeValue(new FileOutputStream(new File(getClass().getResource("/songs.json").toURI())), songList);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		else {
			response.setContentType("text/plain");
			try (PrintWriter out = response.getWriter()) {
				out.println("Payload ist leer; konnte POST-Request nicht ausführen.");
			}
		}
		
	}
	
	public int findId() {
		int ID=1;
		currentID.set(ID);
		while (songDB.containsKey(ID)) {
			ID=currentID.incrementAndGet();
		}
		return ID;
		
	}
	
	protected String getSignature () {
		return this.mySignature;
	}
}