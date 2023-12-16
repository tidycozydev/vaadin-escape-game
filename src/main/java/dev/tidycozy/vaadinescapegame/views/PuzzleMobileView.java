package dev.tidycozy.vaadinescapegame.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * This view is an alternative of {@link PuzzleView} for mobiles.
 */
@PageTitle("Vaadin Escape Game")
@Route(value = "puzzle-mobile", layout = LobbyView.class)
public class PuzzleMobileView extends PuzzleView {

    private Image selectedImage;

    public PuzzleMobileView() {
        setSizeFull();

        configurePuzzlePiecesAndLayouts();
        configureDropZonesAndLayouts();
        configurePuzzleLayout();
    }

    private void configurePuzzlePiecesAndLayouts() {
        Image topLeftImage = createSelectableImage(
                "images/tl.png", "Hint: top left image ;)", TOP_LEFT_IMAGE_ID);
        Image topMiddleImage = createSelectableImage(
                "images/tm.png", "Hint: top middle image ;)", TOP_MIDDLE_IMAGE_ID);
        Image topRightImage = createSelectableImage(
                "images/tr.png", "Hint: top right image ;)", TOP_RIGHT_IMAGE_ID);
        Image bottomLeftImage = createSelectableImage(
                "images/bl.png", "Hint: bottom left image ;)", BOTTOM_LEFT_IMAGE_ID);
        Image bottomMiddleImage = createSelectableImage(
                "images/bm.png", "Hint: bottom middle image ;)", BOTTOM_MIDDLE_IMAGE_ID);
        Image bottomRightImage = createSelectableImage(
                "images/br.png", "Hint: bottom right image ;)", BOTTOM_RIGHT_IMAGE_ID);

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
        VerticalLayout topLeftTarget = createDragTargetLayout(TOP_LEFT_IMAGE_ID);
        VerticalLayout topMiddleTarget = createDragTargetLayout(TOP_MIDDLE_IMAGE_ID);
        VerticalLayout topRightTarget = createDragTargetLayout(TOP_RIGHT_IMAGE_ID);
        VerticalLayout bottomLeftTarget = createDragTargetLayout(BOTTOM_LEFT_IMAGE_ID);
        VerticalLayout bottomMiddleTarget = createDragTargetLayout(BOTTOM_MIDDLE_IMAGE_ID);
        VerticalLayout bottomRightTarget = createDragTargetLayout(BOTTOM_RIGHT_IMAGE_ID);

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

}
