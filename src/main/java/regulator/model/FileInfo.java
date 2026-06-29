package regulator.model;

import java.util.Objects;

public class FileInfo implements Comparable<FileInfo> {

    public FileInfo(int number, String name) {
        this.number = number;
        this.name = name;
    }

    private int number;

    private String name;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileInfo fileInfo = (FileInfo) o;
        return number == fileInfo.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "number=" + number +
                ", absolutePath='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(FileInfo o) {
        return this.number - o.number;
    }
}
