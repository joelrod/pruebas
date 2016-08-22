package controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.Session;
//Importantes
import app.ChatMemory;
import model.MessageWs;
import ws.MessageEndPoint;

@ManagedBean(name="chatController")
@SessionScoped
public class ChatController implements Serializable{

	private static final long serialVersionUID = -3770573459254222700L;
	
	private boolean createUserPanel;
	
	private String nickname;
	
	private String textMessage;
	
	private List<String> allNicknames;
        
         //private String ruta = "ws://localhost:8080/chatWS/sendmsg";
	
	public ChatController() {
		this.createUserPanel = true;
		this.nickname = null;
		this.allNicknames = ChatMemory.allOnlines;
	}
	
	public String addUser() throws IOException, EncodeException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		this.nickname = this.nickname.trim();
		if (this.nickname.contains(" ")){
			System.err.println("*** No se puede agregar usuario con espacio en blanco");
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"No se puede agregar usuario con espacio en blanco", "")); 
			
			return null;
		}else if (!this.allNicknames.contains(this.nickname)){
			this.createUserPanel = false;
			this.allNicknames.add(this.nickname);
			
			MessageWs msgWS = new MessageWs();
			msgWS.setSource(this.nickname);
			msgWS.setDestination("all");
			msgWS.setBody("Usuario "+this.nickname+" acaba de entrar");
			msgWS.setOperation("addUser");
			msgWS.setTimestamp(new Date());
			this.sendMsgWSBroadcast(msgWS);
			// Guardando el ultimo usuario en linea
			// Para add o atributo en la session WS
			ChatMemory.lastUserOnline = getNickname();
			
			return "chat.xhtml?faces-redirect=true";
		}else{
			System.err.println("*** Ya existe un usuario con ese apellido");
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Ya existe un usuario con ese apellido", "")); 
			return null;
		}
	}
	
	public String logoutUser() throws IOException, EncodeException{
		this.createUserPanel = true;
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();
		return "index.xhtml?faces-redirect=true";
	}
	
	/**
	 * Manda msg broadcast para todas las sesiones en la lista estatica 
	 * 
	 * @param msgws JSON da MSG
	 * @throws IOException
	 * @throws EncodeException
	 */
	private void sendMsgWSBroadcast(MessageWs msgws) throws IOException, EncodeException{
		List<Session> sessions = MessageEndPoint.getSessions();
		for (Session s : sessions){
			if (s.isOpen()) {
				s.getBasicRemote().sendObject(msgws);
			}
		}
		System.out.println("Mensaje enviado para los usuarios- : "+msgws.getOperation());
	}
		
	public void insertManyUsers(){
		for (int i = 0; i < 100; i++) {
			this.allNicknames.add("teste"+i);
		}
	}
	
	// GET AND SET
	public boolean isCreateUserPanel() {
		return createUserPanel;
	}

	public void setCreateUserPanel(boolean createUserPanel) {
		this.createUserPanel = createUserPanel;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public List<String> getAllNicknames() {
		return allNicknames;
	}

	public void setAllNicknames(List<String> allNicknames) {
		this.allNicknames = allNicknames;
	}

	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

//    public String getRuta() {
//        return ruta;
//    }

}
