package regulator.util;

import java.io.File;

/**
 * Class for accept file according recieved String[] filter of file extensions
 */
public class FileFilter {

    /*file extensions*/
    private String[] extensions;

    /*default constructor*/
    public FileFilter() {
    }

    /*constructor*/
    public FileFilter(String[] extensions) {
        this.extensions = extensions;
    }

    /*getters and setters*/

    public String[] getExtensions() {
        return extensions;
    }

    public void setExtensions(String[] extensions) {
        this.extensions = extensions;
    }

    /*accepts all files according String[] extensions
    * accepts folders*/
    public boolean accept(String absoluteFilePath){

        if ((this.extensions==null)||(this.extensions.length == 0)) return true;

        String path = absoluteFilePath.toLowerCase();
        for (String extension : extensions) {
            if ((path.endsWith(extension) && (path.charAt(path.length()
                    - extension.length() - 1)) == '.')) {
                return true;
            }else if (new File(absoluteFilePath).isDirectory()) return true;
        }

        return false;
    }
}
