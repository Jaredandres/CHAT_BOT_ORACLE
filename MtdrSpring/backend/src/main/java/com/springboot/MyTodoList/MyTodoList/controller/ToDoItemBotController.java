package com.springboot.MyTodoList.controller;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.model.ToDoTeam;
import com.springboot.MyTodoList.model.ToDoUser;
import com.springboot.MyTodoList.service.ToDoItemService;
import com.springboot.MyTodoList.service.ToDoTeamService;
import com.springboot.MyTodoList.service.ToDoUserService;
import com.springboot.MyTodoList.util.BotHelper;
import com.springboot.MyTodoList.util.BotLabels;
import com.springboot.MyTodoList.util.BotMessages;

public class ToDoItemBotController extends TelegramLongPollingBot {

    private Map<Long, Boolean> userState = new HashMap<>();
    private Map<Long, String> userRoles = new HashMap<>();
    private Map<Long, Integer> userTeams = new HashMap<>();  


    private static final Logger logger = LoggerFactory.getLogger(ToDoItemBotController.class);
    private ToDoItemService toDoItemService;
    private ToDoUserService toDoUserService;
    private ToDoTeamService toDoTeamService; 
    private String botName;
    private boolean verifyTask = false;

    public ToDoItemBotController(String botToken, String botName, ToDoItemService toDoItemService, ToDoUserService toDoUserService, ToDoTeamService toDoTeamService) {
        super(botToken);
        logger.info("Bot Token: " + botToken);
        logger.info("Bot name: " + botName);
        this.toDoItemService = toDoItemService;
        this.toDoUserService = toDoUserService;
        this.toDoTeamService = toDoTeamService;  
        this.botName = botName;
    }

    @Override
    public String getBotUsername() {        
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        
        if (update.hasMessage()) {
            String messageTextFromTelegram = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            
            if (!userState.getOrDefault(chatId, false) && !update.getMessage().hasContact()) {
                showMainScreen(chatId); 
                return; 
            }
            
            if (update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();

                if (!verifyTask) {
                    switch (messageText) {
                        case "/start":
                        case "Show Main Screen":
                            showMainMenu(chatId);
                            break;
                        case "/help":
                            sendHelp(chatId);
                            break;
                        case "/team":
                            sendEquipos(chatId);
                            break;
                        case "/hide":
                        case "Realizar pruebas de software":
                        BotHelper.sendMessageToTelegram(chatId, " Verificar que el software cumpla con los estándares de calidad y funcionalidad establecidos mediante la realización de pruebas exhaustivas.", this);
                        case "Hide Main Screen":
                            hideMainScreen(messageText, chatId);
                            break;
                        case "/todolist":
                        case "List All Items":
                        case "MY TODO LIST":
                            listAllItems(chatId);
                            break;
                        case "/additem":
                        case "Add New Item":
                            if ("Administrador".equals(userRoles.get(chatId))) {
                                verifyTask = true;
                                BotHelper.sendMessageToTelegram(chatId, BotMessages.TYPE_NEW_TODO_ITEM.getMessage(), this);
                            } else {
                                BotHelper.sendMessageToTelegram(chatId, "You do not have permissions to use the add tasks feature.", this);
                            }
                            break;
                        case "/user": 
                            sendAllUsers(chatId);
                            break;
                        default:
                            if (userState.getOrDefault(chatId, false)) {
                                
                            } else {
                                BotHelper.sendMessageToTelegram(chatId, "Please use the /start command to use this bot.", this);
                            }
                            break;
                    }
                } else {
                    try {
                        ToDoItem newItem = new ToDoItem();
                        newItem.setDescription(messageText);
                        newItem.setCreation_ts(OffsetDateTime.now());
                        newItem.setDone(false);
                        ResponseEntity entity = addToDoItem(newItem);

                        BotHelper.sendMessageToTelegram(chatId, BotMessages.NEW_ITEM_ADDED.getMessage(), this);
                        verifyTask = false;
                    } catch (Exception e) {
                        logger.error(e.getLocalizedMessage(), e);
                    }
                }
                if (messageTextFromTelegram.indexOf(BotLabels.DONE.getLabel()) != -1) {    
                    DONE(messageTextFromTelegram, chatId);
                }
                if (messageTextFromTelegram.indexOf(BotLabels.UNDO.getLabel()) != -1) {    
                    UNDO(messageTextFromTelegram, chatId);
                }
                if (messageTextFromTelegram.indexOf(BotLabels.DELETE.getLabel()) != -1) {    
                    if ("Administrador".equals(userRoles.get(chatId))) {
                        DELETE(messageTextFromTelegram, chatId);
                    } else {
                        BotHelper.sendMessageToTelegram(chatId, "You do not have permissions to delete tasks.", this);
                    }
                }
            } else if (update.getMessage().hasContact()) {
                handleContactMessage(update, chatId);
            }
        }
    }

    private void sendEquipos(long chatId) {
        List<ToDoTeam> equipos = toDoTeamService.findAllTeams();
        if (equipos.isEmpty()) {
            sendMessageToTelegram(chatId, "No hay equipos registrados en la base de datos.");
            return;
        }
        
        StringBuilder response = new StringBuilder("List of all Teams:\n");
        for (ToDoTeam equipo : equipos) {
            response.append("ID: ").append(equipo.getIdEquipo()).append("\n")
                    .append("Name: ").append(equipo.getNombre()).append("\n")
                    .append("Department: ").append(equipo.getDepartamento()).append("\n");
        }
    
        sendMessageToTelegram(chatId, response.toString());
    }
    

    private void sendHelp(long chatId) {
        sendMessageToTelegram(chatId, "/start : show options");
        sendMessageToTelegram(chatId, "/todolist : show my to-do list");
        sendMessageToTelegram(chatId, "/user : show my team's information");
        sendMessageToTelegram(chatId, "/team : show all team's information");
        sendMessageToTelegram(chatId, "ID-DELETE : delete a task if possible");
        sendMessageToTelegram(chatId, "ID-UNDO : undo the status of a task");
        sendMessageToTelegram(chatId, "ID-DONE : change state of a task as done");
        }

    private void handleContactMessage(Update update, long chatId) {
        Contact contact = update.getMessage().getContact();
        String phoneNumber = contact.getPhoneNumber();
    
        Optional<ToDoUser> userOptional = toDoUserService.findUserByPhoneNumber(phoneNumber);
    
        if (userOptional.isPresent()) {
            ToDoUser user = userOptional.get();
            BotHelper.sendMessageToTelegram(chatId, "Authenticated phone number. Welcome to the system \n UserName: " + user.getNombre(), this);
            BotHelper.sendMessageToTelegram(chatId, "Rol: " + user.getRol(), this);
            BotHelper.sendMessageToTelegram(chatId, "Select /help to see the all opcions", this);
            userState.put(chatId, true);
            userRoles.put(chatId, user.getRol());
            userTeams.put(chatId,user.getIdEquipo());
        } else {
            BotHelper.sendMessageToTelegram(chatId, "Unregistered phone number. Please register your number to use this bot.", this);
            userState.put(chatId, false);
        }
    }
    

    private void sendAllUsers(long chatId) {
        Integer teamId = userTeams.get(chatId);  
        if (teamId == null) {
            sendMessageToTelegram(chatId, "Your team information is not available. Please re-authenticate.");
            return;
        }
        
        List<ToDoUser> users = toDoUserService.getAllToDoUsers().stream()
                                              .filter(user -> user.getIdEquipo() == teamId)
                                              .collect(Collectors.toList());
        
        StringBuilder response = new StringBuilder("Members of your TEAM: \n");
        for (ToDoUser user : users) {
            response.append("ID: ").append(user.getId()).append("\n")
                    .append("Name: ").append(user.getNombre()).append("\n")
                    .append("Email: ").append(user.getCorreo()).append("\n");
        }
    
        sendMessageToTelegram(chatId, response.toString());
    }

    private void sendMessageToTelegram(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(); 
        sendMessage.setChatId(String.valueOf(chatId)); 
        sendMessage.setText(message); 
    
        try {
            execute(sendMessage); 
        } catch (TelegramApiException e) {
            logger.error("Error sending message to Telegram: " + e.getMessage(), e);
        }
    }

    private void showMainScreen(long chatId) {
        SendMessage messageToTelegram = new SendMessage();
        messageToTelegram.setChatId(chatId);
        messageToTelegram.setText("This Chatbot is property of ORACLE, use reserved for its workers.");
        messageToTelegram.setText("Please send your phone number to continue.");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardButton phoneButton = new KeyboardButton("Send my phone number");
        phoneButton.setRequestContact(true);

        KeyboardRow phoneRow = new KeyboardRow();
        phoneRow.add(phoneButton);
        keyboard.add(phoneRow);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        messageToTelegram.setReplyMarkup(keyboardMarkup);

        try {
            execute(messageToTelegram);
        } catch (TelegramApiException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private void showMainMenu(long chatId) {
        SendMessage messageToTelegram = new SendMessage();
        messageToTelegram.setChatId(chatId);
        messageToTelegram.setText("Welcome to the main menu!");
    
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
    
        KeyboardRow row = new KeyboardRow();
        row.add(BotLabels.LIST_ALL_ITEMS.getLabel());
        row.add(BotLabels.ADD_NEW_ITEM.getLabel());
        keyboard.add(row);
    
        row = new KeyboardRow();
        row.add(BotLabels.HIDE_MAIN_SCREEN.getLabel());
        keyboard.add(row);
    
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
    
        
        messageToTelegram.setReplyMarkup(keyboardMarkup);
    
        try {
            execute(messageToTelegram);  
        } catch (TelegramApiException e) {
            logger.error("Failed to send main menu: " + e.getLocalizedMessage(), e);
        }
    }
    
        public void hideMainScreen(String messageTextFromTelegram, long chatId) {
            BotHelper.sendMessageToTelegram(chatId, BotMessages.BYE.getMessage(), this);
        }
    
        public void listAllItems(long chatId) {
            List<ToDoItem> allItems = getAllToDoItems();
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboard = new ArrayList<>();
        
            KeyboardRow mainScreenRowTop = new KeyboardRow();
            mainScreenRowTop.add(BotLabels.SHOW_MENU.getLabel());
            keyboard.add(mainScreenRowTop);
        
            KeyboardRow firstRow = new KeyboardRow();
            firstRow.add(BotLabels.ADD_NEW_ITEM.getLabel());
            keyboard.add(firstRow);
        
        
            List<ToDoItem> activeItems = allItems.stream().filter(item -> !item.isDone()).collect(Collectors.toList());
        
            for (ToDoItem activeItem : activeItems) {
                KeyboardRow currentRowActive = new KeyboardRow();
                currentRowActive.add(activeItem.getDescription());
                currentRowActive.add(activeItem.getID() + BotLabels.DASH.getLabel() + BotLabels.DONE.getLabel());
                keyboard.add(currentRowActive);
            }
        
            List<ToDoItem> doneItems = allItems.stream().filter(ToDoItem::isDone).collect(Collectors.toList());
        
            for (ToDoItem doneItem : doneItems) {
                KeyboardRow currentRowDone = new KeyboardRow();
                currentRowDone.add(doneItem.getDescription());
                currentRowDone.add(doneItem.getID() + BotLabels.DASH.getLabel() + BotLabels.UNDO.getLabel());
                currentRowDone.add(doneItem.getID() + BotLabels.DASH.getLabel() + BotLabels.DELETE.getLabel());
                keyboard.add(currentRowDone);
            }
        
        
            keyboardMarkup.setKeyboard(keyboard);
        
            SendMessage messageToTelegram = new SendMessage();
            messageToTelegram.setChatId(chatId);
            messageToTelegram.setText(BotLabels.MY_TODO_LIST.getLabel());
            messageToTelegram.setReplyMarkup(keyboardMarkup);
        
            try {
                execute(messageToTelegram);
            } catch (TelegramApiException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        
    
        public void DONE(String messageTextFromTelegram, long chatId) {
            String done = messageTextFromTelegram.substring(0,
            messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
                Integer id = Integer.valueOf(done);
    
                try {
    
                    ToDoItem item = getToDoItemById(id).getBody();
                    item.setDone(true);
                    updateToDoItem(item, id);
                    BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_DONE.getMessage(), this);
    
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
        }
        
        public void UNDO(String messageTextFromTelegram, long chatId) {
            String undo = messageTextFromTelegram.substring(0,
                        messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
                Integer id = Integer.valueOf(undo);
    
                try {
    
                    ToDoItem item = getToDoItemById(id).getBody();
                    item.setDone(false);
                    updateToDoItem(item, id);
                    BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_UNDONE.getMessage(), this);
    
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
        }
        
        public void DELETE(String messageTextFromTelegram, long chatId) {
            String delete = messageTextFromTelegram.substring(0,
                            messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
                    Integer id = Integer.valueOf(delete);
    
                    try {
    
                        deleteToDoItem(id).getBody();
                        BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_DELETED.getMessage(), this);
    
                    } catch (Exception e) {
                        logger.error(e.getLocalizedMessage(), e);
                    }
        }
        
        public List<ToDoItem> getAllToDoItems() { 
            return toDoItemService.findAll();
        }
    
        public ResponseEntity<ToDoItem> getToDoItemById(@PathVariable int id) {
            try {
                ResponseEntity<ToDoItem> responseEntity = toDoItemService.getItemById(id);
                return new ResponseEntity<ToDoItem>(responseEntity.getBody(), HttpStatus.OK);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    
        public ResponseEntity addToDoItem(@RequestBody ToDoItem todoItem) throws Exception {
            ToDoItem td = toDoItemService.addToDoItem(todoItem);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("location", "" + td.getID());
            responseHeaders.set("Access-Control-Expose-Headers", "location");
    
            return ResponseEntity.ok().headers(responseHeaders).build();
        }
    
        public ResponseEntity updateToDoItem(@RequestBody ToDoItem toDoItem, @PathVariable int id) {
            try {
                ToDoItem toDoItem1 = toDoItemService.updateToDoItem(id, toDoItem);
                System.out.println(toDoItem1.toString());
                return new ResponseEntity<>(toDoItem1, HttpStatus.OK);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        }
    
        public ResponseEntity<Boolean> deleteToDoItem(@PathVariable("id") int id) {
            Boolean flag = false;
            try {
                flag = toDoItemService.deleteToDoItem(id);
                return new ResponseEntity<>(flag, HttpStatus.OK);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
                return new ResponseEntity<>(flag, HttpStatus.NOT_FOUND);
            }
        }
}
