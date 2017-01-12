package actions;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.ActionEvent;

public class SubscriptAction extends StyledEditorKit.StyledTextAction {

    public SubscriptAction() {
        super(StyleConstants.Subscript.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JEditorPane editorPane = getEditor(e);

        if(editorPane != null){
            MutableAttributeSet set = getStyledEditorKit(editorPane).getInputAttributes();
            SimpleAttributeSet set1 = new SimpleAttributeSet();
            StyleConstants.setSubscript(set1, !StyleConstants.isSubscript(set));
            setCharacterAttributes(editorPane, set1, false);
        }
    }
}
