package dev.tidycozy.vaadinescapegame.views;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.server.WebBrowser;
import dev.tidycozy.vaadinescapegame.events.MiniGameDoneEvent;
import dev.tidycozy.vaadinescapegame.session.SessionData;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

/**
 * This view demonstrates {@link AppLayout} Vaadin component.
 */
@PageTitle("Vaadin Escape Game")
@Route(value = "lobby")
public class LobbyView extends AppLayout {

    private SessionData sessionData;

    private final H1 title = new H1();

    private Tabs tabs;

    private Tab exitTab, puzzleTab, fileTab, chatTab, caseTab;

    public LobbyView() {
        configureNavbar();
        configureDrawer();
    }

    private void configureNavbar() {
        HorizontalLayout navbarLayout = new HorizontalLayout();
        navbarLayout.setWidthFull();
        navbarLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        navbarLayout.addClassNames("py-0", "px-m"); // Styles from Vaadin tutorial

        // Allows to hide/show navigation items
        DrawerToggle drawerToggle = new DrawerToggle();

        // We're setting the text in onAttach method to access the player's name
        title.addClassNames("text-l", "m-m"); // Styles from Vaadin tutorial

        Button giveUpButton = new Button("Give up?");
        giveUpButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        giveUpButton.addClickListener(event -> giveUp());

        navbarLayout.add(drawerToggle, title, giveUpButton);
        navbarLayout.expand(title);

        addToNavbar(navbarLayout);
    }

    private void configureDrawer() {
        tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        addToDrawer(tabs);
    }

    private void giveUp() {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Give up");
        confirmDialog.setText("Are you sure you want to quit the game?");
        confirmDialog.setCancelable(true);
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setConfirmText("Quit");

        confirmDialog.addCancelListener(event -> event.getSource().close());
        confirmDialog.addConfirmListener(event -> {
            VaadinSession.getCurrent().setAttribute(SessionData.SESSION_DATA_ATTRIBUTE, null);
            getUI().get().navigate(TitleScreenView.class);
        });

        confirmDialog.open();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        sessionData = (SessionData) VaadinSession.getCurrent().getAttribute(SessionData.SESSION_DATA_ATTRIBUTE);

        // The player tries to access views before giving their name
        if (sessionData == null) {
            getUI().ifPresent(ui -> ui.navigate(TitleScreenView.class));
            return;
        }

        title.setText("Can you escape " + sessionData.getPlayerName() + "?");

        updateTabs();

        // This allows to follow the player's progression and unlock elements in the escape game as they progress
        ComponentUtil.addListener(getUI().get(), MiniGameDoneEvent.class, this::miniGameDone);
    }

    private void miniGameDone(MiniGameDoneEvent event) {
        switch (event.getMiniGame()) {
            case PUZZLE -> {
                if (!sessionData.isFileUnlock()) {
                    sessionData.setFileUnlock(true);
                    updateTabs();
                    tabs.setSelectedIndex(1);
                }
            }
            case FILE -> {
                if (!sessionData.isChatUnlock()) {
                    sessionData.setChatUnlock(true);
                    updateTabs();
                    tabs.setSelectedIndex(2);
                }
            }
            case CHAT -> {
                if (!sessionData.isCaseUnlock()) {
                    sessionData.setCaseUnlock(true);
                    updateTabs();
                    tabs.setSelectedIndex(3);
                }
            }
            case CASE -> {
                if (!sessionData.isCaseFinished()) {
                    sessionData.setCaseFinished(true);
                    updateTabs();
                    tabs.setSelectedIndex(4);
                }
            }
        }
    }

    /**
     * Add the tabs when the player progresses and update tabs label as a reminder of the digits already found.
     */
    private void updateTabs() {
        // The exit view
        if (exitTab == null) {
            exitTab = createTab(VaadinIcon.EXIT, "The exit", ExitView.class);
            tabs.add(exitTab);
        }

        // The puzzle view
        if (puzzleTab == null) {
            // Different for mobile devices due to lack of stability for drag n drop API
            String puzzleTabLabel = "The puzzle";
            WebBrowser browser = getUI().get().getSession().getBrowser();
            Class<? extends Component> viewClass =
                    browser.isAndroid() || browser.isIPhone() || browser.isWindowsPhone()
                            ? PuzzleMobileView.class
                            : PuzzleView.class;
            puzzleTab = createTab(VaadinIcon.PUZZLE_PIECE, puzzleTabLabel, viewClass);
            tabs.add(puzzleTab);
        } else if (sessionData.isFileUnlock()) {
            updateTab(puzzleTab, "The puzzle - " + PuzzleView.PUZZLE_VIEW_DIGIT);
        }

        // The file view
        if (sessionData.isFileUnlock()) {
            if (fileTab == null) {
                fileTab = createTab(VaadinIcon.FILE_PICTURE, "The file", FileView.class);
                tabs.add(fileTab);
            } else if (sessionData.isChatUnlock()) {
                updateTab(fileTab, "The file - " + FileView.FILE_VIEW_DIGIT);
            }
        }

        // The chat view
        if (sessionData.isChatUnlock()) {
            if (chatTab == null) {
                chatTab = createTab(VaadinIcon.CHAT, "The chat", ChatView.class);
                tabs.add(chatTab);
            } else if (sessionData.isCaseUnlock()) {
                updateTab(chatTab, "The chat - " + ChatView.CHAT_VIEW_DIGIT);
            }
        }

        // The case view
        if (sessionData.isCaseUnlock()) {
            if (caseTab == null) {
                caseTab = createTab(VaadinIcon.FOLDER, "The case", CaseView.class);
                tabs.add(caseTab);
            } else if (sessionData.isCaseFinished()) {
                updateTab(caseTab, "The case - " + CaseView.CASE_VIEW_DIGIT);
            }
        }
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, Class<? extends Component> viewClass) {
        Icon icon = viewIcon.create();
        icon.getStyle() // Styles from Vaadin tutorial
                .set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        link.setRoute(viewClass);

        return new Tab(link);
    }

    private void updateTab(Tab tab, String newName) {
        Span span = (Span) tab.getChildren().findFirst().get() // That's the RouterLink
                        .getChildren().filter(component -> component instanceof Span).findFirst().get(); // That's the Span
        span.setText(newName);
    }

}