import javax.microedition.lcdui.TextField;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class Options {

  static RecordStore recOpt;

  public Options() throws RecordStoreException {
    recOpt=RecordStore.openRecordStore("Options", true);
  }
  
  public void close() {
    try {
      recOpt.closeRecordStore();
    } catch (RecordStoreNotOpenException e) {
      e.printStackTrace();
    } catch (RecordStoreException e) {
      e.printStackTrace();
    }
    recOpt = null;
  }

  public void getValue(TextField sMSNumber, TextField sMSText, TextField serverPath) throws Exception {
    if (recOpt.getNumRecords() != 0){
      sMSNumber.setString(new String(recOpt.getRecord(1)));
      sMSText.setString(new String(recOpt.getRecord(2)));
      serverPath.setString(new String(recOpt.getRecord(3)));
    }
  }

  public void setValue(TextField sMSNumber, TextField sMSText, TextField serverPath) throws Exception {
    // если записать null, то при чтении произойдёт Exception
    String _smsNumber = (sMSNumber.getString() == null)?"":sMSNumber.getString();
    String _smsText = (sMSText.getString() == null)?"":sMSText.getString();
    String _serverPath = (serverPath.getString() == null)?"":serverPath.getString();
    if (recOpt.getNumRecords()!=0){
      recOpt.setRecord(1, _smsNumber.getBytes(), 0, _smsNumber.length());
      recOpt.setRecord(2, _smsText.getBytes(), 0, _smsText.length());
      recOpt.setRecord(3, _serverPath.getBytes(), 0, _serverPath.length());
    } else {
      recOpt.addRecord(_smsNumber.getBytes(), 0, _smsNumber.length());
      recOpt.addRecord(_smsText.getBytes(), 11, _smsText.length());
      recOpt.addRecord(_serverPath.getBytes(), 37, _serverPath.length());
    }
  }

}
