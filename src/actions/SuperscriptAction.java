package actions;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.ActionEvent;

public class SuperscriptAction extends StyledEditorKit.StyledTextAction {

    public SuperscriptAction() {
        super(StyleConstants.Superscript.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JEditorPane editorPane = getEditor(e);

        if(editorPane != null){
            MutableAttributeSet set = getStyledEditorKit(editorPane).getInputAttributes();
            SimpleAttributeSet set1 = new SimpleAttributeSet();
            StyleConstants.setSuperscript(set1, !StyleConstants.isSuperscript(set));
            setCharacterAttributes(editorPane, set1, false);
        }
    }
}
