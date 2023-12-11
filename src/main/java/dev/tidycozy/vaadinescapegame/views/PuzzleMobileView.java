package dev.tidycozy.vaadinescapegame.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.tidycozy.vaadinescapegame.components.MiniGameView;
import dev.tidycozy.vaadinescapegame.events.MiniGameDoneEvent;

/**
 * This view is an alternative of {@link PuzzleView} for mobiles.
 */
@PageTitle("Vaadin Escape Game")
@Route(value = "puzzle-mobile", layout = LobbyView.class)
public class PuzzleMobileView extends MiniGameView {

    private final VerticalLayout puzzleLayout = new VerticalLayout();

    private final HorizontalLayout dropLayout1 = new HorizontalLayout();

    private final HorizontalLayout dropLayout2 = new HorizontalLayout();

    private final HorizontalLayout piecesLayout1 = new HorizontalLayout();

    private final HorizontalLayout piecesLayout2 = new HorizontalLayout();

    private Image selectedImage;

    private int goodLocationCount = 0;

    public PuzzleMobileView() {
        setSizeFull();

        configurePuzzlePiecesAndLayouts();
        configureDropZonesAndLayouts();
        configurePuzzleLayout();
    }

    private void configurePuzzleLayout() {
        puzzleLayout.setSizeUndefined();
        puzzleLayout.addClassName(LumoUtility.Background.BASE);
        puzzleLayout.add(dropLayout1, dropLayout2);
        puzzleLayout.add(piecesLayout1, piecesLayout2);

        add(puzzleLayout);

        setHorizontalComponentAlignment(Alignment.CENTER, puzzleLayout);
    }

    private void configurePuzzlePiecesAndLayouts() {
        Image topLeftImage = createSelectableImage(
                "images/tl.png", "Hint: top left image ;)", "puzzle-tl");
        Image topMiddleImage = createSelectableImage(
                "images/tm.png", "Hint: top middle image ;)", "puzzle-tm");
        Image topRightImage = createSelectableImage(
                "images/tr.png", "Hint: top right image ;)", "puzzle-tr");
        Image bottomLeftImage = createSelectableImage(
                "images/bl.png", "Hint: bottom left image ;)", "puzzle-bl");
        Image bottomMiddleImage = createSelectableImage(
                "images/bm.png", "Hint: bottom middle image ;)", "puzzle-bm");
        Image bottomRightImage = createSelectableImage(
                "images/br.png", "Hint: bottom right image ;)", "puzzle-br");

        piecesLayout1.add(topRightImage, topLeftImage, bottomRightImage);
        piecesLayout2.add(bottomMiddleImage, topMiddleImage, bottomLeftImage);
    }

    private Image createSelectableImage(String imageUrl, String imageAlt, String imageId) {
        Image image = new Image(imageUrl, imageAlt);
        image.setWidth(100, Unit.PIXELS);
        image.setId(imageId);
        image.addClickListener(event -> {
            if (selectedImage != null) {
                selectedImage.removeClassName(LumoUtility.Border.ALL);
            }
            selectedImage = event.getSource();
            selectedImage.addClassName(LumoUtility.Border.ALL);
        });
        return image;
    }

    private void configureDropZonesAndLayouts() {
        VerticalLayout topLeftTarget = createDragTargetLayout("puzzle-tl");
        VerticalLayout topMiddleTarget = createDragTargetLayout("puzzle-tm");
        VerticalLayout topRightTarget = createDragTargetLayout("puzzle-tr");
        VerticalLayout bottomLeftTarget = createDragTargetLayout("puzzle-bl");
        VerticalLayout bottomMiddleTarget = createDragTargetLayout("puzzle-bm");
        VerticalLayout bottomRightTarget = createDragTargetLayout("puzzle-br");

        dropLayout1.add(topLeftTarget, topMiddleTarget, topRightTarget);
        dropLayout2.add(bottomLeftTarget, bottomMiddleTarget, bottomRightTarget);
    }

    private VerticalLayout createDragTargetLayout(String imageIdMatching) {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.addClassName(LumoUtility.Background.CONTRAST_40);
        layout.setWidth(100, Unit.PIXELS);
        layout.setHeight(100, Unit.PIXELS);

        layout.addClickListener(event -> {
            if (selectedImage != null) {
                // If a piece is already on the spot, we put back
                // the former with the non placed pieces
                if (layout.getComponentCount() > 0) {
                    Component piece = layout.getComponentAt(0);
                    if (piecesLayout1.getComponentCount() < 3) {
                        piecesLayout1.add(piece);
                    } else {
                        piecesLayout2.add(piece);
                    }
                }

                layout.add(selectedImage);

                // If the piece is well-placed, we deactivate the
                // possibility of placing any piece on that spot
                // and moving the piece already placed
                boolean goodLocation = selectedImage.getId().get().equals(imageIdMatching);
                if (goodLocation) {
                    selectedImage.setEnabled(false);
                    selectedImage.removeClassName(LumoUtility.Border.ALL);
                    selectedImage = null;

                    layout.setEnabled(false);

                    // Increasing the player's progression
                    goodLocationCount++;
                    checkLocationCount();
                }
            }
        });

        return layout;
    }

    private void checkLocationCount() {
        if (goodLocationCount > 5) {
            switchPiecesToPicture();
            showProgressionNotification("Got the first digit! Let's investigate that Mr. Swift.");
            ComponentUtil.fireEvent(
                    getUI().get(), new MiniGameDoneEvent(this, MiniGameDoneEvent.Minigames.PUZZLE));
        }
    }

    private void switchPiecesToPicture() {
        puzzleLayout.removeAll();
        Image image = new Image(
                "images/complete-postit.png",
                "Puzzle complete displaying the digit 3 and the name of a mister Swift");
        image.setWidth(360, Unit.PIXELS);
        puzzleLayout.add(image);
    }

    @Override
    protected void customViewAttachCalls() {
        if (!sessionData.isFileUnlock()) {
            showProgressionNotification("This post-it looks like it's holding some useful " +
                    "information, I should put back the pieces together.");
        } else {
            switchPiecesToPicture(); // If the player has already done the puzzle, we show them the full image
        }
    }

}
