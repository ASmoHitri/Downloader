package beans;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import dtos.SongFile;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import java.io.*;
import java.util.Optional;

@ApplicationScoped
public class SongFileBean {
    private static Logger LOG = LogManager.getLogger(SongFileBean.class.getName());

    private Client httpClient = ClientBuilder.newClient();

    @Inject
    @DiscoverService("microservice-uploader")
    private Optional<String> basePath;

    public SongFile getSong(int songId) {
        if (basePath.isPresent()) {
            try {
                SongFile songFile =  httpClient.target(basePath.get() + "/api/v1/upload/" + songId)
                        .request().get(new GenericType<SongFile>(){});
                return songFile;
            } catch (WebApplicationException | ProcessingException exception) {
                System.out.println(exception.getMessage());
                return null;
            }
        }
        return null;
    }

    public byte[] getSongFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("Song with given path does not exists.");
            return null;
        }
        byte[] bytesArray = null;
        try {
            bytesArray = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(bytesArray); //read file into bytes[]
            fis.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return bytesArray;

    }
}
