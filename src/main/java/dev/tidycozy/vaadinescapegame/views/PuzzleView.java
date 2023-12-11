package dev.tidycozy.vaadinescapegame.views;

import dev.tidycozy.vaadinescapegame.components.MiniGameView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.tidycozy.vaadinescapegame.events.MiniGameDoneEvent;

/**
 * This view demonstrates the drag and drop API of Vaadin.
 */
@PageTitle("Vaadin Escape Game")
@Route(value = "puzzle", layout = LobbyView.class)
public class PuzzleView extends MiniGameView {

    private final VerticalLayout puzzleLayout = new VerticalLayout();

    private final HorizontalLayout dropLayout1 = new HorizontalLayout();

    private final HorizontalLayout dropLayout2 = new HorizontalLayout();

    private final HorizontalLayout piecesLayout1 = new HorizontalLayout();

    private final HorizontalLayout piecesLayout2 = new HorizontalLayout();

    private DragSource<Image> topLeftImage, topMiddleImage, topRightImage,
            bottomLeftImage, bottomMiddleImage, bottomRightImage;

    private int goodLocationCount = 0;

    public PuzzleView() {
        setSizeFull();

        configurePuzzlePieces();
        configurePuzzlePiecesLayouts();
        configureDropZonesAndLayouts();
        configurePuzzleLayout(); // This global layout needs the other elements to be instantiated, we call it last
    }

    private void configurePuzzleLayout() {
        puzzleLayout.setSizeUndefined();
        puzzleLayout.addClassName(LumoUtility.Background.BASE);
        puzzleLayout.add(dropLayout1, dropLayout2);
        puzzleLayout.add(piecesLayout1, piecesLayout2);

        add(puzzleLayout);

        setHorizontalComponentAlignment(Alignment.CENTER, puzzleLayout);
    }

    private void configurePuzzlePieces() {
        topLeftImage = createDragSourceImage(
                "images/tl.png", "Hint: top left image ;)", "puzzle-tl");
        topMiddleImage = createDragSourceImage(
                "images/tm.png", "Hint: top middle image ;)", "puzzle-tm");
        topRightImage = createDragSourceImage(
                "images/tr.png", "Hint: top right image ;)", "puzzle-tr");
        bottomLeftImage = createDragSourceImage(
                "images/bl.png", "Hint: bottom left image ;)", "puzzle-bl");
        bottomMiddleImage = createDragSourceImage(
                "images/bm.png", "Hint: bottom middle image ;)", "puzzle-bm");
        bottomRightImage = createDragSourceImage(
                "images/br.png", "Hint: bottom right image ;)", "puzzle-br");
    }

    private DragSource<Image> createDragSourceImage(String imageUrl, String imageAlt, String imageId) {
        Image image = new Image(imageUrl, imageAlt);
        image.setWidth(100, Unit.PIXELS);
        image.setId(imageId);
        return DragSource.create(image);
    }

    private void configurePuzzlePiecesLayouts() {
        piecesLayout1.add(
                topRightImage.getDragSourceComponent(),
                topLeftImage.getDragSourceComponent(),
                bottomRightImage.getDragSourceComponent());

        piecesLayout2.add(
                bottomMiddleImage.getDragSourceComponent(),
                topMiddleImage.getDragSourceComponent(),
                bottomLeftImage.getDragSourceComponent());
    }

    private void configureDropZonesAndLayouts() {
        VerticalLayout topLeftTarget = createDragTargetLayout(topLeftImage, "puzzle-tl");
        VerticalLayout topMiddleTarget = createDragTargetLayout(topMiddleImage, "puzzle-tm");
        VerticalLayout topRightTarget = createDragTargetLayout(topRightImage, "puzzle-tr");
        VerticalLayout bottomLeftTarget = createDragTargetLayout(bottomLeftImage, "puzzle-bl");
        VerticalLayout bottomMiddleTarget = createDragTargetLayout(bottomMiddleImage, "puzzle-bm");
        VerticalLayout bottomRightTarget = createDragTargetLayout(bottomRightImage, "puzzle-br");

        dropLayout1.add(topLeftTarget, topMiddleTarget, topRightTarget);
        dropLayout2.add(bottomLeftTarget, bottomMiddleTarget, bottomRightTarget);
    }

    private VerticalLayout createDragTargetLayout(DragSource<Image> dragSourceImage, String dragSourceMatchingName) {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.addClassName(LumoUtility.Background.CONTRAST_40);
        layout.setWidth(100, Unit.PIXELS);
        layout.setHeight(100, Unit.PIXELS);

        DropTarget<VerticalLayout> dropTarget = DropTarget.create(layout);
        dropTarget.addDropListener(event -> {
            event.getDragSourceComponent().ifPresent(component -> {
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
                layout.add(component);
                // If the piece is well-placed, we deactivate the
                // possibility of dropping piece on this spot as
                // well as the possibility of dragging the piece
                component.getId().ifPresent(id -> {
                    boolean goodLocation = id.equals(dragSourceMatchingName);
                    dropTarget.setActive(!goodLocation);
                    dragSourceImage.setDraggable(!goodLocation);
                    if (goodLocation) { // Increasing the player's progression
                        goodLocationCount++;
                        checkLocationCount();
                    }
                });
            });
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
