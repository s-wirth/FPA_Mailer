package de.bht.fpa.mail.s791831.model.appLogic;


import de.bht.fpa.mail.s791831.model.data.FileElement;
import de.bht.fpa.mail.s791831.model.data.Folder;
import java.io.File;

/*
 * This class manages a hierarchy of folders and their content which is loaded 
 * from a given directory path.
 * 
 * @author Simone Strippgen
 */
public class FileManager implements FolderManagerIF {

    //top Folder of the managed hierarchy
    Folder topFolder;

    /**
     * Constructs a new FileManager object which manages a folder hierarchy, 
     * where file contains the path to the top directory. 
     * The contents of the  directory file are loaded into the top folder
     * @param file File which points to the top directory
     */
    public FileManager(File file) {
       topFolder = new Folder(file, true);   
    }
    
    /**
     * Loads all relevant content in the directory path of a folder
     * object into the folder.
     * @param f the folder into which the content of the corresponding 
     *          directory should be loaded
     */
    @Override
    public void loadContent(Folder f) {
        File file = new File(f.getPath());
        File[] content = file.listFiles();
        if(content != null){
            for(File c: content){
                boolean expandable = false;
                
                  /*undo comment if files should be visible*/
//                if(c.isFile()){
//                   f.addComponent(new FileElement(c)); 
//                }
                if(c.isDirectory()){
                    if(c.listFiles() != null && c.listFiles().length > 0){
                        for(File expandFile: c.listFiles()){
                            if(expandFile.isDirectory()){
                                expandable = true;
                            }
                        }
//                        expandable = true;
                    }
                    if(!c.getName().startsWith(".")){  //filter hidden files
                        f.addComponent(new Folder(c,expandable));
                    }                    
                }
            }
        }
    }

    @Override
    public Folder getTopFolder() {
        return topFolder;
    }
}
