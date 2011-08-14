import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Stack;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;


public class Server {
  
  public String path;

  public Server(String APath){
    path = APath; 
  }

  public void sendRequest(String item) throws IOException {
    HttpConnection c = null;
    OutputStream os = null;
    int rc;
    try {
      c = (HttpConnection) Connector.open("http://"+path+"/index.php?r=share/index");
      c.setRequestMethod(HttpConnection.POST);
      c.setRequestProperty("User-Agent", "Profile/MIDP-2.0 Configuration/CLDC-1.0");
      c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      c.setRequestProperty("Content-Length", Integer.toString(item.length()));
      os = c.openOutputStream();
      os.write(item.getBytes("utf-8"));
      os.flush();
      rc = c.getResponseCode();
      if (rc != HttpConnection.HTTP_OK) {
        throw new IOException("HTTP response code: " + rc);
      }
    } finally {
      if (os != null) {
        os.close();
      }
      if (c != null) {
        c.close();
      }
    }
  }

  // не нашёл стандартной функции 
  public String[] split(String toSplit, char delim, boolean ignoreEmpty) {
    StringBuffer buffer = new StringBuffer();
    Stack stringStack = new Stack();
    for (int i = 0; i < toSplit.length(); i++) {
      if (toSplit.charAt(i) != delim) {
        buffer.append((char) toSplit.charAt(i));
      } else {
        if (buffer.toString().trim().length() == 0 && ignoreEmpty) {
        } else {
          stringStack.addElement(buffer.toString());
        }
        buffer = new StringBuffer();
      }
    }
    if (buffer.length() != 0) {
      stringStack.addElement(buffer.toString());
    }
    String[] split = new String[stringStack.size()];
    for (int i = 0; i < split.length; i++) {
      split[split.length - 1 - i] = (String) stringStack.pop();
    }
    stringStack = null;
    buffer = null;
    return split;
  }

  public String[] getFromServer() throws IOException {
    HttpConnection c = null;
    InputStream is = null;
    InputStreamReader isr = null;
    String result = "";
    try {
      c = (HttpConnection) Connector.open("http://"+path+"/index.php?r=share/getList");
      is = c.openInputStream();
      isr = new InputStreamReader(is, "utf-8");
      int ch;
      while ((ch = isr.read()) != -1) {
        result += (char) ch;
      }
    } catch (IllegalArgumentException e) {
      throw new IOException("Не задан адрес сервера");
    } finally {
      if (is != null) {
        is.close();
      }
      if (c != null) {
        c.close();
      }
    }
    return split(result, '&', false);
  }

  public void addToServer(String item) throws IOException {
    sendRequest("Share[Text]=" + item + "\n");
  }

  public void sendSMS(String number, String text) throws IOException {
    String address = "sms://+7"+number;
    MessageConnection smsconn = null;
    try {
      smsconn = (MessageConnection) Connector.open(address);
      TextMessage txtmessage = (TextMessage) smsconn.newMessage(MessageConnection.TEXT_MESSAGE);
      txtmessage.setAddress(address);
      txtmessage.setPayloadText(text);
      smsconn.send(txtmessage);
    } finally {
      smsconn.close();
    }
  }

}
