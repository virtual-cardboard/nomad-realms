package nomadrealms.render.ui.custom.card;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import engine.context.input.event.InputCallbackRegistry;
import engine.visuals.constraint.box.ConstraintBox;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.collection.DeckList;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.content.BasicUIContent;
import nomadrealms.render.ui.content.UIContent;

public class DeckListGroupUI extends BasicUIContent {

	private final List<DeckListUI> deckListUIs = new ArrayList<>();
	private DeckListUI selectedDeckListUI = null;

	private GameCard hoveredCardForPreview = null;
	private UICard previewUICard = null;

	public DeckListGroupUI(UIContent parent, List<String> deckNames, List<DeckList> deckLists,
						   List<ConstraintBox> boxes, InputCallbackRegistry registry) {
		super(parent);
		for (int i = 0; i < deckLists.size(); i++) {
			String title = deckNames.get(i);
			DeckList deckList = deckLists.get(i);
			ConstraintBox box = boxes.get(i);
			DeckListUI deckListUI = new DeckListUI(this, title, deckList, box, registry);
			deckListUIs.add(deckListUI);
		}

		if (registry != null) {
			registry.registerOnPress(event -> {
				int button = event.button();
				for (DeckListUI dlUI : deckListUIs) {
					if (dlUI.constraintBox().contains(event.mouse().coordinate().vector())) {
						if (button == org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT) {
							select(dlUI);
						}
						// Check if a card inside this decklist was clicked
						for (DeckListCardUI cardUI : new ArrayList<>(dlUI.cardUIs())) {
							if (cardUI.constraintBox().contains(event.mouse().coordinate().vector())) {
								if (button == org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT) {
									select(dlUI);
									dlUI.removeCard(cardUI.card());
								} else if (button == org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
									hoveredCardForPreview = cardUI.card();
									updatePreviewCard(event.mouse().coordinate().x().get(), event.mouse().coordinate().y().get());
								}
								break;
							}
						}
					}
				}
			});

			registry.registerOnDrag(event -> {
				if (hoveredCardForPreview != null) {
					boolean mouseStillOver = false;
					for (DeckListUI dlUI : deckListUIs) {
						for (DeckListCardUI cardUI : dlUI.cardUIs()) {
							if (cardUI.card() == hoveredCardForPreview && cardUI.constraintBox().contains(event.mouse().coordinate().vector())) {
								mouseStillOver = true;
								break;
							}
						}
					}
					if (mouseStillOver) {
						updatePreviewCard(event.mouse().coordinate().x().get(), event.mouse().coordinate().y().get());
					} else {
						hoveredCardForPreview = null;
						previewUICard = null;
					}
				}
			});
		}
	}

	private void updatePreviewCard(float x, float y) {
		if (hoveredCardForPreview != null) {
			ConstraintBox previewBox = new ConstraintBox(
					absolute(x),
					absolute(y),
					UICard.cardSize(2f)
			);
			previewUICard = new UICard(new WorldCard(null, hoveredCardForPreview), previewBox);
		}
	}

	public void select(DeckListUI deckListUI) {
		this.selectedDeckListUI = deckListUI;
		for (DeckListUI dlUI : deckListUIs) {
			dlUI.selected(dlUI == selectedDeckListUI);
		}
	}

	public DeckListUI selectedDeckListUI() {
		return selectedDeckListUI;
	}

	public DeckList selectedDeckList() {
		return selectedDeckListUI != null ? selectedDeckListUI.deckList() : null;
	}

	public void addCardToSelected(GameCard card) {
		if (selectedDeckListUI != null) {
			selectedDeckListUI.addCard(card);
		}
	}

	public List<DeckListUI> deckListUIs() {
		return deckListUIs;
	}

	@Override
	public void _render(RenderingEnvironment re) {
		if (previewUICard != null && hoveredCardForPreview != null) {
			previewUICard.render(re);
		}
	}

}
