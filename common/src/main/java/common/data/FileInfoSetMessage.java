package common.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

public class FileInfoSetMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String clientId;
    private Set<FileInfo> fileInfoSet;


    public FileInfoSetMessage(String clientId, Set<FileInfo> fileInfoSet) {
        this.clientId = clientId;
        this.fileInfoSet = fileInfoSet;
    }

    public String getClientId() {
        return clientId;
    }

    public Set<FileInfo> getFileInfoSet() {
        return fileInfoSet;
    }

    public boolean isSync() {
        for (FileInfo fileInfo : fileInfoSet) {
            if (fileInfo.syncStatus != SyncStatus.SYNC) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "FileInfoSetMessage{" +
                "fileInfoSet=" + fileInfoSet +
                '}';
    }
}
