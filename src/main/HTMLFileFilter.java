package main;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class HTMLFileFilter extends FileFilter {

    @Override
    public String getDescription() {
        return "HTML и HTM файлы";
    }

    @Override
    public boolean accept(File f) {
        String fileName = f.getName().toLowerCase();
        return fileName.endsWith(".html") || fileName.endsWith(".htm") || f.isDirectory();
    }
}
