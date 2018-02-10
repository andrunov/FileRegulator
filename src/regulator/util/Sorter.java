package regulator.util;

import regulator.model.FileInfo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class for custom sorters
 */
public class Sorter {

    /*comparator use only for sort List<FileInfo>, not for compare FileInfo objects */
    private static Comparator<FileInfo> fileInfoComparator;

    static {
        fileInfoComparator = new Comparator<FileInfo>() {
            @Override
            public int compare(FileInfo o1, FileInfo o2) {
                return o1.getAbsolutePath().compareTo(o2.getAbsolutePath());
            }
        };
    }

    /*sort List<FileInfo>*/
    public static void sort(List<FileInfo> fileInfoList){
        Collections.sort(fileInfoList,fileInfoComparator);
        for (FileInfo fileInfo : fileInfoList){
            Collections.sort(fileInfo.getSimilarFiles(),fileInfoComparator);
        }
    }
}
