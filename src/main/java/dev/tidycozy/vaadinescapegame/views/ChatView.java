package dev.tidycozy.vaadinescapegame.views;

import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.tidycozy.vaadinescapegame.components.MiniGameView;
import dev.tidycozy.vaadinescapegame.components.LoadingNotification;
import dev.tidycozy.vaadinescapegame.events.MiniGameDoneEvent;
import dev.tidycozy.vaadinescapegame.utils.ApplicationUtils;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * This view demonstrates {@link MessageList} and {@link MessageInput} Vaadin components.
 */
@PageTitle("Vaadin Escape Game")
@Route(value = "chat", layout = LobbyView.class)
public class ChatView extends MiniGameView {

    private String playerName;
    private String suspectCurrentName;

    private final MessageList messageList = new MessageList();

    private boolean robotThinking;
    private boolean alreadySaidHello;

    private boolean caseOpened;

    public ChatView() {
        setSizeFull();

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.addClassName(LumoUtility.Background.BASE);

        messageList.setSizeFull();

        MessageInput messageInput = new MessageInput();
        messageInput.setWidthFull();
        messageInput.addSubmitListener(submitEvent -> {
            // If robot is already thinking, new message is ignored
            if (!robotThinking) {
                MessageListItem newMessage = new MessageListItem(submitEvent.getValue(), Instant.now(), this.playerName);
                newMessage.setUserColorIndex(2);
                List<MessageListItem> items = new ArrayList<>(messageList.getItems());
                items.add(newMessage);
                messageList.setItems(items);

                // After a message, robot will "think" about it and answer
                robotThinking();
            } else {
                showWarningNotification("Dog robot is already thinking");
            }
        });

        layout.add(messageList, messageInput);

        add(layout);
    }

    private void robotThinking() {
        // Robot start thinking
        robotThinking = true;

        LoadingNotification notification = new LoadingNotification(2000);
        notification.open();
        notification.addOpenedChangeListener(event -> robotProcess());
    }

    /**
     * Big method with some "logic" for the robot to understand and answer to the player.
     * The robot is really dumb, could improve that in the future using machine learning?
     */
    private void robotProcess() {
        String answer;

        String lastMessage =
                messageList.getItems().get(messageList.getItems().size() - 1).getText().toLowerCase();

        // Message expected to progress in the game
        if (!caseOpened && userMentionedSuspect(lastMessage)) {
            answer = "Opening case number 8, suspect's name: " + suspectCurrentName + ".";
            if (!suspectCurrentName.equalsIgnoreCase("swift")) {
                answer += " You changed the suspect's name! Not sure what the protocol says about it...";
            }
            caseOpened = true;
            showProgressionNotification("That's the third digit, the case's number! One more to go.");
            ComponentUtil.fireEvent(
                    getUI().get(), new MiniGameDoneEvent(this, MiniGameDoneEvent.Minigames.CHAT));
        } else {
            // Messages without importance
            if (userIsSayingHello(lastMessage)) {
                if (alreadySaidHello) {
                    answer = "I've already said hello to you but... Hi again!";
                } else {
                    alreadySaidHello = true;
                    answer = "Hello! Which case are you working on today?";
                }
            } else if (userWantToKnowHowRobotIsFeeling(lastMessage)) {
                answer = "I feel great even if I feel the same all the time, thanks for asking!";
            } else if (userSaysThank(lastMessage)) {
                if (!caseOpened) {
                    answer = "You are very welcome even if I'm not sure why...";
                } else {
                    answer = "I only did my job!";
                }
            } else if (userSpeaksAboutFood(lastMessage)) {
                answer = "If in doubt, pizza.";
            } else if (userAskHowToOpenACase(lastMessage)) {
                answer = "To open a case, you need to mention the name of your suspect.";
            } else if (userAskedForHelp(lastMessage)) {
                answer = "Just tell the name of your suspect, I will open the related case for you.";
            } else if (caseOpened) {
                answer = "Your case is opened, go check it out so I can do some machine learning based " +
                        "on our exchange.\nHave a good day!";
            } else {
                answer = getOneConfusedAnswer();
            }
        }

        // In the end, robot writes its answer
        robotWrite(answer);
    }

    private boolean userMentionedSuspect(String message) {
        return message.contains(suspectCurrentName.toLowerCase());
    }

    private boolean userIsSayingHello(String message) {
        return message.contains("hello") ||
                message.matches("^hi.*") ||
                message.contains("good morning") ||
                message.contains("good evening");
    }

    private boolean userWantToKnowHowRobotIsFeeling(String message) {
        return message.contains("how are you doing") ||
                message.contains("how is it going") ||
                message.contains("how do you feel") ||
                message.contains("what's up");
    }

    private boolean userSaysThank(String message) {
        return message.contains("thank");
    }

    private boolean userSpeaksAboutFood(String message) {
        return message.contains("food") ||
                message.contains("hungry") ||
                message.contains("eat");
    }

    private boolean userAskHowToOpenACase(String message) {
        return message.contains("work") ||
                message.contains("open") ||
                message.contains("how do") ||
                message.contains("how to") ||
                message.contains("case");
    }

    private boolean userAskedForHelp(String message) {
        return message.contains("help");
    }

    private String getOneConfusedAnswer() {
        return randomAnswers[ApplicationUtils.generateRandomInt(0, randomAnswers.length)];
    }

    private final String[] randomAnswers = {
            "Can you be more precise? I'm not smart at all.\nWhich case are you working on?",
            "I don't understand your answer, my AI code is 40 lines long.\nWhich case are you working on?",
            "Your message is too difficult to process for my poor capabilities.\nWhich case are you working on?",
            "I can't find any name related to your research.\nWhich case are you working on?",
            "Hum, yes, no, not sure... If you need help, just ask me.\nWhich case are you working on?",
            "I can help but you need to understand I'm not smart.\nWhich case are you working on?"
    };

    private void robotWrite(String message) {
        MessageListItem newMessage = new MessageListItem(message, Instant.now(), "Robot");
        newMessage.setUserImage("images/robot.png");
        newMessage.setUserColorIndex(1);
        List<MessageListItem> items = new ArrayList<>(messageList.getItems());
        items.add(newMessage);
        messageList.setItems(items);

        // Robot stops thinking
        robotThinking = false;
    }

    @Override
    protected void customViewAttachCalls() {
        if (!sessionData.isCaseUnlock()) {
            showProgressionNotification(
                    "OK, I need my dog robot assistant to open the case for me. " +
                            "Strange software when you think about it...");

            robotWrite("Welcome to the case opener assistant.");
        } else {
            caseOpened = true;
            robotWrite("Hello again. Your case is already opened, inferring that you came for a little chat.");
        }

        this.playerName = sessionData.getPlayerName();
        this.suspectCurrentName = sessionData.getSuspect().getLastName();
    }

}
