package main;

import listeners.*;
import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;

public class View extends JFrame implements ActionListener {

    private Controller controller;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JTextPane htmlTextPane = new JTextPane();
    private JEditorPane plainTextPane = new JEditorPane();
    private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);

    public UndoListener getUndoListener() {
        return undoListener;
    }

    public View(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public boolean isHtmlTabSelected(){
        return tabbedPane.getSelectedIndex() == 0;
    }

    public void selectHtmlTab(){
        tabbedPane.setSelectedIndex(0);
        resetUndo();
    }

    public void update(){
        HTMLDocument document = controller.getDocument();
        htmlTextPane.setDocument(document);
    }

    public void showAbout(){
        JOptionPane.showMessageDialog(getContentPane(), "HTML Редактор", "About" , JOptionPane.INFORMATION_MESSAGE);
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void init(){
        initGui();
        FrameListener listener = new FrameListener(this);
        addWindowListener(listener);
        setVisible(true);
    }

    public void exit(){
        controller.exit();
    }

    public void initMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        MenuHelper.initFileMenu(this, menuBar);
        MenuHelper.initEditMenu(this, menuBar);
        MenuHelper.initStyleMenu(this, menuBar);
        MenuHelper.initAlignMenu(this, menuBar);
        MenuHelper.initColorMenu(this, menuBar);
        MenuHelper.initFontMenu(this, menuBar);
        MenuHelper.initHelpMenu(this, menuBar);
        getContentPane().add(menuBar, BorderLayout.NORTH);
    }

    public void initEditor(){
        htmlTextPane.setContentType("text/html");
        JScrollPane scrollPane = new JScrollPane(htmlTextPane);
        tabbedPane.addTab("HTML", scrollPane);
        JScrollPane scrollPane1 = new JScrollPane(plainTextPane);
        tabbedPane.addTab("Текст", scrollPane1);
        tabbedPane.setPreferredSize(new Dimension());
        TabbedPaneChangeListener listener = new TabbedPaneChangeListener(this);
        tabbedPane.addChangeListener(listener);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public void initGui(){
        initMenuBar();
        initEditor();
        pack();
    }

    public void selectedTabChanged(){
        int i = tabbedPane.getSelectedIndex();
        if(i == 0){
            String text = plainTextPane.getText();
            controller.setPlainText(text);
        }else if(i == 1){
            String text = controller.getPlainText();
            plainTextPane.setText(text);
        }
        resetUndo();
    }

    public void undo(){
        undoManager.undo();
    }

    public void redo(){
        undoManager.redo();
    }

    public boolean canUndo(){
        return undoManager.canUndo();
    }

    public boolean canRedo(){
        return undoManager.canRedo();
    }

    public void resetUndo(){
        undoManager.discardAllEdits();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(command.equals("Новый")){
            controller.createNewDocument();
        }else if(command.equals("Открыть")){
            controller.openDocument();
        }else if(command.equals("Сохранить")){
            controller.saveDocument();
        }else if(command.equals("Сохранить как...")){
            controller.saveDocumentAs();
        }else if(command.equals("Выход")){
            exit();
        }else if(command.equals("О программе")){
            showAbout();
        }
    }
}
