package cn.nukkit.form.response;

import cn.nukkit.form.element.ElementNPCButton;

/**
 * @author good777LUCKY
 */
public class FormNPCResponseStudent extends FormNPCResponse {

    private final int clickedButtonId;
    private final ElementButton clickedButton;

    public FormResponseSimple(int clickedButtonId, ElementNPCButton clickedButton) {
        this.clickedButtonId = clickedButtonId;
        this.clickedButton = clickedButton;
    }

    public int getClickedButtonId() {
        return clickedButtonId;
    }

    public ElementButton getClickedButton() {
        return clickedButton;
    }
}
