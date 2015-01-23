package de.bht.fpa.mail.s791831.model.data;


import java.io.File;
import java.util.List;

/*
 * This is the leaf part of a composite pattern.
 * 
 * @author Simone Strippgen
 */

public class FileElement extends Component {

    public FileElement(File path) {
        super(path);
    }

    @Override
    public boolean isExpandable() {
        return false;
    }
    
}
