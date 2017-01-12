package main;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.*;
import java.io.*;

public class Controller {

    private View view;
    private HTMLDocument document;
    private File currentFile;

    public Controller(View view) {
        this.view = view;
    }

    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller(view);
        view.setController(controller);
        view.init();
        controller.init();
    }

    public void setPlainText(String text){
        resetDocument();
        StringReader reader = new StringReader(text);
        try {
            new HTMLEditorKit().read(reader, document, 0);
        }catch (IOException | BadLocationException e){
            ExceptionHandler.log(e);
        }
    }

    public String getPlainText(){
        StringWriter writer = new StringWriter();
        try {
            new HTMLEditorKit().write(writer, document, 0, document.getLength());
        }catch (IOException | BadLocationException e){
            ExceptionHandler.log(e);
        }
        return writer.toString();
    }

    public void resetDocument(){
        if(document != null) {
            document.removeUndoableEditListener(view.getUndoListener());
        }
        HTMLEditorKit editorKit = new HTMLEditorKit();
        document = (HTMLDocument) editorKit.createDefaultDocument();
        document.addUndoableEditListener(view.getUndoListener());
        view.update();
    }

    public void init(){
        createNewDocument();
    }

    public HTMLDocument getDocument() {
        return document;
    }

    public void exit(){
        System.exit(0);
    }

    public void createNewDocument(){
        view.selectHtmlTab();
        resetDocument();
        view.setTitle("HTML редактор");
        view.resetUndo();
        currentFile = null;
    }

    public void openDocument(){
        view.selectHtmlTab();
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new HTMLFileFilter());
        chooser.setDialogTitle("Open File");
        int i = chooser.showOpenDialog(view);

        if(i == JFileChooser.APPROVE_OPTION){
            currentFile = chooser.getSelectedFile();
            resetDocument();
            view.setTitle(currentFile.getName());
            try(FileReader reader = new FileReader(currentFile)){
                new HTMLEditorKit().read(reader, document, 0);
            }catch (IOException | BadLocationException e){
                ExceptionHandler.log(e);
            }
            view.resetUndo();
        }
    }

    public void saveDocument(){
        view.selectHtmlTab();
        if(currentFile == null){
            saveDocumentAs();
        }else{
            try(FileWriter writer = new FileWriter(currentFile)) {
                new HTMLEditorKit().write(writer, document, 0, document.getLength());
            }catch (IOException| BadLocationException e){
                ExceptionHandler.log(e);
            }
        }
    }

    public void saveDocumentAs(){
        view.selectHtmlTab();
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new HTMLFileFilter());
        chooser.setDialogTitle("Save File");
        int i = chooser.showSaveDialog(view);

        if(i == JFileChooser.APPROVE_OPTION){
            currentFile = chooser.getSelectedFile();
            view.setTitle(currentFile.getName());
            try(FileWriter writer = new FileWriter(currentFile)) {
                new HTMLEditorKit().write(writer, document, 0, document.getLength());
            }catch (IOException | BadLocationException e){
                ExceptionHandler.log(e);
            }
        }
    }
}
