package common.data;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;

public class FileInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public File file;
    public String checkSum;

    public SyncStatus syncStatus = SyncStatus.NOT_SYNC;
    public byte[] data;

    public FileInfo(File file, String checkSum) {
        this.file = file;
        this.checkSum = checkSum;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "file=" + file +
                ", checkSum='" + checkSum + '\'' +
                ", syncStatus=" + syncStatus +
                ", length=" + (data == null?0:data.length) +
                '}';
    }


}

