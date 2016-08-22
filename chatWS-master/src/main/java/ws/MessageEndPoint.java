package ws;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import app.ChatMemory;
import model.MessageWs;

//Importante definir los Encoder y Decoders, la comunicacion entre el cliente y el websocket es por JSON
@ServerEndpoint(value = "/sendmsg", encoders = MessageWSEncoder.class, decoders = MessageWSDecoder.class)
public class MessageEndPoint implements Serializable {

    private static final long serialVersionUID = -3538397497899905150L;

    private static ArrayList<Session> sessions = new ArrayList<Session>();

    public MessageEndPoint() {
    }

    @OnMessage
    public void messageReceiver(Session session, MessageWs msgWS) {
        try {
            String operationMsgWS = msgWS.getOperation();
            if (operationMsgWS.equals("addNicknameSession")) {
                // Agregando el nuevo usuario
                session.getUserProperties().put("nickname", msgWS.getSource());
            } else if (operationMsgWS.equals("sendText")) {
                // ---- UNICAST				
                String userSource = (String) session.getUserProperties().get("nickname");
                if (msgWS.getBody().startsWith("@")) {
                    String[] brokenMsgBody = msgWS.getBody().split(" ");
                    // Destinatario
                    String userDestination = brokenMsgBody[0].replace("@", "");
                    msgWS.setDestination(userDestination);
                    // 
                    if (userSource.equals(userDestination)) {
                        this.sendUnicastMSGToUserSource(msgWS, session);

                    } else if (ChatMemory.allOnlines.contains(userDestination)) {
                        this.sendUnicastMSG(msgWS, userDestination, session);

                    } else {
                        // Envia a msg de error para usuario remetente
                        msgWS.setBody("Este usuario ya existe: " + userDestination);
                        this.sendUnicastMSGToUserSource(msgWS, session);
                        System.err.println("Este usuario ya existe: " + userDestination);
                    }

                } else {
                    // ---- BROADCAST
                    this.sendBroadCastMsg(msgWS);
                }
            }

        } catch (IOException | EncodeException e) {
            System.err.println("***** Diablos, extistió un error ¡¡¡¡ : " + e.getMessage());
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Sesion abierta ID : " + session.getId());
        sessions.add(session);
        session.getUserProperties().put("nickname", ChatMemory.lastUserOnline);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) throws IOException, EncodeException {
        System.out.println("Fecha ID: " + session.getId());
        System.out.println(closeReason);
        String nickname = (String) session.getUserProperties().get("nickname");

        ChatMemory.allOnlines.remove(nickname);
        sessions.remove(session);

        MessageWs msgWS = new MessageWs();
        msgWS.setSource(nickname);
        msgWS.setDestination("all");
        msgWS.setBody("Usuario " + nickname + " acaba de salir...");
        msgWS.setOperation("logoutUser");
        msgWS.setTimestamp(new Date());
        this.sendBroadCastMsg(msgWS);

    }

    @OnError
    public void onError(Session session, Throwable t) throws IOException, EncodeException {
        System.out.println("*** Error Event en endPoint");
        t.printStackTrace();
    }

    /**     
     * @param msgWS JSON da MSG
     * @param destination Usuario Destinatario
     * @param session Sessao WS do usuario Remetente
     * @throws EncodeException
     * @throws IOException
     */
    public void sendUnicastMSG(MessageWs msgWS, String destination, Session session) throws IOException, EncodeException {
        for (Session s : sessions) {
            if (s.isOpen() && s.getUserProperties().containsValue(destination)) {
                msgWS.setDestination(destination);
                s.getBasicRemote().sendObject(msgWS);
                
                session.getBasicRemote().sendObject(msgWS);
                System.out.println("Mensaje enviado para : " + destination);
                break;
            }
        }
    }

    /**
     *
     * @param msgWS JSON da MSG
     * @param session Sessao WS do usuario Remetente
     * @throws EncodeException
     * @throws IOException
     */
    public void sendUnicastMSGToUserSource(MessageWs msgWS, Session session) throws IOException, EncodeException {
        String userSource = (String) session.getUserProperties().get("nickname");
        msgWS.setSource(userSource);
        session.getBasicRemote().sendObject(msgWS);
        System.out.println("Mensaje enviado a : " + userSource);
    }

    /**
     * Envia msg para todos os usuarios
     *
     * @param msgWS JSON da MSG
     * @throws IOException
     * @throws EncodeException
     */
    public void sendBroadCastMsg(MessageWs msgWS) throws IOException, EncodeException {
        for (Session s : sessions) {
            if (s.isOpen()) {
                s.getBasicRemote().sendObject(msgWS);
            }
        }
        System.out.println("Mensaje enviado para todos....");
    }

    // GET AND SET
    public static ArrayList<Session> getSessions() {
        return sessions;
    }

    public static void setSessions(ArrayList<Session> sessions) {
        MessageEndPoint.sessions = sessions;
    }

}
