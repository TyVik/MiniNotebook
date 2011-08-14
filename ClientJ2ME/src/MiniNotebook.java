import java.io.IOException;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class MiniNotebook extends MIDlet implements CommandListener, ItemCommandListener {

  private boolean midletPaused = false;

  private java.util.Hashtable __previousDisplayables = new java.util.Hashtable();

  private Form MainForm;
  private ChoiceGroup choiceGroup;

  private Form About;
  private StringItem stringItem = new StringItem("Notebook v 0.1", "Любимому Солнышку на День рождения :*");
  
  private Form AddForm;
  
  private Alert alert;
  private TextField textField = new TextField("Новая запись", null, 32, TextField.ANY);
  private Command exitCommand = new Command("Выход", Command.EXIT, 0);
  private Command aboutCommand = new Command("О программе", Command.HELP, 0);
  private Command backCommand = new Command("Назад", Command.BACK, 0);
  private Command delCommand = new Command("Удалить", Command.ITEM, 0);
  private Command addCommand = new Command("Добавить", Command.ITEM, 0);
  private Command okCommand = new Command("OK", Command.OK, 0);
  private Command cancelCommand = new Command("Отмена", Command.CANCEL, 0);
  private Command smsCommand = new Command("Отправить СМС", Command.ITEM, 0);
  private Command optionsCommand = new Command("Настройки", Command.ITEM, 0);
  
  private Form Options;
  private TextField SMSText = new TextField("Текст СМС", "", 25, TextField.ANY);
  private TextField SMSNumber = new TextField("Номер СМС +7", "", 10, TextField.NUMERIC);
  private TextField ServerPath = new TextField("Адрес сервера", "", 50, TextField.ANY);

  private Options options;
  private Server server;

  public MiniNotebook() {
    try {
      options = new Options();
      options.getValue(SMSNumber, SMSText, ServerPath);
    } catch (Exception e) {
      showAlert("Ошибка при инициализации настроек\n" + e.getMessage());
    }
    server = new Server(ServerPath.getString());
  }

  private void switchToPreviousDisplayable() {
    Displayable __currentDisplayable = getDisplay().getCurrent();
    if (__currentDisplayable != null) {
      Displayable __nextDisplayable = (Displayable) __previousDisplayables.get(__currentDisplayable);
      if (__nextDisplayable != null) {
        switchDisplayable(null, __nextDisplayable);
      }
    }
  }

  public void switchDisplayable(Alert alert, Displayable nextDisplayable) {
    Display display = getDisplay();
    Displayable __currentDisplayable = display.getCurrent();
    if (__currentDisplayable != null  &&  nextDisplayable != null) {
      __previousDisplayables.put(nextDisplayable, __currentDisplayable);
    }
    if (alert == null) {
      display.setCurrent(nextDisplayable);
    } else {
      display.setCurrent(alert, nextDisplayable);
    }
  }
  
  public void commandAction(Command command, Displayable displayable) {
    if (displayable == About) {
      if (command == backCommand) {
        switchToPreviousDisplayable();
      }
    } else if (displayable == AddForm) {
      if (command == cancelCommand) {
        switchToPreviousDisplayable();
      } else if (command == okCommand) {
        addElement();
        switchDisplayable(null, getMainForm());
      }
    } else if (displayable == MainForm) {
      if (command == aboutCommand) {
        switchDisplayable(null, getAbout());
      } else if (command == exitCommand) {
        exitMIDlet();
      } else if (command == optionsCommand) {
        switchDisplayable(null, getOptions());
      } else if (command == smsCommand) {
        try {
          server.sendSMS(SMSNumber.getString(), SMSText.getString());
    	  } catch (Exception e) {
    	    showAlert("Ошибка при тправке СМС\n" + e.getMessage());
    	  }
      }
    } else if (displayable == alert) {
      if (command == backCommand) {
        switchToPreviousDisplayable();
      } else if (command == exitCommand) {
        exitMIDlet();
      }
    } else if (displayable == Options) {
      if (command == okCommand) {
        try {
          options.setValue(SMSNumber, SMSText, ServerPath);
          if (server.path != ServerPath.getString()) {
            server.path = ServerPath.getString();
            fillList();
          }
        } catch (Exception e) {
          showAlert("Ошибка при сохранении настроек\n" + e.getMessage());
        }
      }
      switchToPreviousDisplayable();
    }
  }
  
  public void showAlert(String text) {
    Alert a = getAlert();
    a.setString(text);
    switchDisplayable(null, a);
  }

  public Form getOptions() {
    if (Options == null) {
      Options = new Form("Настройки");
      Options.addCommand(cancelCommand);
      Options.addCommand(okCommand);
      Options.append(SMSNumber);
      Options.append(SMSText);
      Options.append(ServerPath);
      Options.setCommandListener(this);
    }
    return Options;
  }

  public Form getMainForm() {
    if (MainForm == null) {
      MainForm = new Form("\u0417\u0430\u043F\u0438\u0441\u043D\u0430\u044F \u043A\u043D\u0438\u0436\u043A\u0430", new Item[] { getChoiceGroup() });
      MainForm.addCommand(aboutCommand);
      MainForm.addCommand(exitCommand);
      MainForm.addCommand(smsCommand);
      MainForm.addCommand(optionsCommand);
      MainForm.setCommandListener(this);
    }
    return MainForm;
  }

  public Form getAbout() {
    if (About == null) {
      About = new Form("\u041E \u043F\u0440\u043E\u0433\u0440\u0430\u043C\u043C\u0435", new Item[] { stringItem });
      About.addCommand(backCommand);
      About.setCommandListener(this);
    }
    return About;
  }

  public Alert getAlert() {
    if (alert == null) {
      alert = new Alert("alert", null, null, AlertType.ALARM);
      alert.addCommand(backCommand);
      alert.addCommand(exitCommand);
      alert.setCommandListener(this);
      alert.setTimeout(Alert.FOREVER);
    }
    return alert;
  }

  public Form getAddForm() {
    if (AddForm == null) {
      AddForm = new Form("form", new Item[] { textField });
      AddForm.addCommand(okCommand);
      AddForm.addCommand(cancelCommand);
      AddForm.setCommandListener(this);
    }
    return AddForm;
  }
  
  public ChoiceGroup getChoiceGroup() {
    if (choiceGroup == null) {
      choiceGroup = new ChoiceGroup("\u041E\u0431\u0449\u0438\u0439 \u0441\u043F\u0438\u0441\u043E\u043A", Choice.MULTIPLE);
      choiceGroup.addCommand(addCommand);
      choiceGroup.addCommand(delCommand);
      choiceGroup.setItemCommandListener(this);
      choiceGroup.setSelectedFlags(new boolean[] {  });
    }
    return choiceGroup;
  }

  public void commandAction(Command command, Item item) {
    if (item == choiceGroup) {
      if (command == addCommand) {
        switchDisplayable(null, getAddForm());
      } else if (command == delCommand) {
        delElements();
      }
    }
  }

  public Display getDisplay() {
    return Display.getDisplay(this);
  }

  private void initialize() {
  }

  public void fillList() {
    try {
      String[] strs = server.getFromServer();
      for (int i = 0; i < strs.length; i++) {
        choiceGroup.append(strs[i], null);
      }
    } catch (IOException e) {
      showAlert("Не могу получить список с сервера\n" + e.getMessage());
    }
  }
  
  public void startMIDlet() {
    switchDisplayable(null, getMainForm());
    fillList();
  }
  
  public void exitMIDlet() {
    options.close(); // нет в J2ME деструкторов
    switchDisplayable(null, null);
    destroyApp(true);
    notifyDestroyed();
  }

  public void startApp() {
    if (midletPaused) {
      resumeMIDlet();
    } else {
      initialize();
      startMIDlet();
    }
    midletPaused = false;
  }

  public void resumeMIDlet() {
  }

  public void pauseApp() {
    midletPaused = true;
  }

  public void destroyApp(boolean unconditional) {
  }

  public void delElements() {
    String item = "";
    int i = 0;
    while (i < choiceGroup.size()) {
      if (choiceGroup.isSelected(i)) {
        item = item + "itemsSelected%5B%5D=" + choiceGroup.getString(i) + "&";
        choiceGroup.delete(i);
      } else {
        i++;
      }
    }
    try {
      server.sendRequest(item);
    } catch (IOException e) {
      showAlert("Ошибка в удалении с сервера\n" + e.getMessage());
   }
  }

  public void addElement() {
    try {
      server.addToServer(textField.getString());
      choiceGroup.append(textField.getString(), null);
    } catch (IOException e) {
      showAlert("Ошибка при добавлении на сервер\n" + e.getMessage());
   } finally {
      textField.setString("");
    }
  }

}